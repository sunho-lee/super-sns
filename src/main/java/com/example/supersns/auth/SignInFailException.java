package com.example.supersns.auth;

public class SignInFailException extends Throwable {
    public SignInFailException(Exception e) {
        super(e);
    }
}
