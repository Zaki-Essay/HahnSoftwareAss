package com.example.employeemanagementsystemapi.repository;

import com.example.employeemanagementsystemapi.entity.Employee;
import com.example.employeemanagementsystemapi.entity.EmploymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Page<Employee> findByDepartmentAndStatus(String department, EmploymentStatus status, Pageable pageable);

    Page<Employee> findByDepartment(String department, Pageable pageable);

    Page<Employee> findByStatus(EmploymentStatus status, Pageable pageable);
}
