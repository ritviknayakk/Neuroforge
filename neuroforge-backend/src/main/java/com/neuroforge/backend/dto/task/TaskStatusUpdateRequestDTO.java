package com.neuroforge.backend.dto.task;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskStatusUpdateRequestDTO {

    @NotBlank(message = "Status is required")
    private String status;
}