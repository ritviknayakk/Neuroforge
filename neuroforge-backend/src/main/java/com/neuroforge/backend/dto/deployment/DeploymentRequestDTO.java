package com.neuroforge.backend.dto.deployment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeploymentRequestDTO {

    @NotNull(message = "Project ID is required")
    private Integer projectId;

    @NotBlank(message = "Version is required")
    private String version;

    @Builder.Default
    private String status = "InProgress";

    @NotNull(message = "Triggered by user ID is required")
    private Integer triggeredByUserId;

    private String errorLogUrl;
}