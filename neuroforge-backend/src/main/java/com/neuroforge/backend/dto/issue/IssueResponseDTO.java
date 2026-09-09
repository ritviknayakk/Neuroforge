package com.neuroforge.backend.dto.issue;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueResponseDTO {

    private Integer issueId;
    private Integer projectId;
    private String projectName;
    private String title;
    private String description;
    private String severity;
    private String status;
    private Integer reportedByUserId;
    private String reportedByFullName;
    private Integer assignedToUserId;
    private String assignedToFullName;
    private Integer relatedTestRunId;
    private Integer relatedTaskId;
    private Boolean isArchived;
    private LocalDateTime createdAt;
    private Integer modifiedByUserId;
    private String modifiedByFullName;
    private LocalDateTime modifiedAt;
    private Integer closedByUserId;
    private String closedByFullName;
    private LocalDateTime closedAt;
}