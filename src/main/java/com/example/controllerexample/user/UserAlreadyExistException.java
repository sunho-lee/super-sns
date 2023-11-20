package com.example.controllerexample.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class UserAlreadyExistException extends ErrorResponseException {

    public UserAlreadyExistException(String username) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, asProblemDetail("User with : " + username + " already exist" ), null);
    }

    private static ProblemDetail asProblemDetail(String message){
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, message);
        pd.setTitle("User already exist.");
        return pd;
    }
}
