package com.neuroforge.backend.service;

import com.neuroforge.backend.dto.testrun.TestRunRequestDTO;
import com.neuroforge.backend.dto.testrun.TestRunResponseDTO;
import com.neuroforge.backend.dto.testrun.TestRunUpdateRequestDTO;
import com.neuroforge.backend.entity.TestCases;
import com.neuroforge.backend.entity.TestRuns;
import com.neuroforge.backend.entity.User;
import com.neuroforge.backend.mapper.TestRunMapper;
import com.neuroforge.backend.repository.TestCasesRepository;
import com.neuroforge.backend.repository.TestRunsRepository;
import com.neuroforge.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TestRunService {

    private final TestRunsRepository testRunsRepository;
    private final TestCasesRepository testCasesRepository;
    private final UserRepository userRepository;
    private final TestRunMapper testRunMapper;

    private static final List<String> VALID_EXECUTION_TYPES = Arrays.asList("Manual", "Automated");
    private static final List<String> VALID_RESULTS = Arrays.asList("Pass", "Fail", "Blocked");

    @Transactional(readOnly = true)
    public List<TestRunResponseDTO> getAllTestRuns() {
        return testRunsRepository.findAll().stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TestRunResponseDTO> getTestRunsByTestCaseId(Integer testCaseId) {
        if (!testCasesRepository.existsById(testCaseId)) {
            throw new RuntimeException("Test case not found with id: " + testCaseId);
        }
        return testRunsRepository.findByTestCase_TestCaseId(testCaseId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TestRunResponseDTO> getLatestTestRunsForTestCase(Integer testCaseId) {
        if (!testCasesRepository.existsById(testCaseId)) {
            throw new RuntimeException("Test case not found with id: " + testCaseId);
        }
        return testRunsRepository.findLatestTestRunsForTestCase(testCaseId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TestRunResponseDTO> getTestRunsByExecutedByUserId(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        return testRunsRepository.findByExecutedBy_UserId(userId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TestRunResponseDTO> getTestRunsByResult(String result) {
        if (!VALID_RESULTS.contains(result)) {
            throw new RuntimeException("Invalid result: " + result + ". Valid results: " + VALID_RESULTS);
        }
        return testRunsRepository.findByResult(result).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TestRunResponseDTO> getTestRunsByExecutionType(String executionType) {
        if (!VALID_EXECUTION_TYPES.contains(executionType)) {
            throw new RuntimeException(
                    "Invalid execution type: " + executionType + ". Valid types: " + VALID_EXECUTION_TYPES);
        }
        return testRunsRepository.findByExecutionType(executionType).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TestRunResponseDTO getTestRunById(Integer id) {
        TestRuns testRun = testRunsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Test run not found with id: " + id));
        return enrichResponseDto(testRun);
    }

    @Transactional(readOnly = true)
    public List<TestRunResponseDTO> getTestRunsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return testRunsRepository.findTestRunsBetweenDates(startDate, endDate).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TestRunResponseDTO> getRecentFailures(LocalDateTime since) {
        return testRunsRepository.findRecentFailures(since).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    public TestRunResponseDTO createTestRun(TestRunRequestDTO request) {
        // Check if test case exists
        TestCases testCase = testCasesRepository.findById(request.getTestCaseId())
                .orElseThrow(() -> new RuntimeException("Test case not found with id: " + request.getTestCaseId()));

        // Validate execution type
        if (!VALID_EXECUTION_TYPES.contains(request.getExecutionType())) {
            throw new RuntimeException("Invalid execution type: " + request.getExecutionType() +
                    ". Valid types: " + VALID_EXECUTION_TYPES);
        }

        // Validate result
        if (!VALID_RESULTS.contains(request.getResult())) {
            throw new RuntimeException("Invalid result: " + request.getResult() +
                    ". Valid results: " + VALID_RESULTS);
        }

        // Check if executed by user exists (if provided)
        User executedBy = null;
        if (request.getExecutedByUserId() != null) {
            executedBy = userRepository.findById(request.getExecutedByUserId())
                    .orElseThrow(
                            () -> new RuntimeException("User not found with id: " + request.getExecutedByUserId()));
        }

        TestRuns testRun = testRunMapper.toEntity(request);
        testRun.setTestCase(testCase);
        testRun.setExecutedBy(executedBy);

        TestRuns savedTestRun = testRunsRepository.save(testRun);
        return enrichResponseDto(savedTestRun);
    }

    public TestRunResponseDTO updateTestRun(Integer id, TestRunUpdateRequestDTO request) {
        TestRuns testRun = testRunsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Test run not found with id: " + id));

        // Validate execution type if being updated
        if (request.getExecutionType() != null && !VALID_EXECUTION_TYPES.contains(request.getExecutionType())) {
            throw new RuntimeException("Invalid execution type: " + request.getExecutionType() +
                    ". Valid types: " + VALID_EXECUTION_TYPES);
        }

        // Validate result if being updated
        if (request.getResult() != null && !VALID_RESULTS.contains(request.getResult())) {
            throw new RuntimeException("Invalid result: " + request.getResult() +
                    ". Valid results: " + VALID_RESULTS);
        }

        // Check if executed by user exists (if being updated)
        if (request.getExecutedByUserId() != null) {
            User executedBy = userRepository.findById(request.getExecutedByUserId())
                    .orElseThrow(
                            () -> new RuntimeException("User not found with id: " + request.getExecutedByUserId()));
            testRun.setExecutedBy(executedBy);
        }

        testRunMapper.updateEntityFromDto(request, testRun);
        TestRuns updatedTestRun = testRunsRepository.save(testRun);
        return enrichResponseDto(updatedTestRun);
    }

    public void deleteTestRun(Integer id) {
        if (!testRunsRepository.existsById(id)) {
            throw new RuntimeException("Test run not found with id: " + id);
        }
        testRunsRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long getTestRunCountByTestCase(Integer testCaseId) {
        return testRunsRepository.countByTestCase_TestCaseId(testCaseId);
    }

    @Transactional(readOnly = true)
    public long getPassedRunCountForTestCase(Integer testCaseId) {
        return testRunsRepository.countPassedRunsForTestCase(testCaseId);
    }

    @Transactional(readOnly = true)
    public long getFailedRunCountForTestCase(Integer testCaseId) {
        return testRunsRepository.countFailedRunsForTestCase(testCaseId);
    }

    @Transactional(readOnly = true)
    public double getPassRateForTestCase(Integer testCaseId) {
        long total = testRunsRepository.countByTestCase_TestCaseId(testCaseId);
        if (total == 0) {
            return 0.0;
        }
        long passed = testRunsRepository.countPassedRunsForTestCase(testCaseId);
        return (double) passed / total * 100;
    }

    private TestRunResponseDTO enrichResponseDto(TestRuns entity) {
        TestRunResponseDTO dto = testRunMapper.toResponseDto(entity);

        // Set executed by full name
        if (entity.getExecutedBy() != null && entity.getExecutedBy().getUserId() != null) {
            userRepository.findById(entity.getExecutedBy().getUserId())
                    .ifPresent(user -> dto.setExecutedByFullName(user.getFullName()));
        }

        return dto;
    }
}