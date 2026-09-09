package com.neuroforge.backend.dto.issue;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueRequestDTO {

    @NotNull(message = "Project ID is required")
    private Integer projectId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Severity is required")
    private String severity; // Low, Medium, High

    @Builder.Default
    private String status = "Open";

    @NotNull(message = "Reported by user ID is required")
    private Integer reportedByUserId;

    private Integer assignedToUserId;

    private Integer relatedTestRunId;

    private Integer relatedTaskId;

    @Builder.Default
    private Boolean isArchived = false;
}