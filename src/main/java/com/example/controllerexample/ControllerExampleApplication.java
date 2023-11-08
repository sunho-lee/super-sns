package com.example.controllerexample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ControllerExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControllerExampleApplication.class, args);
        log.info("logger is {}", log.getClass().getSimpleName());
    }

}
