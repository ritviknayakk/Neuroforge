package com.neuroforge.backend.dto.projectmember;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectMemberResponseDTO {

    private Integer projectMemberId;
    private Integer projectId;
    private String projectName;
    private Integer userId;
    private String userFullName;
    private Integer projectRoleId;
    private String projectRoleName;
    private Integer addedByUserId;
    private String addedByFullName;
    private LocalDateTime joinedAt;
    private Integer removedByUserId;
    private String removedByFullName;
    private LocalDateTime removedAt;
    private Boolean isActive; // true if not removed
}