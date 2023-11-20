package com.example.controllerexample.user.dto;

import java.util.Set;

public record UserResponse(
        Long id,
        String username,
        String nickname,
        Set<String> roles
) {
}
