package com.pulga.ratelimiter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping")
public class ApiHandler{
    @GetMapping
    public ResponseEntity<String> ping(){
        return ResponseEntity.status(HttpStatus.OK).body("pong");
    }
}
