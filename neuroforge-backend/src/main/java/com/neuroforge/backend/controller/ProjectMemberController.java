package com.neuroforge.backend.controller;

import com.neuroforge.backend.dto.projectmember.ProjectMemberRequestDTO;
import com.neuroforge.backend.dto.projectmember.ProjectMemberResponseDTO;
import com.neuroforge.backend.dto.projectmember.ProjectMemberUpdateRequestDTO;
import com.neuroforge.backend.service.ProjectMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project-members")
@RequiredArgsConstructor
@Tag(name = "Project Members", description = "Project member management endpoints")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    @GetMapping
    @Operation(summary = "Get all project members", description = "Returns a list of all project members")
    public List<ProjectMemberResponseDTO> getAllProjectMembers() {
        return projectMemberService.getAllProjectMembers();
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get members by project ID", description = "Returns all members of a specific project")
    public List<ProjectMemberResponseDTO> getMembersByProjectId(@PathVariable Integer projectId) {
        return projectMemberService.getMembersByProjectId(projectId);
    }

    @GetMapping("/project/{projectId}/active")
    @Operation(summary = "Get active members by project ID", description = "Returns only active members of a specific project")
    public List<ProjectMemberResponseDTO> getActiveMembersByProjectId(@PathVariable Integer projectId) {
        return projectMemberService.getActiveMembersByProjectId(projectId);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get memberships by user ID", description = "Returns all project memberships for a specific user")
    public List<ProjectMemberResponseDTO> getMembersByUserId(@PathVariable Integer userId) {
        return projectMemberService.getMembersByUserId(userId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project member by ID", description = "Returns a single project member by their ID")
    public ProjectMemberResponseDTO getProjectMemberById(@PathVariable Integer id) {
        return projectMemberService.getProjectMemberById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add member to project", description = "Adds a user to a project with a specific role")
    public ProjectMemberResponseDTO addMemberToProject(@Valid @RequestBody ProjectMemberRequestDTO request) {
        return projectMemberService.addMemberToProject(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update member role", description = "Updates a project member's role")
    public ProjectMemberResponseDTO updateMemberRole(
            @PathVariable Integer id,
            @Valid @RequestBody ProjectMemberUpdateRequestDTO request) {
        return projectMemberService.updateMemberRole(id, request);
    }

    @PatchMapping("/{id}/remove")
    @Operation(summary = "Remove member from project", description = "Removes a member from a project (soft delete)")
    public ProjectMemberResponseDTO removeMemberFromProject(
            @PathVariable Integer id,
            @RequestParam Integer removedByUserId) {
        return projectMemberService.removeMemberFromProject(id, removedByUserId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete project member", description = "Permanently deletes a project member record")
    public void deleteProjectMember(@PathVariable Integer id) {
        projectMemberService.deleteProjectMember(id);
    }
}