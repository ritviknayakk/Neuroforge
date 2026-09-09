package com.neuroforge.backend.dto.userstory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStoryRequestDTO {

    @NotNull(message = "Requirement ID is required")
    private Integer requirementId;

    private Integer sprintId;

    private String actorType;

    private String goal;

    private String reason;

    @NotBlank(message = "Story text is required")
    private String storyText;

    @Builder.Default
    private String priority = "Medium";

    @Builder.Default
    private String status = "Draft";

    @Builder.Default
    private Boolean isAIGenerated = false;

    @Builder.Default
    private Boolean isArchived = false;
}