package com.example.controllerexample.post;

import jakarta.validation.constraints.NotBlank;

public record PostRequest(
        @NotBlank(message = "title cannot be blank")
        String title,
        @NotBlank(message = "description cannot be blank")
        String desc) {
}
