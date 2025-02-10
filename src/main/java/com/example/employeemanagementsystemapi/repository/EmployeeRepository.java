package com.example.employeemanagementsystemapi.repository;

import com.example.employeemanagementsystemapi.entity.Employee;
import com.example.employeemanagementsystemapi.type.EmploymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    List<Employee> findByFullNameContainingIgnoreCaseOrEmployeeIdContainingIgnoreCaseOrDepartmentContainingIgnoreCaseOrJobTitleContainingIgnoreCase(
            String fullName, String employeeId, String department, String jobTitle);

}
