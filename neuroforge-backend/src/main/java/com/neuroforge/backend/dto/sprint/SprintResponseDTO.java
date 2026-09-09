package com.neuroforge.backend.dto.sprint;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SprintResponseDTO {

    private Integer sprintId;
    private Integer projectId;
    private String projectName;
    private String sprintName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Integer createdByUserId;
    private String createdByFullName;
    private LocalDateTime createdAt;
    private Integer modifiedByUserId;
    private String modifiedByFullName;
    private LocalDateTime modifiedAt;
    private Integer durationInDays;
}