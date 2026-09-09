package com.neuroforge.backend.dto.issue;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueUpdateRequestDTO {

    private String title;
    private String description;
    private String severity;
    private String status;
    private Integer assignedToUserId;
    private Integer relatedTestRunId;
    private Integer relatedTaskId;
    private Boolean isArchived;
}