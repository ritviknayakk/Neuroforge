package com.neuroforge.backend.service;

import com.neuroforge.backend.dto.rolepermission.RolePermissionRequestDTO;
import com.neuroforge.backend.dto.rolepermission.RolePermissionResponseDTO;
import com.neuroforge.backend.dto.rolepermission.RolePermissionUpdateRequestDTO;
import com.neuroforge.backend.dto.rolepermission.RolePermissionsBulkRequestDTO;
import com.neuroforge.backend.entity.Modules;
import com.neuroforge.backend.entity.Role;
import com.neuroforge.backend.entity.RolePermissions;
import com.neuroforge.backend.mapper.RolePermissionMapper;
import com.neuroforge.backend.repository.ModuleRepository;
import com.neuroforge.backend.repository.RolePermissionsRepository;
import com.neuroforge.backend.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RolePermissionService {

    private final RolePermissionsRepository rolePermissionsRepository;
    private final RoleRepository roleRepository;
    private final ModuleRepository moduleRepository;
    private final RolePermissionMapper rolePermissionMapper;

    @Transactional(readOnly = true)
    public List<RolePermissionResponseDTO> getAllRolePermissions() {
        return rolePermissionsRepository.findAll().stream()
                .map(rolePermissionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RolePermissionResponseDTO> getPermissionsByRoleId(Integer roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new RuntimeException("Role not found with id: " + roleId);
        }
        return rolePermissionsRepository.findByRole_RoleId(roleId).stream()
                .map(rolePermissionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RolePermissionResponseDTO> getPermissionsByModuleId(Integer moduleId) {
        if (!moduleRepository.existsById(moduleId)) {
            throw new RuntimeException("Module not found with id: " + moduleId);
        }
        return rolePermissionsRepository.findByModule_ModuleId(moduleId).stream()
                .map(rolePermissionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RolePermissionResponseDTO getPermission(Integer roleId, Integer moduleId) {
        RolePermissions permission = rolePermissionsRepository
                .findByRole_RoleIdAndModule_ModuleId(roleId, moduleId)
                .orElseThrow(() -> new RuntimeException(
                        "Permission not found for role " + roleId + " and module " + moduleId));
        return rolePermissionMapper.toResponseDto(permission);
    }

    public RolePermissionResponseDTO createPermission(RolePermissionRequestDTO request) {
        // Check if role exists
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + request.getRoleId()));

        // Check if module exists
        Modules module = moduleRepository.findById(request.getModuleId())
                .orElseThrow(() -> new RuntimeException("Module not found with id: " + request.getModuleId()));

        // Check if permission already exists
        if (rolePermissionsRepository.existsByRole_RoleIdAndModule_ModuleId(request.getRoleId(),
                request.getModuleId())) {
            throw new RuntimeException("Permission already exists for role " + request.getRoleId() + " and module "
                    + request.getModuleId());
        }

        RolePermissions permission = rolePermissionMapper.toEntity(request);
        permission.setRole(role);
        permission.setModule(module);

        RolePermissions savedPermission = rolePermissionsRepository.save(permission);
        return rolePermissionMapper.toResponseDto(savedPermission);
    }

    public RolePermissionResponseDTO updatePermission(Integer roleId, Integer moduleId,
            RolePermissionUpdateRequestDTO request) {
        RolePermissions permission = rolePermissionsRepository
                .findByRole_RoleIdAndModule_ModuleId(roleId, moduleId)
                .orElseThrow(() -> new RuntimeException(
                        "Permission not found for role " + roleId + " and module " + moduleId));

        rolePermissionMapper.updateEntityFromDto(request, permission);
        RolePermissions updatedPermission = rolePermissionsRepository.save(permission);
        return rolePermissionMapper.toResponseDto(updatedPermission);
    }

    public List<RolePermissionResponseDTO> bulkAssignPermissions(RolePermissionsBulkRequestDTO request) {
        // Check if role exists
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + request.getRoleId()));

        List<RolePermissionResponseDTO> results = new ArrayList<>();

        for (Integer moduleId : request.getModuleIds()) {
            // Check if module exists
            Modules module = moduleRepository.findById(moduleId)
                    .orElseThrow(() -> new RuntimeException("Module not found with id: " + moduleId));

            // Check if permission already exists
            if (rolePermissionsRepository.existsByRole_RoleIdAndModule_ModuleId(request.getRoleId(), moduleId)) {
                // Update existing permission
                RolePermissions existing = rolePermissionsRepository
                        .findByRole_RoleIdAndModule_ModuleId(request.getRoleId(), moduleId)
                        .get();

                if (request.getCanView() != null)
                    existing.setCanView(request.getCanView());
                if (request.getCanCreate() != null)
                    existing.setCanCreate(request.getCanCreate());
                if (request.getCanEdit() != null)
                    existing.setCanEdit(request.getCanEdit());
                if (request.getCanDelete() != null)
                    existing.setCanDelete(request.getCanDelete());

                RolePermissions updated = rolePermissionsRepository.save(existing);
                results.add(rolePermissionMapper.toResponseDto(updated));
            } else {
                // Create new permission
                RolePermissions permission = RolePermissions.builder()
                        .role(role)
                        .module(module)
                        .canView(request.getCanView() != null ? request.getCanView() : false)
                        .canCreate(request.getCanCreate() != null ? request.getCanCreate() : false)
                        .canEdit(request.getCanEdit() != null ? request.getCanEdit() : false)
                        .canDelete(request.getCanDelete() != null ? request.getCanDelete() : false)
                        .build();

                RolePermissions saved = rolePermissionsRepository.save(permission);
                results.add(rolePermissionMapper.toResponseDto(saved));
            }
        }

        return results;
    }

    public void deletePermission(Integer roleId, Integer moduleId) {
        RolePermissions permission = rolePermissionsRepository
                .findByRole_RoleIdAndModule_ModuleId(roleId, moduleId)
                .orElseThrow(() -> new RuntimeException(
                        "Permission not found for role " + roleId + " and module " + moduleId));
        rolePermissionsRepository.delete(permission);
    }

    public void deleteAllPermissionsForRole(Integer roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new RuntimeException("Role not found with id: " + roleId);
        }
        rolePermissionsRepository.deleteByRoleId(roleId);
    }

    @Transactional(readOnly = true)
    public boolean hasPermission(Integer roleId, Integer moduleId, String permissionType) {
        return rolePermissionsRepository.findByRole_RoleIdAndModule_ModuleId(roleId, moduleId)
                .map(permission -> {
                    return switch (permissionType.toLowerCase()) {
                        case "view" -> permission.getCanView();
                        case "create" -> permission.getCanCreate();
                        case "edit" -> permission.getCanEdit();
                        case "delete" -> permission.getCanDelete();
                        default -> false;
                    };
                })
                .orElse(false);
    }
}