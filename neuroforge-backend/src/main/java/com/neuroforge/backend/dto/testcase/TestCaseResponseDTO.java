package com.neuroforge.backend.dto.testcase;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestCaseResponseDTO {

    private Integer testCaseId;
    private Integer projectId;
    private String projectName;
    private Integer userStoryId;
    private String userStoryText;
    private String featureName;
    private String steps;
    private String expectedResult;
    private Boolean isAIGenerated;
    private String status;
    private Integer reviewedByUserId;
    private String reviewedByFullName;
    private LocalDateTime reviewedAt;
    private Boolean isArchived;
    private Integer createdByUserId;
    private String createdByFullName;
    private LocalDateTime createdAt;
    private Integer modifiedByUserId;
    private String modifiedByFullName;
    private LocalDateTime modifiedAt;
}