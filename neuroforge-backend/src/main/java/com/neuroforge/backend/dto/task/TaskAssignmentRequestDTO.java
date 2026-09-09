package com.neuroforge.backend.dto.task;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskAssignmentRequestDTO {

    private Integer assignedToUserId;
}