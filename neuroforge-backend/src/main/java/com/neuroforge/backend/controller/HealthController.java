package com.neuroforge.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
@Tag(name = "Health Check", description = "Application health and status endpoints")
public class HealthController {

    @GetMapping
    @Operation(summary = "Check application health", description = "Returns the current status of the application")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "NeuroForge Backend");
        response.put("version", "0.0.1-SNAPSHOT");
        return response;
    }

    @GetMapping("/ping")
    @Operation(summary = "Ping endpoint", description = "Simple ping-pong endpoint for connectivity testing")
    public Map<String, String> ping() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "pong");
        response.put("timestamp", LocalDateTime.now().toString());
        return response;
    }
}