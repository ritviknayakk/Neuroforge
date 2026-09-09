package com.neuroforge.backend.controller;

import com.neuroforge.backend.dto.designsuggestion.DesignSuggestionAcceptRequestDTO;
import com.neuroforge.backend.dto.designsuggestion.DesignSuggestionRequestDTO;
import com.neuroforge.backend.dto.designsuggestion.DesignSuggestionResponseDTO;
import com.neuroforge.backend.dto.designsuggestion.DesignSuggestionStatusUpdateRequestDTO;
import com.neuroforge.backend.dto.designsuggestion.DesignSuggestionUpdateRequestDTO;
import com.neuroforge.backend.service.DesignSuggestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/design-suggestions")
@RequiredArgsConstructor
@Tag(name = "Design Suggestions", description = "Design suggestion management endpoints")
public class DesignSuggestionController {

    private final DesignSuggestionService designSuggestionService;

    @GetMapping
    @Operation(summary = "Get all design suggestions", description = "Returns a list of all design suggestions")
    public List<DesignSuggestionResponseDTO> getAllDesignSuggestions() {
        return designSuggestionService.getAllDesignSuggestions();
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get design suggestions by project ID", description = "Returns all design suggestions for a specific project")
    public List<DesignSuggestionResponseDTO> getDesignSuggestionsByProjectId(@PathVariable Integer projectId) {
        return designSuggestionService.getDesignSuggestionsByProjectId(projectId);
    }

    @GetMapping("/requirement/{requirementId}")
    @Operation(summary = "Get design suggestions by requirement ID", description = "Returns all design suggestions for a specific requirement")
    public List<DesignSuggestionResponseDTO> getDesignSuggestionsByRequirementId(@PathVariable Integer requirementId) {
        return designSuggestionService.getDesignSuggestionsByRequirementId(requirementId);
    }

    @GetMapping("/project/{projectId}/status/{status}")
    @Operation(summary = "Get design suggestions by project and status", description = "Returns design suggestions for a project with specific status")
    public List<DesignSuggestionResponseDTO> getDesignSuggestionsByProjectAndStatus(
            @PathVariable Integer projectId,
            @PathVariable String status) {
        return designSuggestionService.getDesignSuggestionsByProjectAndStatus(projectId, status);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get design suggestions by status", description = "Returns design suggestions with a specific status")
    public List<DesignSuggestionResponseDTO> getDesignSuggestionsByStatus(@PathVariable String status) {
        return designSuggestionService.getDesignSuggestionsByStatus(status);
    }

    @GetMapping("/ai-generated")
    @Operation(summary = "Get AI-generated design suggestions", description = "Returns all AI-generated design suggestions")
    public List<DesignSuggestionResponseDTO> getAIGeneratedSuggestions() {
        return designSuggestionService.getAIGeneratedSuggestions();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get design suggestion by ID", description = "Returns a single design suggestion by its ID")
    public DesignSuggestionResponseDTO getDesignSuggestionById(@PathVariable Integer id) {
        return designSuggestionService.getDesignSuggestionById(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search design suggestions", description = "Searches design suggestions by components description")
    public List<DesignSuggestionResponseDTO> searchByComponentsDescription(@RequestParam String keyword) {
        return designSuggestionService.searchByComponentsDescription(keyword);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new design suggestion", description = "Creates a new design suggestion")
    public DesignSuggestionResponseDTO createDesignSuggestion(@Valid @RequestBody DesignSuggestionRequestDTO request) {
        return designSuggestionService.createDesignSuggestion(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a design suggestion", description = "Updates an existing design suggestion")
    public DesignSuggestionResponseDTO updateDesignSuggestion(
            @PathVariable Integer id,
            @Valid @RequestBody DesignSuggestionUpdateRequestDTO request) {
        return designSuggestionService.updateDesignSuggestion(id, request);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update design suggestion status", description = "Updates the status of a design suggestion")
    public DesignSuggestionResponseDTO updateSuggestionStatus(
            @PathVariable Integer id,
            @Valid @RequestBody DesignSuggestionStatusUpdateRequestDTO request) {
        return designSuggestionService.updateSuggestionStatus(id, request);
    }

    @PostMapping("/{id}/accept")
    @Operation(summary = "Accept design suggestion", description = "Accepts a design suggestion")
    public DesignSuggestionResponseDTO acceptDesignSuggestion(
            @PathVariable Integer id,
            @Valid @RequestBody DesignSuggestionAcceptRequestDTO request) {
        return designSuggestionService.acceptDesignSuggestion(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a design suggestion", description = "Deletes a design suggestion by its ID")
    public void deleteDesignSuggestion(@PathVariable Integer id) {
        designSuggestionService.deleteDesignSuggestion(id);
    }

    @GetMapping("/project/{projectId}/count")
    @Operation(summary = "Get design suggestion count by project", description = "Returns the total count of design suggestions for a project")
    public long getDesignSuggestionCountByProject(@PathVariable Integer projectId) {
        return designSuggestionService.getDesignSuggestionCountByProject(projectId);
    }

    @GetMapping("/project/{projectId}/status/{status}/count")
    @Operation(summary = "Get design suggestion count by project and status", description = "Returns the count of design suggestions for a project with specific status")
    public long getDesignSuggestionCountByProjectAndStatus(
            @PathVariable Integer projectId,
            @PathVariable String status) {
        return designSuggestionService.getDesignSuggestionCountByProjectAndStatus(projectId, status);
    }
}