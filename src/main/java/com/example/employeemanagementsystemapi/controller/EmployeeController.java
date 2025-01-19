package com.example.employeemanagementsystemapi.controller;

import com.example.employeemanagementsystemapi.entity.Employee;
import com.example.employeemanagementsystemapi.entity.EmploymentStatus;
import com.example.employeemanagementsystemapi.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "Employee Management", description = "APIs for managing employee records")
@SecurityRequirement(name = "bearerAuth")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @Operation(
            summary = "Create new employee",
            description = "Creates a new employee record. Requires HR or Admin role."
    )
    @ApiResponse(responseCode = "201", description = "Employee created successfully")
    @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    @ApiResponse(responseCode = "400", description = "Invalid employee data")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee createdEmployee = employeeService.createEmployee(employee);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
            summary = "Find employees",
            description = "Retrieves a paginated list of employees with optional filters. Managers can only view their department."
    )
    public ResponseEntity<Page<Employee>> findEmployees(
            @Parameter(description = "Filter by department")
            @RequestParam(required = false) String department,
            @Parameter(description = "Filter by employment status")
            @RequestParam(required = false) EmploymentStatus status,
            Pageable pageable) {
        Page<Employee> employees = employeeService.findEmployees(department, status, pageable);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Find employee by ID",
            description = "Retrieves an employee by their ID. Managers can only view employees in their department."
    )
    @ApiResponse(responseCode = "200", description = "Employee found")
    @ApiResponse(responseCode = "404", description = "Employee not found")
    @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    public ResponseEntity<Employee> findEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.findEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update employee",
            description = "Updates an existing employee record. Managers have limited update capabilities."
    )
    @ApiResponse(responseCode = "200", description = "Employee updated successfully")
    @ApiResponse(responseCode = "404", description = "Employee not found")
    @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    @ApiResponse(responseCode = "400", description = "Invalid employee data")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @RequestBody Employee employee) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete employee",
            description = "Deletes an employee record. Requires HR or Admin role."
    )
    @ApiResponse(responseCode = "204", description = "Employee deleted successfully")
    @ApiResponse(responseCode = "404", description = "Employee not found")
    @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
