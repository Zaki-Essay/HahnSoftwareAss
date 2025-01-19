package com.example.employeemanagementsystemapi.validator;

import com.example.employeemanagementsystemapi.entity.Employee;
import com.example.employeemanagementsystemapi.entity.EmploymentStatus;
import jakarta.validation.ValidationException;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class EmployeeValidationService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+?[1-9]\\d{1,14}$");

    private static final Pattern EMPLOYEE_ID_PATTERN =
            Pattern.compile("^[A-Z]{2}\\d{4}$");

    public void validateEmployee(Employee employee) {
        validateEmployeeId(employee.getEmployeeId());
        validateFullName(employee.getFullName());
        validateJobTitle(employee.getJobTitle());
        validateDepartment(employee.getDepartment());
        validateHireDate(employee.getHireDate());
        validateStatus(employee.getStatus());
        validateContactInfo(employee.getEmail(), employee.getPhone());
        validateAddress(employee.getAddress());
    }

    private void validateEmployeeId(String employeeId) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new ValidationException("Employee ID is required");
        }
        if (!EMPLOYEE_ID_PATTERN.matcher(employeeId).matches()) {
            throw new ValidationException("Employee ID must be in format XX0000 (2 uppercase letters followed by 4 digits)");
        }
    }

    private void validateFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new ValidationException("Full name is required");
        }
        if (fullName.length() < 2 || fullName.length() > 100) {
            throw new ValidationException("Full name must be between 2 and 100 characters");
        }
        if (!fullName.matches("^[a-zA-Z\\s\\-']+$")) {
            throw new ValidationException("Full name contains invalid characters");
        }
    }

    private void validateJobTitle(String jobTitle) {
        if (jobTitle == null || jobTitle.trim().isEmpty()) {
            throw new ValidationException("Job title is required");
        }
        if (jobTitle.length() < 2 || jobTitle.length() > 50) {
            throw new ValidationException("Job title must be between 2 and 50 characters");
        }
    }

    private void validateDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            throw new ValidationException("Department is required");
        }
    }

    private void validateHireDate(LocalDate hireDate) {
        if (hireDate == null) {
            throw new ValidationException("Hire date is required");
        }
        if (hireDate.isAfter(LocalDate.now())) {
            throw new ValidationException("Hire date cannot be in the future");
        }
    }

    private void validateStatus(EmploymentStatus status) {
        if (status == null) {
            throw new ValidationException("Employment status is required");
        }
    }

    private void validateContactInfo(String email, String phone) {
        // Email validation
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email is required");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Invalid email format");
        }

        // Phone validation
        if (phone == null || phone.trim().isEmpty()) {
            throw new ValidationException("Phone number is required");
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new ValidationException("Invalid phone number format");
        }
    }

    private void validateAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new ValidationException("address is required");
        }
        if (address.length() < 2 || address.length() > 100) {
            throw new ValidationException("addressmust be between 2 and 100 characters");
        }}
    }

