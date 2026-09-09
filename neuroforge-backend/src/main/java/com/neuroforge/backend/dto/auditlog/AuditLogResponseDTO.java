package com.neuroforge.backend.dto.auditlog;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogResponseDTO {

    private Long auditLogId;
    private Integer userId;
    private String userFullName;
    private String userEmail;
    private String action;
    private String entityType;
    private Integer entityId;
    private String details;
    private String ipAddress;
    private LocalDateTime createdAt;
}