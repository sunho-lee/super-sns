package com.example.supersns.role;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class RoleNotFoundException extends ErrorResponseException {

    public RoleNotFoundException(String role) {
        super(HttpStatus.NOT_FOUND, asProblemDetail("Role with name : " + role + " not found"), null);
    }

    private static ProblemDetail asProblemDetail(String message) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
        pd.setTitle("Role not found.");
        return pd;
    }
}
