package com.neuroforge.backend.dto.sprint;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SprintUpdateRequestDTO {

    private String sprintName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
}