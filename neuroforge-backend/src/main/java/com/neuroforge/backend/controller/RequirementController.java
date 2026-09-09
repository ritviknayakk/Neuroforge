package com.neuroforge.backend.controller;

import com.neuroforge.backend.dto.requirement.RequirementRequestDTO;
import com.neuroforge.backend.dto.requirement.RequirementResponseDTO;
import com.neuroforge.backend.dto.requirement.RequirementStatusUpdateRequestDTO;
import com.neuroforge.backend.dto.requirement.RequirementUpdateRequestDTO;
import com.neuroforge.backend.service.RequirementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requirements")
@RequiredArgsConstructor
@Tag(name = "Requirements", description = "Requirements management endpoints")
public class RequirementController {

    private final RequirementService requirementService;

    @GetMapping
    @Operation(summary = "Get all requirements", description = "Returns a list of all requirements")
    public List<RequirementResponseDTO> getAllRequirements() {
        return requirementService.getAllRequirements();
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get requirements by project ID", description = "Returns all requirements for a specific project")
    public List<RequirementResponseDTO> getRequirementsByProjectId(@PathVariable Integer projectId) {
        return requirementService.getRequirementsByProjectId(projectId);
    }

    @GetMapping("/project/{projectId}/active")
    @Operation(summary = "Get active requirements by project ID", description = "Returns active (non-archived) requirements for a project")
    public List<RequirementResponseDTO> getActiveRequirementsByProjectId(@PathVariable Integer projectId) {
        return requirementService.getActiveRequirementsByProjectId(projectId);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get requirements by status", description = "Returns requirements with a specific status")
    public List<RequirementResponseDTO> getRequirementsByStatus(@PathVariable String status) {
        return requirementService.getRequirementsByStatus(status);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get requirement by ID", description = "Returns a single requirement by its ID")
    public RequirementResponseDTO getRequirementById(@PathVariable Integer id) {
        return requirementService.getRequirementById(id);
    }

    @GetMapping("/project/{projectId}/requirement/{requirementId}")
    @Operation(summary = "Get requirement by project and requirement ID", description = "Returns a requirement for a specific project")
    public RequirementResponseDTO getRequirementByProjectAndId(
            @PathVariable Integer projectId,
            @PathVariable Integer requirementId) {
        return requirementService.getRequirementByProjectAndId(projectId, requirementId);
    }

    @GetMapping("/search")
    @Operation(summary = "Search requirements", description = "Searches active requirements by keyword")
    public List<RequirementResponseDTO> searchRequirements(@RequestParam String keyword) {
        return requirementService.searchRequirements(keyword);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new requirement", description = "Creates a new requirement for a project")
    public RequirementResponseDTO createRequirement(@Valid @RequestBody RequirementRequestDTO request) {
        return requirementService.createRequirement(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a requirement", description = "Updates an existing requirement")
    public RequirementResponseDTO updateRequirement(
            @PathVariable Integer id,
            @Valid @RequestBody RequirementUpdateRequestDTO request) {
        return requirementService.updateRequirement(id, request);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update requirement status", description = "Updates the status of a requirement")
    public RequirementResponseDTO updateRequirementStatus(
            @PathVariable Integer id,
            @Valid @RequestBody RequirementStatusUpdateRequestDTO request) {
        return requirementService.updateRequirementStatus(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a requirement", description = "Deletes a requirement by its ID")
    public void deleteRequirement(@PathVariable Integer id) {
        requirementService.deleteRequirement(id);
    }

    @GetMapping("/project/{projectId}/count")
    @Operation(summary = "Get requirement count by project", description = "Returns the total count of requirements for a project")
    public long getRequirementCountByProject(@PathVariable Integer projectId) {
        return requirementService.getRequirementCountByProject(projectId);
    }

    @GetMapping("/project/{projectId}/count-active")
    @Operation(summary = "Get active requirement count by project", description = "Returns the count of active requirements for a project")
    public long getActiveRequirementCountByProject(@PathVariable Integer projectId) {
        return requirementService.getActiveRequirementCountByProject(projectId);
    }
}