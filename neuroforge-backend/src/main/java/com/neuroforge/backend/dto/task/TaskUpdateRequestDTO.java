package com.neuroforge.backend.dto.task;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskUpdateRequestDTO {

    private String title;
    private String description;
    private Integer assignedToUserId;
    private String status;
    private String priority;
    private LocalDate dueDate;
    private Boolean isArchived;
}