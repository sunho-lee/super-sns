package com.example.controllerexample.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;


/**
 * The type User invalid exception.
 * 해당 리소스에 접근이 허용되지 않은 유저일 경우 발생.
 * userId와 현재 context에서 인증된 유저 아이디가 다를 경우 발생한다.
 */
public class UserInvalidException extends ErrorResponseException {

    /**
     * Instantiates a new User invalid exception.
     *
     * @param id the id
     */
    public UserInvalidException(Long id) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, asProblemDetail("User with id : " + id + " is invalid" ), null);
    }

    private static ProblemDetail asProblemDetail(String message){
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, message);
        pd.setTitle("Invalid user");
        return pd;
    }
}
