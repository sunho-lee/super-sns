package com.example.supersns.user.dto;

import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(

        @NotBlank(message = "username cannot blank")
        String username,
        @NotBlank(message = "nickname cannot blank")
        String nickname,
        @NotBlank(message = "password cannot blank")
        String password

) {
}
