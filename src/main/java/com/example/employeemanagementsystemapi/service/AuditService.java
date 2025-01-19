package com.example.employeemanagementsystemapi.service;


import com.example.employeemanagementsystemapi.config.SecurityContext;
import com.example.employeemanagementsystemapi.entity.AuditLog;
import com.example.employeemanagementsystemapi.entity.Employee;
import com.example.employeemanagementsystemapi.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuditService {
    private final AuditLogRepository auditLogRepository;

    public void logAction(String action, String entityId, Employee oldState, Employee newState) {
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(action);
        auditLog.setEntityType("Employee");
        auditLog.setEntityId(entityId);
        auditLog.setModifiedBy(SecurityContext.getCurrentUser().getUsername());
        auditLog.setModifiedDate(LocalDate.now());

        if (oldState != null || newState != null) {
            String changes = generateChangesDescription(oldState, newState);
            auditLog.setChanges(changes);
        }

        auditLogRepository.save(auditLog);
    }

    private String generateChangesDescription(Employee oldState, Employee newState) {
        if (oldState == null && newState != null) {
            return "Created new employee record";
        }
        if (oldState != null && newState == null) {
            return "Deleted employee record";
        }

        StringBuilder changes = new StringBuilder();
        // Implement change detection logic here
        return changes.toString();
    }
}

