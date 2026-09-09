package com.neuroforge.backend.dto.testrun;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestRunResponseDTO {

    private Integer testRunId;
    private Integer testCaseId;
    private String testCaseFeatureName;
    private String executionType;
    private String result;
    private Integer executedByUserId;
    private String executedByFullName;
    private String notes;
    private LocalDateTime executedAt;
}