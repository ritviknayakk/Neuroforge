package com.neuroforge.backend.dto.requirement;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequirementResponseDTO {

    private Integer requirementId;
    private Integer projectId;
    private String projectName;
    private String requirementText;
    private String status;
    private Boolean isArchived;
    private Integer createdByUserId;
    private String createdByFullName;
    private LocalDateTime createdAt;
    private Integer modifiedByUserId;
    private String modifiedByFullName;
    private LocalDateTime modifiedAt;
}