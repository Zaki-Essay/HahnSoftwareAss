package com.example.employeemanagementsystemapi.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserSession {
    private String username;
    private Set<String> roles;
    private String department;
}