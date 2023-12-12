package com.example.supersns.auth;


import jakarta.validation.constraints.NotBlank;

public record SignInRequest(

        @NotBlank(message = "username cannot blank")
        String username,

        @NotBlank(message = "password cannot blank")
        String password
) {
}
