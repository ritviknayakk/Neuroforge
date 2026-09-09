package com.neuroforge.backend.dto.task;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponseDTO {

    private Integer taskId;
    private Integer projectId;
    private String projectName;
    private Integer userStoryId;
    private String userStoryText;
    private Integer sprintId;
    private String sprintName;
    private String title;
    private String description;
    private Integer assignedToUserId;
    private String assignedToFullName;
    private String status;
    private String priority;
    private LocalDate dueDate;
    private Boolean isArchived;
    private Integer createdByUserId;
    private String createdByFullName;
    private LocalDateTime createdAt;
    private Integer modifiedByUserId;
    private String modifiedByFullName;
    private LocalDateTime modifiedAt;
}