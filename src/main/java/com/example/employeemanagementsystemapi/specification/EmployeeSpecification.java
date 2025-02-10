package com.example.employeemanagementsystemapi.specification;

import com.example.employeemanagementsystemapi.entity.Employee;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecification {

    public static Specification<Employee> filterBy(String department, String employmentStatus, LocalDate hireDate) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (department != null && !department.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("department"), department));
            }

            if (employmentStatus != null && !employmentStatus.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("employmentStatus"), employmentStatus));
            }

            if (hireDate != null) {
                predicates.add(criteriaBuilder.equal(root.get("hireDate"), hireDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

