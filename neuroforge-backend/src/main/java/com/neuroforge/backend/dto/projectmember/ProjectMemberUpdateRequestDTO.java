package com.neuroforge.backend.dto.projectmember;

import jakarta.validation.constraints.NotNull; // ← Add this import
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectMemberUpdateRequestDTO {

    @NotNull(message = "Project Role ID is required")
    private Integer projectRoleId;

    private Integer removedByUserId;
}