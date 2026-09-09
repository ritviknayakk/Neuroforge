package com.neuroforge.backend.dto.auditlog;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogSearchRequestDTO {

    private Integer userId;
    private String action;
    private String entityType;
    private Integer entityId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String ipAddress;
}