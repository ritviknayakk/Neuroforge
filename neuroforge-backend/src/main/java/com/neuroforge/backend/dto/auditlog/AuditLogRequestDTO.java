package com.neuroforge.backend.dto.auditlog;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogRequestDTO {

    private Integer userId;

    @NotBlank(message = "Action is required")
    private String action;

    @NotBlank(message = "Entity type is required")
    private String entityType;

    private Integer entityId;

    private String details;

    private String ipAddress;
}