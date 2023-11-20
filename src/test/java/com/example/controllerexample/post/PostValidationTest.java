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
    @DisplayName("content Validate 검증 성공")
    public void SuccessValidPostRequest(){
        PostRequest postRequest = new PostRequest("test");
        Set<ConstraintViolation<PostRequest>> violations = validator.validate(postRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("content가 null인 경우 실패")
    public void FailureValidPostRequestDescIsNull(){
        PostRequest postRequest = new PostRequest(null);
        Set<ConstraintViolation<PostRequest>> violations = validator.validate(postRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("content가 비어있는 경우 실패")
    public void FailureValidPostRequestDescIsEmpty(){
        PostRequest postRequest = new PostRequest("");
        Set<ConstraintViolation<PostRequest>> violations = validator.validate(postRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("content가 공백만 있는 경우 실패")
    public void FailureValidPostRequestDescIsBlank(){
        PostRequest postRequest = new PostRequest(" ");
        Set<ConstraintViolation<PostRequest>> violations = validator.validate(postRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("content가 180자 이상인 경우 실패")
    public void FailureValidPostRequestDescIsTooLong(){
        String s = "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij" +
                "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghi" +
                "abcdefghijabcdefghijabcdefghij212345678901";
        Post postRequest = new Post( s);
        Set<ConstraintViolation<Post>> violations = validator.validate(postRequest);
        assertFalse(violations.isEmpty());
    }

}
