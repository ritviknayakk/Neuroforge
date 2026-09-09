package com.neuroforge.backend.controller;

import com.neuroforge.backend.dto.sprint.SprintRequestDTO;
import com.neuroforge.backend.dto.sprint.SprintResponseDTO;
import com.neuroforge.backend.dto.sprint.SprintStatusUpdateRequestDTO;
import com.neuroforge.backend.dto.sprint.SprintUpdateRequestDTO;
import com.neuroforge.backend.service.SprintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/sprints")
@RequiredArgsConstructor
@Tag(name = "Sprints", description = "Sprint management endpoints")
public class SprintController {

    private final SprintService sprintService;

    @GetMapping
    @Operation(summary = "Get all sprints", description = "Returns a list of all sprints")
    public List<SprintResponseDTO> getAllSprints() {
        return sprintService.getAllSprints();
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get sprints by project ID", description = "Returns all sprints for a specific project")
    public List<SprintResponseDTO> getSprintsByProjectId(@PathVariable Integer projectId) {
        return sprintService.getSprintsByProjectId(projectId);
    }

    @GetMapping("/project/{projectId}/status/{status}")
    @Operation(summary = "Get sprints by project and status", description = "Returns sprints for a project with specific status")
    public List<SprintResponseDTO> getSprintsByProjectAndStatus(
            @PathVariable Integer projectId,
            @PathVariable String status) {
        return sprintService.getSprintsByProjectAndStatus(projectId, status);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get sprints by status", description = "Returns all sprints with a specific status")
    public List<SprintResponseDTO> getSprintsByStatus(@PathVariable String status) {
        return sprintService.getSprintsByStatus(status);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get sprint by ID", description = "Returns a single sprint by its ID")
    public SprintResponseDTO getSprintById(@PathVariable Integer id) {
        return sprintService.getSprintById(id);
    }

    @GetMapping("/active-on-date")
    @Operation(summary = "Get active sprints on date", description = "Returns sprints active on a specific date")
    public List<SprintResponseDTO> getActiveSprintsOnDate(
            @RequestParam Integer projectId,
            @RequestParam LocalDate date) {
        return sprintService.getActiveSprintsOnDate(projectId, date);
    }

    @GetMapping("/overdue")
    @Operation(summary = "Get overdue sprints", description = "Returns all overdue sprints")
    public List<SprintResponseDTO> getOverdueSprints() {
        return sprintService.getOverdueSprints();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new sprint", description = "Creates a new sprint for a project")
    public SprintResponseDTO createSprint(@Valid @RequestBody SprintRequestDTO request) {
        return sprintService.createSprint(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a sprint", description = "Updates an existing sprint")
    public SprintResponseDTO updateSprint(
            @PathVariable Integer id,
            @Valid @RequestBody SprintUpdateRequestDTO request) {
        return sprintService.updateSprint(id, request);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update sprint status", description = "Updates the status of a sprint")
    public SprintResponseDTO updateSprintStatus(
            @PathVariable Integer id,
            @Valid @RequestBody SprintStatusUpdateRequestDTO request) {
        return sprintService.updateSprintStatus(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a sprint", description = "Deletes a sprint by its ID")
    public void deleteSprint(@PathVariable Integer id) {
        sprintService.deleteSprint(id);
    }

    @GetMapping("/project/{projectId}/count")
    @Operation(summary = "Get sprint count by project", description = "Returns the total count of sprints for a project")
    public long getSprintCountByProject(@PathVariable Integer projectId) {
        return sprintService.getSprintCountByProject(projectId);
    }

    @GetMapping("/project/{projectId}/status/{status}/count")
    @Operation(summary = "Get sprint count by project and status", description = "Returns the count of sprints for a project with specific status")
    public long getSprintCountByProjectAndStatus(
            @PathVariable Integer projectId,
            @PathVariable String status) {
        return sprintService.getSprintCountByProjectAndStatus(projectId, status);
    }
}