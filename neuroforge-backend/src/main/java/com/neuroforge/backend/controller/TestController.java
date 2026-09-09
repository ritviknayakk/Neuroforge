package com.neuroforge.backend.controller;

import com.neuroforge.backend.dto.role.RoleResponseDTO;
import com.neuroforge.backend.dto.user.UserResponseDTO;
import com.neuroforge.backend.mapper.RoleMapper;
import com.neuroforge.backend.mapper.UserMapper;
import com.neuroforge.backend.repository.RoleRepository;
import com.neuroforge.backend.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Tag(name = "Test", description = "Test endpoints to verify database connectivity")
public class TestController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper; // ← Added RoleMapper

    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Returns a list of all users from the database")
    public List<UserResponseDTO> getUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/roles")
    @Operation(summary = "Get all roles", description = "Returns a list of all roles from the database")
    public List<RoleResponseDTO> getRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toResponseDto) // ← Using RoleMapper
                .collect(Collectors.toList());
    }

    @GetMapping("/counts")
    @Operation(summary = "Get entity counts", description = "Returns counts of various entities")
    public Map<String, Long> getCounts() {
        Map<String, Long> counts = new HashMap<>();
        counts.put("users", userRepository.count());
        counts.put("roles", roleRepository.count());
        counts.put("projects", 0L);
        return counts;
    }
}