package com.neuroforge.backend.controller;

import com.neuroforge.backend.dto.issue.IssueAssignRequestDTO;
import com.neuroforge.backend.dto.issue.IssueCloseRequestDTO;
import com.neuroforge.backend.dto.issue.IssueRequestDTO;
import com.neuroforge.backend.dto.issue.IssueResponseDTO;
import com.neuroforge.backend.dto.issue.IssueStatusUpdateRequestDTO;
import com.neuroforge.backend.dto.issue.IssueUpdateRequestDTO;
import com.neuroforge.backend.service.IssueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/issues")
@RequiredArgsConstructor
@Tag(name = "Issues", description = "Issue tracking management endpoints")
public class IssueController {

    private final IssueService issueService;

    @GetMapping
    @Operation(summary = "Get all issues", description = "Returns a list of all issues")
    public List<IssueResponseDTO> getAllIssues() {
        return issueService.getAllIssues();
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get issues by project ID", description = "Returns all issues for a specific project")
    public List<IssueResponseDTO> getIssuesByProjectId(@PathVariable Integer projectId) {
        return issueService.getIssuesByProjectId(projectId);
    }

    @GetMapping("/project/{projectId}/active")
    @Operation(summary = "Get active issues by project ID", description = "Returns active (non-archived) issues for a project")
    public List<IssueResponseDTO> getActiveIssuesByProjectId(@PathVariable Integer projectId) {
        return issueService.getActiveIssuesByProjectId(projectId);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get issues by status", description = "Returns issues with a specific status")
    public List<IssueResponseDTO> getIssuesByStatus(@PathVariable String status) {
        return issueService.getIssuesByStatus(status);
    }

    @GetMapping("/severity/{severity}")
    @Operation(summary = "Get issues by severity", description = "Returns issues with a specific severity")
    public List<IssueResponseDTO> getIssuesBySeverity(@PathVariable String severity) {
        return issueService.getIssuesBySeverity(severity);
    }

    @GetMapping("/assigned-to/{userId}")
    @Operation(summary = "Get issues assigned to user", description = "Returns all issues assigned to a specific user")
    public List<IssueResponseDTO> getIssuesAssignedToUser(@PathVariable Integer userId) {
        return issueService.getIssuesAssignedToUser(userId);
    }

    @GetMapping("/assigned-to/{userId}/open")
    @Operation(summary = "Get open issues assigned to user", description = "Returns open issues assigned to a specific user")
    public List<IssueResponseDTO> getOpenIssuesAssignedToUser(@PathVariable Integer userId) {
        return issueService.getOpenIssuesAssignedToUser(userId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get issue by ID", description = "Returns a single issue by its ID")
    public IssueResponseDTO getIssueById(@PathVariable Integer id) {
        return issueService.getIssueById(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search issues", description = "Searches issues by title or description")
    public List<IssueResponseDTO> searchIssues(@RequestParam String keyword) {
        return issueService.searchIssues(keyword);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new issue", description = "Creates a new issue")
    public IssueResponseDTO createIssue(@Valid @RequestBody IssueRequestDTO request) {
        return issueService.createIssue(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an issue", description = "Updates an existing issue")
    public IssueResponseDTO updateIssue(
            @PathVariable Integer id,
            @Valid @RequestBody IssueUpdateRequestDTO request) {
        return issueService.updateIssue(id, request);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update issue status", description = "Updates the status of an issue")
    public IssueResponseDTO updateIssueStatus(
            @PathVariable Integer id,
            @Valid @RequestBody IssueStatusUpdateRequestDTO request) {
        return issueService.updateIssueStatus(id, request);
    }

    @PatchMapping("/{id}/assign")
    @Operation(summary = "Assign issue to user", description = "Assigns an issue to a user")
    public IssueResponseDTO assignIssue(
            @PathVariable Integer id,
            @Valid @RequestBody IssueAssignRequestDTO request) {
        return issueService.assignIssue(id, request);
    }

    @PostMapping("/{id}/close")
    @Operation(summary = "Close issue", description = "Closes an issue")
    public IssueResponseDTO closeIssue(
            @PathVariable Integer id,
            @Valid @RequestBody IssueCloseRequestDTO request) {
        return issueService.closeIssue(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an issue", description = "Deletes an issue by its ID")
    public void deleteIssue(@PathVariable Integer id) {
        issueService.deleteIssue(id);
    }

    @GetMapping("/project/{projectId}/count")
    @Operation(summary = "Get issue count by project", description = "Returns the total count of issues for a project")
    public long getIssueCountByProject(@PathVariable Integer projectId) {
        return issueService.getIssueCountByProject(projectId);
    }

    @GetMapping("/project/{projectId}/status/{status}/count")
    @Operation(summary = "Get issue count by project and status", description = "Returns the count of issues for a project with specific status")
    public long getIssueCountByProjectAndStatus(
            @PathVariable Integer projectId,
            @PathVariable String status) {
        return issueService.getIssueCountByProjectAndStatus(projectId, status);
    }
}