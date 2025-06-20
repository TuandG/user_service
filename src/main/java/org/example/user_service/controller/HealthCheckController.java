package org.example.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/ping")
@RestController
public class HealthCheckController {
    @GetMapping
    public ResponseEntity<Object> healthCheck() {
        return ResponseEntity.ok("pong");
    }
}
