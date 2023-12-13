package com.example.supersns.post;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class PostNotFoundException extends ErrorResponseException {
    public PostNotFoundException(Long postId) {
        super(HttpStatus.NOT_FOUND, asProblemDetail("Post with id: " + postId + " not found"), null);
    }

    private static ProblemDetail asProblemDetail(String message) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
        pd.setTitle("Post not found.");
        return pd;
    }

}
