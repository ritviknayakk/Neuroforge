package com.neuroforge.backend.dto.rolepermission;

import jakarta.validation.constraints.NotNull; // ← Add this import
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermissionsBulkRequestDTO {

    @NotNull(message = "Role ID is required")
    private Integer roleId;

    @NotNull(message = "Module IDs list is required")
    private List<Integer> moduleIds;

    private Boolean canView;
    private Boolean canCreate;
    private Boolean canEdit;
    private Boolean canDelete;
}