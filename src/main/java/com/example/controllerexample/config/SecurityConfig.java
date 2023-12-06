package com.example.controllerexample.config;

import com.example.controllerexample.auth.CustomUserDetails;
import com.example.controllerexample.user.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;

import java.io.PrintWriter;
import java.security.Principal;
import java.util.function.Supplier;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/signin").anonymous()
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users").anonymous()
                        .requestMatchers(HttpMethod.GET, "/api/v1/followers/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/followees/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/{userId}").access(this::hasUserId)
                        .anyRequest().authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS).disable())
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/signout")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .addLogoutHandler(new HeaderWriterLogoutHandler(
                                new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.ALL)))
                        .logoutSuccessHandler((req, res, auth) -> {
                            if (auth == null) {
                                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                PrintWriter writer = res.getWriter();
                                writer.write("auth is null");
                            } else {
                                res.setStatus(HttpServletResponse.SC_OK);
                                PrintWriter writer = res.getWriter();
                                writer.write("signout successfully");
                            }
                        })
                )
                .exceptionHandling(authorize ->
                        authorize.authenticationEntryPoint(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
                        ));
        return http.build();
    }

    private AuthorizationDecision hasUserId(Supplier<Authentication> authenticationSupplier,
                                            RequestAuthorizationContext requestAuthorizationContext) {

        CustomUserDetails auth = (CustomUserDetails) authenticationSupplier.get().getPrincipal();
        String id = String.valueOf(auth.getId());
        String userId = requestAuthorizationContext.getVariables().get("userId");

        return new AuthorizationDecision(id.equals(userId));
    }

}
