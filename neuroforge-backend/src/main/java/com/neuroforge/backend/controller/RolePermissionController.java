package com.neuroforge.backend.controller;

import com.neuroforge.backend.dto.rolepermission.RolePermissionRequestDTO;
import com.neuroforge.backend.dto.rolepermission.RolePermissionResponseDTO;
import com.neuroforge.backend.dto.rolepermission.RolePermissionUpdateRequestDTO;
import com.neuroforge.backend.dto.rolepermission.RolePermissionsBulkRequestDTO;
import com.neuroforge.backend.service.RolePermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role-permissions")
@RequiredArgsConstructor
@Tag(name = "Role Permissions", description = "Role permission management endpoints")
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    @GetMapping
    @Operation(summary = "Get all role permissions", description = "Returns a list of all role permissions")
    public List<RolePermissionResponseDTO> getAllRolePermissions() {
        return rolePermissionService.getAllRolePermissions();
    }

    @GetMapping("/role/{roleId}")
    @Operation(summary = "Get permissions by role ID", description = "Returns all permissions for a specific role")
    public List<RolePermissionResponseDTO> getPermissionsByRoleId(@PathVariable Integer roleId) {
        return rolePermissionService.getPermissionsByRoleId(roleId);
    }

    @GetMapping("/module/{moduleId}")
    @Operation(summary = "Get permissions by module ID", description = "Returns all permissions for a specific module")
    public List<RolePermissionResponseDTO> getPermissionsByModuleId(@PathVariable Integer moduleId) {
        return rolePermissionService.getPermissionsByModuleId(moduleId);
    }

    @GetMapping("/{roleId}/{moduleId}")
    @Operation(summary = "Get permission by role and module", description = "Returns a specific permission for a role and module")
    public RolePermissionResponseDTO getPermission(@PathVariable Integer roleId, @PathVariable Integer moduleId) {
        return rolePermissionService.getPermission(roleId, moduleId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new role permission", description = "Creates a new permission for a role and module")
    public RolePermissionResponseDTO createPermission(@Valid @RequestBody RolePermissionRequestDTO request) {
        return rolePermissionService.createPermission(request);
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Bulk assign permissions", description = "Assigns permissions for a role to multiple modules")
    public List<RolePermissionResponseDTO> bulkAssignPermissions(
            @Valid @RequestBody RolePermissionsBulkRequestDTO request) {
        return rolePermissionService.bulkAssignPermissions(request);
    }

    @PutMapping("/{roleId}/{moduleId}")
    @Operation(summary = "Update a role permission", description = "Updates an existing permission for a role and module")
    public RolePermissionResponseDTO updatePermission(
            @PathVariable Integer roleId,
            @PathVariable Integer moduleId,
            @Valid @RequestBody RolePermissionUpdateRequestDTO request) {
        return rolePermissionService.updatePermission(roleId, moduleId, request);
    }

    @DeleteMapping("/{roleId}/{moduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a role permission", description = "Deletes a specific permission for a role and module")
    public void deletePermission(@PathVariable Integer roleId, @PathVariable Integer moduleId) {
        rolePermissionService.deletePermission(roleId, moduleId);
    }

    @DeleteMapping("/role/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete all permissions for a role", description = "Deletes all permissions for a specific role")
    public void deleteAllPermissionsForRole(@PathVariable Integer roleId) {
        rolePermissionService.deleteAllPermissionsForRole(roleId);
    }

    @GetMapping("/check")
    @Operation(summary = "Check if role has specific permission", description = "Checks if a role has a specific permission type for a module")
    public boolean hasPermission(
            @RequestParam Integer roleId,
            @RequestParam Integer moduleId,
            @RequestParam String permissionType) {
        return rolePermissionService.hasPermission(roleId, moduleId, permissionType);
    }
}