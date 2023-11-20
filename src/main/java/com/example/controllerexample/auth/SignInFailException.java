package com.example.controllerexample.auth;

public class SignInFailException extends Throwable {
    public SignInFailException(Exception e) {
        super(e);
    }
}
