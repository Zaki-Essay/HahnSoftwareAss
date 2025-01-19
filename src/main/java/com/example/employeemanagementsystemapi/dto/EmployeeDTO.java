package com.example.employeemanagementsystemapi.dto;

import com.example.employeemanagementsystemapi.entity.EmploymentStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeDTO {
    private Long id;

    private String employeeId;

    private String fullName;

    private String jobTitle;

    private String department;

    private LocalDate hireDate;

    private EmploymentStatus status;

    private String email;

    private String phone;

    private String address;
}