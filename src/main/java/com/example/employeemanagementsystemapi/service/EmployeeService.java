package com.example.employeemanagementsystemapi.service;

import com.example.employeemanagementsystemapi.entity.Employee;
import com.example.employeemanagementsystemapi.repository.EmployeeRepository;
import com.example.employeemanagementsystemapi.specification.EmployeeSpecification;
import jakarta.validation.Valid;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public Employee addEmployee(@Valid Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id, String userDepartment, boolean isManager) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            if (isManager && !employee.get().getDepartment().equals(userDepartment)) {
                throw new RuntimeException("Access Denied: Managers can only view employees in their department.");
            }
            return employee;
        }
        return Optional.empty();
    }

    @Transactional
    public Employee updateEmployee(Long id, @Valid Employee updatedEmployee, String userDepartment, boolean isManager) {
        return employeeRepository.findById(id).map(employee -> {
            if (isManager && !employee.getDepartment().equals(userDepartment)) {
                throw new RuntimeException("Access Denied: Managers can only update employees in their department.");
            }

            if (isManager) {
                employee.setJobTitle(updatedEmployee.getJobTitle());
                employee.setEmploymentStatus(updatedEmployee.getEmploymentStatus());
            } else {
                employee.setFullName(updatedEmployee.getFullName());
                employee.setEmployeeId(updatedEmployee.getEmployeeId());
                employee.setJobTitle(updatedEmployee.getJobTitle());
                employee.setDepartment(updatedEmployee.getDepartment());
                employee.setHireDate(updatedEmployee.getHireDate());
                employee.setEmploymentStatus(updatedEmployee.getEmploymentStatus());
                employee.setContactNumber(updatedEmployee.getContactNumber());
                employee.setAddress(updatedEmployee.getAddress());
            }

            return employeeRepository.save(employee);
        }).orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public List<Employee> searchEmployees(String query) {
        return employeeRepository.findByFullNameContainingIgnoreCaseOrEmployeeIdContainingIgnoreCaseOrDepartmentContainingIgnoreCaseOrJobTitleContainingIgnoreCase(
                query, query, query, query);
    }

    public List<Employee> filterEmployees(String department, String employmentStatus, LocalDate hireDate) {
        Specification<Employee> spec = EmployeeSpecification.filterBy(department, employmentStatus, hireDate);
        return employeeRepository.findAll(spec);
    }
}
