package com.neuroforge.backend.dto.project;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponseDTO {

    private Integer projectId;
    private String projectName;
    private String description;
    private String status;
    private Integer createdByUserId;
    private LocalDateTime createdAt;
    private Integer modifiedByUserId;
    private LocalDateTime modifiedAt;
    private LocalDateTime archivedAt;
}