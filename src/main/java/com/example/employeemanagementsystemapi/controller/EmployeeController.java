package com.example.employeemanagementsystemapi.controller;

import com.example.employeemanagementsystemapi.entity.Employee;
import com.example.employeemanagementsystemapi.type.EmploymentStatus;
import com.example.employeemanagementsystemapi.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PreAuthorize("hasRole('HR_PERSONNEL') or hasRole('ADMIN')")
    @PostMapping
    public Employee addEmployee(@Valid @RequestBody Employee employee) {
        return employeeService.addEmployee(employee);
    }


    @PreAuthorize("hasRole('HR_PERSONNEL') or hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }


    @PreAuthorize("hasRole('HR_PERSONNEL') or hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public Optional<Employee> getEmployeeById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String role = userDetails.getAuthorities().stream().findFirst().get().getAuthority();
        String userDepartment = "IT"; //TODO This should be fetched dynamically from user details
        boolean isManager = role.equals("ROLE_MANAGER");

        return employeeService.getEmployeeById(id, userDepartment, isManager);
    }


    @PreAuthorize("hasRole('HR_PERSONNEL') or hasRole('MANAGER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id,@Valid @RequestBody Employee updatedEmployee,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        String role = userDetails.getAuthorities().stream().findFirst().get().getAuthority();
        String userDepartment = "IT"; //TODO This should be fetched dynamically from user details
        boolean isManager = role.equals("ROLE_MANAGER");

        return employeeService.updateEmployee(id, updatedEmployee, userDepartment, isManager);
    }


    @PreAuthorize("hasRole('HR_PERSONNEL') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }

    @GetMapping("/search")
    public List<Employee> searchEmployees(@RequestParam String query) {
        return employeeService.searchEmployees(query);
    }

    @GetMapping("/filter")
    public List<Employee> filterEmployees(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String employmentStatus,
            @RequestParam(required = false) LocalDate hireDate) { // @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        return employeeService.filterEmployees(department, employmentStatus, hireDate);
    }
}
