package com.neuroforge.backend.controller;

import com.neuroforge.backend.dto.deployment.DeploymentCompleteRequestDTO;
import com.neuroforge.backend.dto.deployment.DeploymentRequestDTO;
import com.neuroforge.backend.dto.deployment.DeploymentResponseDTO;
import com.neuroforge.backend.dto.deployment.DeploymentStatusUpdateRequestDTO;
import com.neuroforge.backend.dto.deployment.DeploymentUpdateRequestDTO;
import com.neuroforge.backend.service.DeploymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/deployments")
@RequiredArgsConstructor
@Tag(name = "Deployments", description = "Deployment management endpoints")
public class DeploymentController {

    private final DeploymentService deploymentService;

    @GetMapping
    @Operation(summary = "Get all deployments", description = "Returns a list of all deployments")
    public List<DeploymentResponseDTO> getAllDeployments() {
        return deploymentService.getAllDeployments();
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get deployments by project ID", description = "Returns all deployments for a specific project")
    public List<DeploymentResponseDTO> getDeploymentsByProjectId(@PathVariable Integer projectId) {
        return deploymentService.getDeploymentsByProjectId(projectId);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get deployments by status", description = "Returns deployments with a specific status")
    public List<DeploymentResponseDTO> getDeploymentsByStatus(@PathVariable String status) {
        return deploymentService.getDeploymentsByStatus(status);
    }

    @GetMapping("/project/{projectId}/status/{status}")
    @Operation(summary = "Get deployments by project and status", description = "Returns deployments for a project with specific status")
    public List<DeploymentResponseDTO> getDeploymentsByProjectAndStatus(
            @PathVariable Integer projectId,
            @PathVariable String status) {
        return deploymentService.getDeploymentsByProjectAndStatus(projectId, status);
    }

    @GetMapping("/project/{projectId}/successful")
    @Operation(summary = "Get recent successful deployments", description = "Returns recent successful deployments for a project")
    public List<DeploymentResponseDTO> getRecentSuccessfulDeployments(@PathVariable Integer projectId) {
        return deploymentService.getRecentSuccessfulDeployments(projectId);
    }

    @GetMapping("/project/{projectId}/failed")
    @Operation(summary = "Get recent failed deployments", description = "Returns recent failed deployments for a project")
    public List<DeploymentResponseDTO> getRecentFailedDeployments(@PathVariable Integer projectId) {
        return deploymentService.getRecentFailedDeployments(projectId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get deployment by ID", description = "Returns a single deployment by its ID")
    public DeploymentResponseDTO getDeploymentById(@PathVariable Integer id) {
        return deploymentService.getDeploymentById(id);
    }

    @GetMapping("/project/{projectId}/version/{version}")
    @Operation(summary = "Get deployment by project and version", description = "Returns a deployment for a project with specific version")
    public DeploymentResponseDTO getDeploymentByProjectAndVersion(
            @PathVariable Integer projectId,
            @PathVariable String version) {
        return deploymentService.getDeploymentByProjectAndVersion(projectId, version);
    }

    @GetMapping("/between-dates")
    @Operation(summary = "Get deployments between dates", description = "Returns deployments between two dates")
    public List<DeploymentResponseDTO> getDeploymentsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return deploymentService.getDeploymentsBetweenDates(startDate, endDate);
    }

    @GetMapping("/stalled")
    @Operation(summary = "Get stalled deployments", description = "Returns deployments that have been InProgress for too long")
    public List<DeploymentResponseDTO> getStalledDeployments(@RequestParam(defaultValue = "30") int timeoutMinutes) {
        return deploymentService.getStalledDeployments(timeoutMinutes);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new deployment", description = "Creates a new deployment")
    public DeploymentResponseDTO createDeployment(@Valid @RequestBody DeploymentRequestDTO request) {
        return deploymentService.createDeployment(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a deployment", description = "Updates an existing deployment")
    public DeploymentResponseDTO updateDeployment(
            @PathVariable Integer id,
            @Valid @RequestBody DeploymentUpdateRequestDTO request) {
        return deploymentService.updateDeployment(id, request);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update deployment status", description = "Updates the status of a deployment")
    public DeploymentResponseDTO updateDeploymentStatus(
            @PathVariable Integer id,
            @Valid @RequestBody DeploymentStatusUpdateRequestDTO request) {
        return deploymentService.updateDeploymentStatus(id, request);
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "Complete deployment", description = "Completes a deployment with final status")
    public DeploymentResponseDTO completeDeployment(
            @PathVariable Integer id,
            @Valid @RequestBody DeploymentCompleteRequestDTO request) {
        return deploymentService.completeDeployment(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a deployment", description = "Deletes a deployment by its ID")
    public void deleteDeployment(@PathVariable Integer id) {
        deploymentService.deleteDeployment(id);
    }

    @GetMapping("/project/{projectId}/count")
    @Operation(summary = "Get deployment count by project", description = "Returns the total count of deployments for a project")
    public long getDeploymentCountByProject(@PathVariable Integer projectId) {
        return deploymentService.getDeploymentCountByProject(projectId);
    }

    @GetMapping("/project/{projectId}/status/{status}/count")
    @Operation(summary = "Get deployment count by project and status", description = "Returns the count of deployments for a project with specific status")
    public long getDeploymentCountByProjectAndStatus(
            @PathVariable Integer projectId,
            @PathVariable String status) {
        return deploymentService.getDeploymentCountByProjectAndStatus(projectId, status);
    }
}