package com.neuroforge.backend.dto.userstory;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStoryResponseDTO {

    private Integer userStoryId;
    private Integer requirementId;
    private String requirementText;
    private Integer sprintId;
    private String sprintName;
    private String actorType;
    private String goal;
    private String reason;
    private String storyText;
    private String priority;
    private String status;
    private Boolean isAIGenerated;
    private Integer acceptedByUserId;
    private String acceptedByFullName;
    private LocalDateTime acceptedAt;
    private Boolean isArchived;
    private Integer createdByUserId;
    private String createdByFullName;
    private LocalDateTime createdAt;
    private Integer modifiedByUserId;
    private String modifiedByFullName;
    private LocalDateTime modifiedAt;
}