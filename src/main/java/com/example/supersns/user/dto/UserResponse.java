package com.example.supersns.user.dto;

import java.util.Set;

public record UserResponse(
        Long id,
        String username,
        String nickname
) {
}
