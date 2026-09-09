package com.neuroforge.backend.dto.requirement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequirementRequestDTO {

    @NotNull(message = "Project ID is required")
    private Integer projectId;

    @NotBlank(message = "Requirement text is required")
    private String requirementText;

    @Builder.Default
    private String status = "Draft";

    @Builder.Default
    private Boolean isArchived = false;
}