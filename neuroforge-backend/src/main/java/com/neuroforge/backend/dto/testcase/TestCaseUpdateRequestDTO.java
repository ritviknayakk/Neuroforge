package com.neuroforge.backend.dto.testcase;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestCaseUpdateRequestDTO {

    private String featureName;
    private String steps;
    private String expectedResult;
    private Boolean isAIGenerated;
    private String status;
    private Boolean isArchived;
}