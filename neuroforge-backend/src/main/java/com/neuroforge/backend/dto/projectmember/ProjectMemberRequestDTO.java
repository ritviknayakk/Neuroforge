package com.neuroforge.backend.dto.projectmember;

import jakarta.validation.constraints.NotNull; // ← Add this import
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectMemberRequestDTO {

    @NotNull(message = "Project ID is required")
    private Integer projectId;

    @NotNull(message = "User ID is required")
    private Integer userId;

    @NotNull(message = "Project Role ID is required")
    private Integer projectRoleId;

    @NotNull(message = "Added By User ID is required")
    private Integer addedByUserId;
}