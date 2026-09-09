package com.neuroforge.backend.dto.designsuggestion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DesignSuggestionRequestDTO {

    @NotNull(message = "Project ID is required")
    private Integer projectId;

    private Integer requirementId;

    private String title;

    @NotBlank(message = "Components description is required")
    private String componentsDescription;

    @Builder.Default
    private Boolean isAIGenerated = true;

    @Builder.Default
    private String status = "Pending";
}