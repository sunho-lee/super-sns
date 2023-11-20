package com.example.controllerexample.config;

import com.example.controllerexample.auth.CustomUserDetails;
import com.example.controllerexample.user.User;
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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.security.Principal;
import java.util.function.Supplier;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
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
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/auth/signin").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/users/{userId}").access(this::hasUserId)
                                .anyRequest().authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
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
