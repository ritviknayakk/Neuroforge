package com.neuroforge.backend.dto.rolepermission;

import jakarta.validation.constraints.NotNull;  // ← Add this import
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermissionRequestDTO {

    @NotNull(message = "Role ID is required")
    private Integer roleId;

    @NotNull(message = "Module ID is required")
    private Integer moduleId;

    @Builder.Default
    private Boolean canView = false;

    @Builder.Default
    private Boolean canCreate = false;

    @Builder.Default
    private Boolean canEdit = false;

    @Builder.Default
    private Boolean canDelete = false;
}