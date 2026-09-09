package com.neuroforge.backend.controller;

import com.neuroforge.backend.dto.testcase.TestCaseRequestDTO;
import com.neuroforge.backend.dto.testcase.TestCaseResponseDTO;
import com.neuroforge.backend.dto.testcase.TestCaseReviewRequestDTO;
import com.neuroforge.backend.dto.testcase.TestCaseStatusUpdateRequestDTO;
import com.neuroforge.backend.dto.testcase.TestCaseUpdateRequestDTO;
import com.neuroforge.backend.service.TestCaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test-cases")
@RequiredArgsConstructor
@Tag(name = "Test Cases", description = "Test case management endpoints")
public class TestCaseController {

    private final TestCaseService testCaseService;

    @GetMapping
    @Operation(summary = "Get all test cases", description = "Returns a list of all test cases")
    public List<TestCaseResponseDTO> getAllTestCases() {
        return testCaseService.getAllTestCases();
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get test cases by project ID", description = "Returns all test cases for a specific project")
    public List<TestCaseResponseDTO> getTestCasesByProjectId(@PathVariable Integer projectId) {
        return testCaseService.getTestCasesByProjectId(projectId);
    }

    @GetMapping("/project/{projectId}/active")
    @Operation(summary = "Get active test cases by project ID", description = "Returns active (non-archived) test cases for a project")
    public List<TestCaseResponseDTO> getActiveTestCasesByProjectId(@PathVariable Integer projectId) {
        return testCaseService.getActiveTestCasesByProjectId(projectId);
    }

    @GetMapping("/user-story/{userStoryId}")
    @Operation(summary = "Get test cases by user story ID", description = "Returns all test cases for a specific user story")
    public List<TestCaseResponseDTO> getTestCasesByUserStoryId(@PathVariable Integer userStoryId) {
        return testCaseService.getTestCasesByUserStoryId(userStoryId);
    }

    @GetMapping("/user-story/{userStoryId}/active")
    @Operation(summary = "Get active test cases by user story ID", description = "Returns active test cases for a user story")
    public List<TestCaseResponseDTO> getActiveTestCasesByUserStoryId(@PathVariable Integer userStoryId) {
        return testCaseService.getActiveTestCasesByUserStoryId(userStoryId);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get test cases by status", description = "Returns test cases with a specific status")
    public List<TestCaseResponseDTO> getTestCasesByStatus(@PathVariable String status) {
        return testCaseService.getTestCasesByStatus(status);
    }

    @GetMapping("/ai-generated")
    @Operation(summary = "Get AI-generated test cases", description = "Returns all AI-generated test cases")
    public List<TestCaseResponseDTO> getAIGeneratedTestCases() {
        return testCaseService.getAIGeneratedTestCases();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get test case by ID", description = "Returns a single test case by its ID")
    public TestCaseResponseDTO getTestCaseById(@PathVariable Integer id) {
        return testCaseService.getTestCaseById(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search test cases", description = "Searches active test cases by feature name")
    public List<TestCaseResponseDTO> searchTestCases(@RequestParam String keyword) {
        return testCaseService.searchTestCases(keyword);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new test case", description = "Creates a new test case")
    public TestCaseResponseDTO createTestCase(@Valid @RequestBody TestCaseRequestDTO request) {
        return testCaseService.createTestCase(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a test case", description = "Updates an existing test case")
    public TestCaseResponseDTO updateTestCase(
            @PathVariable Integer id,
            @Valid @RequestBody TestCaseUpdateRequestDTO request) {
        return testCaseService.updateTestCase(id, request);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update test case status", description = "Updates the status of a test case")
    public TestCaseResponseDTO updateTestCaseStatus(
            @PathVariable Integer id,
            @Valid @RequestBody TestCaseStatusUpdateRequestDTO request) {
        return testCaseService.updateTestCaseStatus(id, request);
    }

    @PostMapping("/{id}/review")
    @Operation(summary = "Review test case", description = "Reviews a test case")
    public TestCaseResponseDTO reviewTestCase(
            @PathVariable Integer id,
            @Valid @RequestBody TestCaseReviewRequestDTO request) {
        return testCaseService.reviewTestCase(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a test case", description = "Deletes a test case by its ID")
    public void deleteTestCase(@PathVariable Integer id) {
        testCaseService.deleteTestCase(id);
    }

    @GetMapping("/project/{projectId}/count")
    @Operation(summary = "Get test case count by project", description = "Returns the total count of test cases for a project")
    public long getTestCaseCountByProject(@PathVariable Integer projectId) {
        return testCaseService.getTestCaseCountByProject(projectId);
    }

    @GetMapping("/project/{projectId}/count-active")
    @Operation(summary = "Get active test case count by project", description = "Returns the count of active test cases for a project")
    public long getActiveTestCaseCountByProject(@PathVariable Integer projectId) {
        return testCaseService.getActiveTestCaseCountByProject(projectId);
    }

    @GetMapping("/user-story/{userStoryId}/count")
    @Operation(summary = "Get test case count by user story", description = "Returns the total count of test cases for a user story")
    public long getTestCaseCountByUserStory(@PathVariable Integer userStoryId) {
        return testCaseService.getTestCaseCountByUserStory(userStoryId);
    }
}