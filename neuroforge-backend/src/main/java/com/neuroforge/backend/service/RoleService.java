package com.neuroforge.backend.service;

import com.neuroforge.backend.dto.role.RoleRequestDTO;
import com.neuroforge.backend.dto.role.RoleResponseDTO;
import com.neuroforge.backend.dto.role.RoleUpdateRequestDTO;
import com.neuroforge.backend.entity.Role;
import com.neuroforge.backend.mapper.RoleMapper;
import com.neuroforge.backend.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Transactional(readOnly = true)
    public List<RoleResponseDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoleResponseDTO getRoleById(Integer id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
        return roleMapper.toResponseDto(role);
    }

    @Transactional(readOnly = true)
    public RoleResponseDTO getRoleByName(String roleName) {
        Role role = roleRepository.findByRoleNameIgnoreCase(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found with name: " + roleName));
        return roleMapper.toResponseDto(role);
    }

    @Transactional(readOnly = true)
    public List<RoleResponseDTO> searchRolesByName(String roleName) {
        return roleRepository.findByRoleNameContainingIgnoreCase(roleName).stream()
                .map(roleMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public RoleResponseDTO createRole(RoleRequestDTO request) {
        // Check if role name already exists
        if (roleRepository.existsByRoleNameIgnoreCase(request.getRoleName())) {
            throw new RuntimeException("Role with name '" + request.getRoleName() + "' already exists");
        }

        Role role = roleMapper.toEntity(request);
        // Capitalize the first letter of role name
        role.setRoleName(capitalizeFirstLetter(request.getRoleName()));

        Role savedRole = roleRepository.save(role);
        return roleMapper.toResponseDto(savedRole);
    }

    public RoleResponseDTO updateRole(Integer id, RoleUpdateRequestDTO request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        // Check if role name is being changed and if it already exists
        if (request.getRoleName() != null && !request.getRoleName().equalsIgnoreCase(role.getRoleName())) {
            if (roleRepository.existsByRoleNameIgnoreCase(request.getRoleName())) {
                throw new RuntimeException("Role with name '" + request.getRoleName() + "' already exists");
            }
            // Capitalize the role name
            request.setRoleName(capitalizeFirstLetter(request.getRoleName()));
        }

        roleMapper.updateEntityFromDto(request, role);
        Role updatedRole = roleRepository.save(role);
        return roleMapper.toResponseDto(updatedRole);
    }

    public void deleteRole(Integer id) {
        // Check if role exists before deleting
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role not found with id: " + id);
        }

        // Optional: Check if role is being used by any users
        // You can add a check here to prevent deletion if users are assigned to this
        // role
        // For now, we'll just delete it
        roleRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long getRoleCount() {
        return roleRepository.count();
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}