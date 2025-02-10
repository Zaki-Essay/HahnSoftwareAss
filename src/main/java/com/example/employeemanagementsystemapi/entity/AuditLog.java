package com.example.employeemanagementsystemapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@AllArgsConstructor
@Builder
@Setter
@Getter
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;
    private String entity;
    private Long entityId;
    private String changedBy;
    private LocalDateTime timestamp;

    @Column(columnDefinition = "TEXT")
    private String changes;

    public AuditLog() {}

    public AuditLog(String action, String entity, Long entityId, String changedBy, String changes) {
        this.action = action;
        this.entity = entity;
        this.entityId = entityId;
        this.changedBy = changedBy;
        this.changes = changes;
        this.timestamp = LocalDateTime.now();
    }
}
