package com.example.employeemanagementsystemapi.service;

import com.example.employeemanagementsystemapi.config.SecurityContext;
import com.example.employeemanagementsystemapi.config.SecurityUtils;
import com.example.employeemanagementsystemapi.entity.Employee;
import com.example.employeemanagementsystemapi.entity.EmploymentStatus;
import com.example.employeemanagementsystemapi.exception.ResourceNotFoundException;
import com.example.employeemanagementsystemapi.repository.EmployeeRepository;
import com.example.employeemanagementsystemapi.validator.EmployeeValidationService;
import jakarta.validation.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final AuditService auditService;
    private  EmployeeValidationService validationService;

    public EmployeeService(EmployeeRepository employeeRepository, AuditService auditService) {
        this.employeeRepository = employeeRepository;
        this.auditService = auditService;
    }

    @Transactional
    public Employee createEmployee(Employee employee) {
        SecurityUtils.checkPermission(SecurityUtils.ROLE_HR, SecurityUtils.ROLE_ADMIN);

        validationService.validateEmployee(employee);
        Employee savedEmployee = employeeRepository.save(employee);
        auditService.logAction("CREATE", savedEmployee.getId().toString(), null, savedEmployee);
        return savedEmployee;
    }

    public Page<Employee> findEmployees(String department, EmploymentStatus status, Pageable pageable) {
        SecurityUtils.checkPermission(SecurityUtils.ROLE_HR, SecurityUtils.ROLE_ADMIN, SecurityUtils.ROLE_MANAGER);

        // For managers, force department filter to their own department
        if (SecurityContext.hasRole(SecurityUtils.ROLE_MANAGER)) {
            department = SecurityContext.getCurrentDepartment();
        }

        if (department != null && status != null) {
            return employeeRepository.findByDepartmentAndStatus(department, status, pageable);
        } else if (department != null) {
            return employeeRepository.findByDepartment(department, pageable);
        } else if (status != null) {
            return employeeRepository.findByStatus(status, pageable);
        }

        return employeeRepository.findAll(pageable);
    }

    public Employee findEmployeeById(Long id) {
        SecurityUtils.checkPermission(SecurityUtils.ROLE_HR, SecurityUtils.ROLE_ADMIN, SecurityUtils.ROLE_MANAGER);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        SecurityUtils.checkDepartmentAccess(employee.getDepartment());

        return employee;
    }

    @Transactional
    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        SecurityUtils.checkPermission(SecurityUtils.ROLE_HR, SecurityUtils.ROLE_ADMIN, SecurityUtils.ROLE_MANAGER);

        Employee existingEmployee = findEmployeeById(id);
        SecurityUtils.checkDepartmentAccess(existingEmployee.getDepartment());

        // If user is a manager, validate restricted fields
        if (SecurityContext.hasRole(SecurityUtils.ROLE_MANAGER)) {
            validateManagerUpdate(existingEmployee, updatedEmployee);
        }

        // Store old state for audit
        Employee oldState = cloneEmployee(existingEmployee);

        // Update fields
        updateEmployeeFields(existingEmployee, updatedEmployee);

        Employee savedEmployee = employeeRepository.save(existingEmployee);
        auditService.logAction("UPDATE", id.toString(), oldState, savedEmployee);

        return savedEmployee;
    }

    @Transactional
    public void deleteEmployee(Long id) {
        SecurityUtils.checkPermission(SecurityUtils.ROLE_HR, SecurityUtils.ROLE_ADMIN);

        Employee employee = findEmployeeById(id);
        employeeRepository.delete(employee);
        auditService.logAction("DELETE", id.toString(), employee, null);
    }

    private void validateManagerUpdate(Employee existing, Employee updated) {
        if (!existing.getDepartment().equals(updated.getDepartment())) {
            throw new AccessDeniedException("Managers cannot change department");
        }
        if (!existing.getEmployeeId().equals(updated.getEmployeeId())) {
            throw new AccessDeniedException("Managers cannot change employee ID");
        }
        if (!existing.getHireDate().equals(updated.getHireDate())) {
            throw new AccessDeniedException("Managers cannot change hire date");
        }
    }

    private Employee cloneEmployee(Employee employee) {
        //TODO Implement deep clone logic here

        Employee clone = new Employee();
        clone.setId(employee.getId());
        clone.setEmployeeId(employee.getEmployeeId());
        //TODO ... clone other fields
        return clone;
    }

    public void updateEmployeeFields(Employee existing, Employee updated) {
        // Validate the updated employee first
        validationService.validateEmployee(updated);

        // Store original department for security check
        String originalDepartment = existing.getDepartment();

        // Update basic information
        existing.setFullName(updated.getFullName());
        existing.setJobTitle(updated.getJobTitle());

        // Department changes are restricted
        if (!SecurityContext.hasRole(SecurityUtils.ROLE_MANAGER)) {
            existing.setDepartment(updated.getDepartment());
        } else if (!originalDepartment.equals(updated.getDepartment())) {
            throw new AccessDeniedException("Managers cannot change department");
        }

        // Update status
        updateEmploymentStatus(existing, updated);

        // Update contact information
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());

        // Update address
        existing.setAddress(updated.getAddress());
        // Restricted fields  HR/Admin can update
        if (SecurityContext.hasAnyRole(SecurityUtils.ROLE_HR, SecurityUtils.ROLE_ADMIN)) {
            existing.setEmployeeId(updated.getEmployeeId());
            existing.setHireDate(updated.getHireDate());
        }
    }

    private void updateEmploymentStatus(Employee existing, Employee updated) {
        if (existing.getStatus() != updated.getStatus()) {
            validateStatusTransition(existing.getStatus(), updated.getStatus());
            existing.setStatus(updated.getStatus());
        }
    }

    private void validateStatusTransition(EmploymentStatus currentStatus, EmploymentStatus newStatus) {

        if (currentStatus == EmploymentStatus.TERMINATED &&
                newStatus != EmploymentStatus.TERMINATED) {
            throw new ValidationException("Cannot change status of terminated employee");
        }
    }
}
