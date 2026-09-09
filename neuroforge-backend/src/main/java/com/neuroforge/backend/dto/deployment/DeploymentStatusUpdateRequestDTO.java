package com.neuroforge.backend.dto.deployment;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeploymentStatusUpdateRequestDTO {

    @NotBlank(message = "Status is required")
    private String status; // InProgress, Successful, Failed
}