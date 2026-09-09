package com.neuroforge.backend.controller;

import com.neuroforge.backend.dto.codesuggestion.CodeSuggestionDecisionRequestDTO;
import com.neuroforge.backend.dto.codesuggestion.CodeSuggestionRequestDTO;
import com.neuroforge.backend.dto.codesuggestion.CodeSuggestionResponseDTO;
import com.neuroforge.backend.dto.codesuggestion.CodeSuggestionStatusUpdateRequestDTO;
import com.neuroforge.backend.dto.codesuggestion.CodeSuggestionUpdateRequestDTO;
import com.neuroforge.backend.service.CodeSuggestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/code-suggestions")
@RequiredArgsConstructor
@Tag(name = "Code Suggestions", description = "Code suggestion management endpoints")
public class CodeSuggestionController {

    private final CodeSuggestionService codeSuggestionService;

    @GetMapping
    @Operation(summary = "Get all code suggestions", description = "Returns a list of all code suggestions")
    public List<CodeSuggestionResponseDTO> getAllCodeSuggestions() {
        return codeSuggestionService.getAllCodeSuggestions();
    }

    @GetMapping("/task/{taskId}")
    @Operation(summary = "Get code suggestions by task ID", description = "Returns all code suggestions for a specific task")
    public List<CodeSuggestionResponseDTO> getCodeSuggestionsByTaskId(@PathVariable Integer taskId) {
        return codeSuggestionService.getCodeSuggestionsByTaskId(taskId);
    }

    @GetMapping("/task/{taskId}/status/{status}")
    @Operation(summary = "Get code suggestions by task and status", description = "Returns code suggestions for a task with specific status")
    public List<CodeSuggestionResponseDTO> getCodeSuggestionsByTaskAndStatus(
            @PathVariable Integer taskId,
            @PathVariable String status) {
        return codeSuggestionService.getCodeSuggestionsByTaskAndStatus(taskId, status);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get code suggestions by status", description = "Returns code suggestions with a specific status")
    public List<CodeSuggestionResponseDTO> getCodeSuggestionsByStatus(@PathVariable String status) {
        return codeSuggestionService.getCodeSuggestionsByStatus(status);
    }

    @GetMapping("/ai-generated")
    @Operation(summary = "Get AI-generated code suggestions", description = "Returns all AI-generated code suggestions")
    public List<CodeSuggestionResponseDTO> getAIGeneratedSuggestions() {
        return codeSuggestionService.getAIGeneratedSuggestions();
    }

    @GetMapping("/language/{language}")
    @Operation(summary = "Get code suggestions by language", description = "Returns code suggestions for a specific language")
    public List<CodeSuggestionResponseDTO> getSuggestionsByLanguage(@PathVariable String language) {
        return codeSuggestionService.getSuggestionsByLanguage(language);
    }

    @GetMapping("/task/{taskId}/accepted")
    @Operation(summary = "Get accepted code suggestion for task", description = "Returns the accepted code suggestion for a task")
    public CodeSuggestionResponseDTO getAcceptedSuggestionForTask(@PathVariable Integer taskId) {
        return codeSuggestionService.getAcceptedSuggestionForTask(taskId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get code suggestion by ID", description = "Returns a single code suggestion by its ID")
    public CodeSuggestionResponseDTO getCodeSuggestionById(@PathVariable Integer id) {
        return codeSuggestionService.getCodeSuggestionById(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search code suggestions", description = "Searches code suggestions by code content")
    public List<CodeSuggestionResponseDTO> searchByCode(@RequestParam String keyword) {
        return codeSuggestionService.searchByCode(keyword);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new code suggestion", description = "Creates a new code suggestion for a task")
    public CodeSuggestionResponseDTO createCodeSuggestion(@Valid @RequestBody CodeSuggestionRequestDTO request) {
        return codeSuggestionService.createCodeSuggestion(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a code suggestion", description = "Updates an existing code suggestion")
    public CodeSuggestionResponseDTO updateCodeSuggestion(
            @PathVariable Integer id,
            @Valid @RequestBody CodeSuggestionUpdateRequestDTO request) {
        return codeSuggestionService.updateCodeSuggestion(id, request);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update code suggestion status", description = "Updates the status of a code suggestion")
    public CodeSuggestionResponseDTO updateSuggestionStatus(
            @PathVariable Integer id,
            @Valid @RequestBody CodeSuggestionStatusUpdateRequestDTO request) {
        return codeSuggestionService.updateSuggestionStatus(id, request);
    }

    @PostMapping("/{id}/decide")
    @Operation(summary = "Decide on code suggestion", description = "Accepts, dismisses, or marks as edited a code suggestion")
    public CodeSuggestionResponseDTO decideSuggestion(
            @PathVariable Integer id,
            @Valid @RequestBody CodeSuggestionDecisionRequestDTO request) {
        return codeSuggestionService.decideSuggestion(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a code suggestion", description = "Deletes a code suggestion by its ID")
    public void deleteCodeSuggestion(@PathVariable Integer id) {
        codeSuggestionService.deleteCodeSuggestion(id);
    }

    @GetMapping("/task/{taskId}/count")
    @Operation(summary = "Get code suggestion count by task", description = "Returns the total count of code suggestions for a task")
    public long getCodeSuggestionCountByTask(@PathVariable Integer taskId) {
        return codeSuggestionService.getCodeSuggestionCountByTask(taskId);
    }

    @GetMapping("/task/{taskId}/status/{status}/count")
    @Operation(summary = "Get code suggestion count by task and status", description = "Returns the count of code suggestions for a task with specific status")
    public long getCodeSuggestionCountByTaskAndStatus(
            @PathVariable Integer taskId,
            @PathVariable String status) {
        return codeSuggestionService.getCodeSuggestionCountByTaskAndStatus(taskId, status);
    }
}