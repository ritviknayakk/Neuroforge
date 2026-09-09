package com.neuroforge.backend.dto.deployment;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeploymentUpdateRequestDTO {

    private String status;
    private String errorLogUrl;
}