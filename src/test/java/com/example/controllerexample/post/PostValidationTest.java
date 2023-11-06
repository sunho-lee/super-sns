package com.example.controllerexample.post;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PostValidationTest {

    private ValidatorFactory validatorFactory;
    private Validator validator;

    @BeforeEach
    void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    @DisplayName("title이 없는 경우 경우, 실패")
    public void FailureValidPostRequestTitleIsBlank(){
        PostRequest postRequest = new PostRequest("", "desc");
        Set<ConstraintViolation<PostRequest>> violations = validator.validate(postRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("title에 공백만 있는 경우, 실패")
    public void FailureValidPostRequestTitle(){
        PostRequest postRequest2 = new PostRequest(" ", "desc");
        Set<ConstraintViolation<PostRequest>> violations2 = validator.validate(postRequest2);
        assertFalse(violations2.isEmpty());
    }

    @Test
    @DisplayName("desc이 비어있는 경우 실패")
    public void FailureValidPostRequestDescIsEmpty(){
        PostRequest postRequest = new PostRequest("", "desc");
        Set<ConstraintViolation<PostRequest>> violations = validator.validate(postRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("desc이 공백만 있는 경우 실패")
    public void FailureValidPostRequestDescIsBlank(){
        PostRequest postRequest = new PostRequest(" ", "desc");
        Set<ConstraintViolation<PostRequest>> violations = validator.validate(postRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("desc이 180자 이상인 경우 실패")
    public void FailureValidPostRequestDescIsTooLong(){
        String s = "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij" +
                "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghi" +
                "abcdefghijabcdefghijabcdefghij212345678901";
        Post postRequest = new Post("title", s);
        Set<ConstraintViolation<Post>> violations = validator.validate(postRequest);
        assertFalse(violations.isEmpty());
    }

}
