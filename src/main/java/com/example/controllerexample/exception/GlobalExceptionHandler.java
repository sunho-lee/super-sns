package com.example.controllerexample.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(x -> errors.put(x.getField(), x.getDefaultMessage()));

        ProblemDetail pd = ex.getBody();
        pd.setProperty("errors", errors);

        return handleExceptionInternal(ex, pd, headers, status, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handle(ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();

        if (!e.getConstraintViolations().isEmpty()) {
            for (var v : e.getConstraintViolations()) {
                String fieldName = null;
                for (var node : v.getPropertyPath()) {
                    fieldName = node.getName();
                }
                errors.put(fieldName, v.getMessage());
            }
        }

        ProblemDetail pd = ProblemDetail
                .forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, "Validation failed");
        pd.setTitle("Unprocessable Entity");
        pd.setProperty("errors", errors);
        return ResponseEntity.unprocessableEntity().body(pd);
    }

}
