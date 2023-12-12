package com.example.supersns.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank(message = "nickname cannot blank")
        String nickname,
        @NotBlank(message = "password cannot blank")
        String password
) {
}
