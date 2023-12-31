package com.example.supersns.post;

import jakarta.validation.constraints.NotBlank;

public record PostRequest(
        @NotBlank(message = "content cannot be blank")
        String content) {
}
