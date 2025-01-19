package com.example.employeemanagementsystemapi.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ApiResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, String> errors;

    public ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(int status, String message, Map<String, String> errors) {
        this(status, message);
        this.errors = errors;
    }
}
