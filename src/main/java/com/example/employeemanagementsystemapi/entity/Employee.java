package com.example.employeemanagementsystemapi.entity;

import com.example.employeemanagementsystemapi.audit.EmployeeAuditListener;
import com.example.employeemanagementsystemapi.type.EmploymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(EmployeeAuditListener.class)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full Name is required")
    @Size(min = 2, max = 100, message = "Full Name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Employee ID is required")
    @Column(unique = true) // Ensures uniqueness at the database level
    private String employeeId;

    @NotBlank(message = "Job Title is required")
    private String jobTitle;

    @NotBlank(message = "Department is required")
    private String department;

    @NotNull(message = "Hire Date is required")
    private LocalDate hireDate;

    @NotBlank(message = "Employment Status is required")
    @Pattern(regexp = "Active|Inactive|Terminated", message = "Employment Status must be Active, Inactive, or Terminated")
    private String employmentStatus;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Contact Number is required")
    @Pattern(regexp = "^(\\+\\d{1,3})?\\d{9,15}$", message = "Invalid contact number format")
    private String contactNumber;

    @NotBlank(message = "Address is required")
    private String address;
}