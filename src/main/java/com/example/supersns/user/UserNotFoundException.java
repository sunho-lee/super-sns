package com.example.supersns.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class UserNotFoundException extends ErrorResponseException {

    public UserNotFoundException(Long id) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, asProblemDetail("User with id : " + id + " not found" ), null);
    }

    private static ProblemDetail asProblemDetail(String message){
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
        pd.setTitle("User not found.");
        return pd;
    }
}
