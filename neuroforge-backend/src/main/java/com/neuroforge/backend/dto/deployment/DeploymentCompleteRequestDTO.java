package com.neuroforge.backend.dto.deployment;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeploymentCompleteRequestDTO {

    @NotBlank(message = "Status is required")
    private String status; // Successful or Failed

    private String errorLogUrl;
}