package com.neuroforge.backend.dto.designsuggestion;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DesignSuggestionResponseDTO {

    private Integer designSuggestionId;
    private Integer projectId;
    private String projectName;
    private Integer requirementId;
    private String requirementText;
    private String title;
    private String componentsDescription;
    private Boolean isAIGenerated;
    private String status;
    private Integer acceptedByUserId;
    private String acceptedByFullName;
    private LocalDateTime acceptedAt;
    private Integer createdByUserId;
    private String createdByFullName;
    private LocalDateTime createdAt;
    private Integer modifiedByUserId;
    private String modifiedByFullName;
    private LocalDateTime modifiedAt;
}