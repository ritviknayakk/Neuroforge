package com.neuroforge.backend.dto.rolepermission;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermissionResponseDTO {

    private Integer roleId;
    private String roleName;
    private Integer moduleId;
    private String moduleName;
    private Boolean canView;
    private Boolean canCreate;
    private Boolean canEdit;
    private Boolean canDelete;
}