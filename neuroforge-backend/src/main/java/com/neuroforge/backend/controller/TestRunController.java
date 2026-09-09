package com.neuroforge.backend.controller;

import com.neuroforge.backend.dto.testrun.TestRunRequestDTO;
import com.neuroforge.backend.dto.testrun.TestRunResponseDTO;
import com.neuroforge.backend.dto.testrun.TestRunUpdateRequestDTO;
import com.neuroforge.backend.service.TestRunService;
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
@RequestMapping("/test-runs")
@RequiredArgsConstructor
@Tag(name = "Test Runs", description = "Test run execution management endpoints")
public class TestRunController {

    private final TestRunService testRunService;

    @GetMapping
    @Operation(summary = "Get all test runs", description = "Returns a list of all test runs")
    public List<TestRunResponseDTO> getAllTestRuns() {
        return testRunService.getAllTestRuns();
    }

    @GetMapping("/test-case/{testCaseId}")
    @Operation(summary = "Get test runs by test case ID", description = "Returns all test runs for a specific test case")
    public List<TestRunResponseDTO> getTestRunsByTestCaseId(@PathVariable Integer testCaseId) {
        return testRunService.getTestRunsByTestCaseId(testCaseId);
    }

    @GetMapping("/test-case/{testCaseId}/latest")
    @Operation(summary = "Get latest test runs for test case", description = "Returns the latest test runs for a test case")
    public List<TestRunResponseDTO> getLatestTestRunsForTestCase(@PathVariable Integer testCaseId) {
        return testRunService.getLatestTestRunsForTestCase(testCaseId);
    }

    @GetMapping("/executed-by/{userId}")
    @Operation(summary = "Get test runs executed by user", description = "Returns test runs executed by a specific user")
    public List<TestRunResponseDTO> getTestRunsByExecutedByUserId(@PathVariable Integer userId) {
        return testRunService.getTestRunsByExecutedByUserId(userId);
    }

    @GetMapping("/result/{result}")
    @Operation(summary = "Get test runs by result", description = "Returns test runs with a specific result (Pass/Fail/Blocked)")
    public List<TestRunResponseDTO> getTestRunsByResult(@PathVariable String result) {
        return testRunService.getTestRunsByResult(result);
    }

    @GetMapping("/execution-type/{executionType}")
    @Operation(summary = "Get test runs by execution type", description = "Returns test runs with a specific execution type (Manual/Automated)")
    public List<TestRunResponseDTO> getTestRunsByExecutionType(@PathVariable String executionType) {
        return testRunService.getTestRunsByExecutionType(executionType);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get test run by ID", description = "Returns a single test run by its ID")
    public TestRunResponseDTO getTestRunById(@PathVariable Integer id) {
        return testRunService.getTestRunById(id);
    }

    @GetMapping("/between-dates")
    @Operation(summary = "Get test runs between dates", description = "Returns test runs executed between two dates")
    public List<TestRunResponseDTO> getTestRunsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return testRunService.getTestRunsBetweenDates(startDate, endDate);
    }

    @GetMapping("/recent-failures")
    @Operation(summary = "Get recent failures", description = "Returns test runs that failed since a specific date")
    public List<TestRunResponseDTO> getRecentFailures(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since) {
        return testRunService.getRecentFailures(since);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new test run", description = "Creates a new test run execution record")
    public TestRunResponseDTO createTestRun(@Valid @RequestBody TestRunRequestDTO request) {
        return testRunService.createTestRun(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a test run", description = "Updates an existing test run")
    public TestRunResponseDTO updateTestRun(
            @PathVariable Integer id,
            @Valid @RequestBody TestRunUpdateRequestDTO request) {
        return testRunService.updateTestRun(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a test run", description = "Deletes a test run by its ID")
    public void deleteTestRun(@PathVariable Integer id) {
        testRunService.deleteTestRun(id);
    }

    @GetMapping("/test-case/{testCaseId}/count")
    @Operation(summary = "Get test run count by test case", description = "Returns the total count of test runs for a test case")
    public long getTestRunCountByTestCase(@PathVariable Integer testCaseId) {
        return testRunService.getTestRunCountByTestCase(testCaseId);
    }

    @GetMapping("/test-case/{testCaseId}/pass-count")
    @Operation(summary = "Get passed run count", description = "Returns the count of passed test runs for a test case")
    public long getPassedRunCountForTestCase(@PathVariable Integer testCaseId) {
        return testRunService.getPassedRunCountForTestCase(testCaseId);
    }

    @GetMapping("/test-case/{testCaseId}/fail-count")
    @Operation(summary = "Get failed run count", description = "Returns the count of failed test runs for a test case")
    public long getFailedRunCountForTestCase(@PathVariable Integer testCaseId) {
        return testRunService.getFailedRunCountForTestCase(testCaseId);
    }

    @GetMapping("/test-case/{testCaseId}/pass-rate")
    @Operation(summary = "Get pass rate", description = "Returns the pass rate percentage for a test case")
    public double getPassRateForTestCase(@PathVariable Integer testCaseId) {
        return testRunService.getPassRateForTestCase(testCaseId);
    }
}