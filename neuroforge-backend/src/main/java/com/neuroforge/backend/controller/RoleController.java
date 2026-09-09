package com.neuroforge.backend.controller;

import com.neuroforge.backend.dto.role.RoleRequestDTO;
import com.neuroforge.backend.dto.role.RoleResponseDTO;
import com.neuroforge.backend.dto.role.RoleUpdateRequestDTO;
import com.neuroforge.backend.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "Role management endpoints")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    @Operation(summary = "Get all roles", description = "Returns a list of all roles")
    public List<RoleResponseDTO> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get role by ID", description = "Returns a single role by its ID")
    public RoleResponseDTO getRoleById(@PathVariable Integer id) {
        return roleService.getRoleById(id);
    }

    @GetMapping("/name/{roleName}")
    @Operation(summary = "Get role by name", description = "Returns a single role by its name (case insensitive)")
    public RoleResponseDTO getRoleByName(@PathVariable String roleName) {
        return roleService.getRoleByName(roleName);
    }

    @GetMapping("/search")
    @Operation(summary = "Search roles by name", description = "Returns roles matching the search term (case insensitive)")
    public List<RoleResponseDTO> searchRolesByName(@RequestParam String query) {
        return roleService.searchRolesByName(query);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new role", description = "Creates a new role with the provided details")
    public RoleResponseDTO createRole(@Valid @RequestBody RoleRequestDTO request) {
        return roleService.createRole(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing role", description = "Updates an existing role's details")
    public RoleResponseDTO updateRole(@PathVariable Integer id, @Valid @RequestBody RoleUpdateRequestDTO request) {
        return roleService.updateRole(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a role", description = "Deletes a role by its ID")
    public void deleteRole(@PathVariable Integer id) {
        roleService.deleteRole(id);
    }

    @GetMapping("/count")
    @Operation(summary = "Get total number of roles", description = "Returns the total count of roles in the system")
    public long getRoleCount() {
        return roleService.getRoleCount();
    }
}