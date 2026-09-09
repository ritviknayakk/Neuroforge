package com.neuroforge.backend.dto.userstory;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStoryUpdateRequestDTO {

    private Integer sprintId;
    private String actorType;
    private String goal;
    private String reason;
    private String storyText;
    private String priority;
    private String status;
    private Boolean isAIGenerated;
    private Boolean isArchived;
}