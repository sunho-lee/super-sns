package com.example.controllerexample.config;

import com.example.controllerexample.auth.CustomUserDetails;
import com.example.controllerexample.role.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        CustomUserDetails principal =
                new CustomUserDetails(1L, customUser.name(), customUser.username(), "password");
        Arrays.stream(customUser.authorities()).forEach(auth -> principal.addRole(new Role(auth)));
        Authentication auth = UsernamePasswordAuthenticationToken
                        .authenticated(principal, principal.getPassword(), principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}