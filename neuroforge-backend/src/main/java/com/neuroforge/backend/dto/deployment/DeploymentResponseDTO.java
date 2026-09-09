package com.neuroforge.backend.dto.deployment;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeploymentResponseDTO {

    private Integer deploymentId;
    private Integer projectId;
    private String projectName;
    private String version;
    private String status;
    private Integer triggeredByUserId;
    private String triggeredByFullName;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String errorLogUrl;
    private Long durationInSeconds;
}