package com.neuroforge.backend.controller;

import com.neuroforge.backend.dto.project.ProjectRequestDTO;
import com.neuroforge.backend.dto.project.ProjectResponseDTO;
import com.neuroforge.backend.dto.project.ProjectUpdateRequestDTO;
import com.neuroforge.backend.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@Tag(name = "Projects", description = "Project management endpoints")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    @Operation(summary = "Get all projects", description = "Returns a list of all projects")
    public List<ProjectResponseDTO> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get projects by status", description = "Returns projects filtered by status (Active/Archived)")
    public List<ProjectResponseDTO> getProjectsByStatus(@PathVariable String status) {
        return projectService.getProjectsByStatus(status);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID", description = "Returns a single project by its ID")
    public ProjectResponseDTO getProjectById(@PathVariable Integer id) {
        return projectService.getProjectById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new project", description = "Creates a new project with the provided details")
    public ProjectResponseDTO createProject(@Valid @RequestBody ProjectRequestDTO request) {
        return projectService.createProject(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing project", description = "Updates an existing project's details")
    public ProjectResponseDTO updateProject(@PathVariable Integer id,
            @Valid @RequestBody ProjectUpdateRequestDTO request) {
        return projectService.updateProject(id, request);
    }

    @PatchMapping("/{id}/archive")
    @Operation(summary = "Archive a project", description = "Archives a project by setting its status to Archived")
    public ProjectResponseDTO archiveProject(@PathVariable Integer id) {
        return projectService.archiveProject(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a project", description = "Deletes a project by its ID")
    public void deleteProject(@PathVariable Integer id) {
        projectService.deleteProject(id);
    }
}