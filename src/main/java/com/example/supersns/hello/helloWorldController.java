package com.example.supersns.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class helloWorldController {

    @GetMapping("/hello")
    public String ping(){
        return "Hello World, " + System.getProperty("PID");
    }
}
