package com.example.employeemanagementsystemapi.audit;

import com.example.employeemanagementsystemapi.entity.AuditLog;
import com.example.employeemanagementsystemapi.entity.Employee;
import com.example.employeemanagementsystemapi.repository.AuditLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EmployeeAuditListener {

    private static AuditLogRepository auditLogRepository;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public void setAuditLogRepository(AuditLogRepository repository) {
        auditLogRepository = repository;
    }

    @PrePersist
    public void beforeCreate(Employee employee) {
        String username = getCurrentUser();
        saveAuditLog("CREATE", employee, username, null);
    }

    @PreUpdate
    public void beforeUpdate(Employee employee) {
        String username = getCurrentUser();
        saveAuditLog("UPDATE", employee, username, getChangedFields(employee));
    }

    @PreRemove
    public void beforeDelete(Employee employee) {
        String username = getCurrentUser();
        saveAuditLog("DELETE", employee, username, null);
    }

    private String getCurrentUser() {
        try {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception e) {
            return "SYSTEM";
        }
    }

    private String getChangedFields(Employee employee) {
        try {
            Map<String, Object> changes = new HashMap<>();
            changes.put("jobTitle", employee.getJobTitle());
            changes.put("employmentStatus", employee.getEmploymentStatus());
            changes.put("department", employee.getDepartment());
            return objectMapper.writeValueAsString(changes);
        } catch (Exception e) {
            return "{}";
        }
    }

    private void saveAuditLog(String action, Employee employee, String changedBy, String changes) {
        AuditLog log = new AuditLog(action, "EMPLOYEE", employee.getId(), changedBy, changes);
        auditLogRepository.save(log);
    }
}
