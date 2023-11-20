package com.example.controllerexample.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignInRequest(

        @NotBlank(message = "username cannot blank")
        String username,

        @NotBlank(message = "password cannot blank")
        String password
) {
}
