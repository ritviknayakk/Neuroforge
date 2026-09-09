package com.neuroforge.backend.dto.testcase;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestCaseRequestDTO {

    @NotNull(message = "Project ID is required")
    private Integer projectId;

    private Integer userStoryId;

    @NotBlank(message = "Feature name is required")
    private String featureName;

    @NotBlank(message = "Steps are required")
    private String steps;

    @NotBlank(message = "Expected result is required")
    private String expectedResult;

    @Builder.Default
    private Boolean isAIGenerated = true;

    @Builder.Default
    private String status = "Draft";

    @Builder.Default
    private Boolean isArchived = false;
}