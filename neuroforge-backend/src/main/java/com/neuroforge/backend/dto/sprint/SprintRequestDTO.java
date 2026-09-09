package com.neuroforge.backend.dto.sprint;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SprintRequestDTO {

    @NotNull(message = "Project ID is required")
    private Integer projectId;

    @NotBlank(message = "Sprint name is required")
    private String sprintName;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @Builder.Default
    private String status = "Planned";
}