package com.neuroforge.backend.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequestDTO {

    @NotNull(message = "Project ID is required")
    private Integer projectId;

    private Integer userStoryId;

    private Integer sprintId;

    @NotBlank(message = "Task title is required")
    private String title;

    private String description;

    private Integer assignedToUserId;

    @Builder.Default
    private String status = "ToDo";

    @Builder.Default
    private String priority = "Medium";

    private LocalDate dueDate;

    @Builder.Default
    private Boolean isArchived = false;
}