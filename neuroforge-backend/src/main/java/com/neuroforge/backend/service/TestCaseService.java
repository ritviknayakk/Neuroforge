package com.neuroforge.backend.service;

import com.neuroforge.backend.dto.testcase.TestCaseRequestDTO;
import com.neuroforge.backend.dto.testcase.TestCaseResponseDTO;
import com.neuroforge.backend.dto.testcase.TestCaseReviewRequestDTO;
import com.neuroforge.backend.dto.testcase.TestCaseStatusUpdateRequestDTO;
import com.neuroforge.backend.dto.testcase.TestCaseUpdateRequestDTO;
import com.neuroforge.backend.entity.Project;
import com.neuroforge.backend.entity.TestCases;
import com.neuroforge.backend.entity.User;
import com.neuroforge.backend.entity.UserStories;
import com.neuroforge.backend.mapper.TestCaseMapper;
import com.neuroforge.backend.repository.ProjectRepository;
import com.neuroforge.backend.repository.TestCasesRepository;
import com.neuroforge.backend.repository.UserRepository;
import com.neuroforge.backend.repository.UserStoriesRepository;
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
public class TestCaseService {

    private final TestCasesRepository testCasesRepository;
    private final ProjectRepository projectRepository;
    private final UserStoriesRepository userStoriesRepository;
    private final UserRepository userRepository;
    private final TestCaseMapper testCaseMapper;

    private static final List<String> VALID_STATUSES = Arrays.asList("Draft", "Reviewed", "Approved", "Archived");

    @Transactional(readOnly = true)
    public List<TestCaseResponseDTO> getAllTestCases() {
        return testCasesRepository.findAll().stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TestCaseResponseDTO> getTestCasesByProjectId(Integer projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Project not found with id: " + projectId);
        }
        return testCasesRepository.findByProject_ProjectId(projectId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TestCaseResponseDTO> getActiveTestCasesByProjectId(Integer projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Project not found with id: " + projectId);
        }
        return testCasesRepository.findByProject_ProjectIdAndIsArchivedFalse(projectId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TestCaseResponseDTO> getTestCasesByUserStoryId(Integer userStoryId) {
        if (!userStoriesRepository.existsById(userStoryId)) {
            throw new RuntimeException("User story not found with id: " + userStoryId);
        }
        return testCasesRepository.findByUserStory_UserStoryId(userStoryId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TestCaseResponseDTO> getActiveTestCasesByUserStoryId(Integer userStoryId) {
        if (!userStoriesRepository.existsById(userStoryId)) {
            throw new RuntimeException("User story not found with id: " + userStoryId);
        }
        return testCasesRepository.findByUserStory_UserStoryIdAndIsArchivedFalse(userStoryId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TestCaseResponseDTO> getTestCasesByStatus(String status) {
        if (!VALID_STATUSES.contains(status)) {
            throw new RuntimeException("Invalid status: " + status + ". Valid statuses: " + VALID_STATUSES);
        }
        return testCasesRepository.findByStatus(status).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TestCaseResponseDTO getTestCaseById(Integer id) {
        TestCases testCase = testCasesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Test case not found with id: " + id));
        return enrichResponseDto(testCase);
    }

    @Transactional(readOnly = true)
    public List<TestCaseResponseDTO> getAIGeneratedTestCases() {
        return testCasesRepository.findByIsAIGeneratedTrue().stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TestCaseResponseDTO> searchTestCases(String keyword) {
        return testCasesRepository.searchActiveTestCases(keyword).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    public TestCaseResponseDTO createTestCase(TestCaseRequestDTO request) {
        // Check if project exists
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + request.getProjectId()));

        // Check if user story exists (if provided)
        UserStories userStory = null;
        if (request.getUserStoryId() != null) {
            userStory = userStoriesRepository.findById(request.getUserStoryId())
                    .orElseThrow(
                            () -> new RuntimeException("User story not found with id: " + request.getUserStoryId()));
        }

        // Validate status if provided
        if (request.getStatus() != null && !VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        TestCases testCase = testCaseMapper.toEntity(request);
        testCase.setProject(project);
        testCase.setUserStory(userStory);

        // Set default values if not provided
        if (testCase.getIsAIGenerated() == null) {
            testCase.setIsAIGenerated(true);
        }
        if (testCase.getStatus() == null) {
            testCase.setStatus("Draft");
        }
        if (testCase.getIsArchived() == null) {
            testCase.setIsArchived(false);
        }

        TestCases savedTestCase = testCasesRepository.save(testCase);
        return enrichResponseDto(savedTestCase);
    }

    public TestCaseResponseDTO updateTestCase(Integer id, TestCaseUpdateRequestDTO request) {
        TestCases testCase = testCasesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Test case not found with id: " + id));

        // Validate status if being updated
        if (request.getStatus() != null && !VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        testCaseMapper.updateEntityFromDto(request, testCase);
        TestCases updatedTestCase = testCasesRepository.save(testCase);
        return enrichResponseDto(updatedTestCase);
    }

    public TestCaseResponseDTO updateTestCaseStatus(Integer id, TestCaseStatusUpdateRequestDTO request) {
        TestCases testCase = testCasesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Test case not found with id: " + id));

        if (!VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        testCase.setStatus(request.getStatus());
        TestCases updatedTestCase = testCasesRepository.save(testCase);
        return enrichResponseDto(updatedTestCase);
    }

    public TestCaseResponseDTO reviewTestCase(Integer id, TestCaseReviewRequestDTO request) {
        TestCases testCase = testCasesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Test case not found with id: " + id));

        if (!VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        // Check if user exists
        User user = userRepository.findById(request.getReviewedByUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getReviewedByUserId()));

        testCase.setReviewedBy(user);
        testCase.setReviewedAt(LocalDateTime.now());
        testCase.setStatus(request.getStatus());

        TestCases reviewedTestCase = testCasesRepository.save(testCase);
        return enrichResponseDto(reviewedTestCase);
    }

    public void deleteTestCase(Integer id) {
        if (!testCasesRepository.existsById(id)) {
            throw new RuntimeException("Test case not found with id: " + id);
        }
        testCasesRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long getTestCaseCountByProject(Integer projectId) {
        return testCasesRepository.countByProject_ProjectId(projectId);
    }

    @Transactional(readOnly = true)
    public long getActiveTestCaseCountByProject(Integer projectId) {
        return testCasesRepository.countByProject_ProjectIdAndIsArchivedFalse(projectId);
    }

    @Transactional(readOnly = true)
    public long getTestCaseCountByUserStory(Integer userStoryId) {
        return testCasesRepository.countByUserStory_UserStoryId(userStoryId);
    }

    private TestCaseResponseDTO enrichResponseDto(TestCases entity) {
        TestCaseResponseDTO dto = testCaseMapper.toResponseDto(entity);

        // Set reviewed by full name
        if (entity.getReviewedBy() != null && entity.getReviewedBy().getUserId() != null) {
            userRepository.findById(entity.getReviewedBy().getUserId())
                    .ifPresent(user -> dto.setReviewedByFullName(user.getFullName()));
        }

        // Set created by full name
        if (entity.getCreatedByUserId() != null) {
            userRepository.findById(entity.getCreatedByUserId())
                    .ifPresent(user -> dto.setCreatedByFullName(user.getFullName()));
        }

        // Set modified by full name
        if (entity.getModifiedByUserId() != null) {
            userRepository.findById(entity.getModifiedByUserId())
                    .ifPresent(user -> dto.setModifiedByFullName(user.getFullName()));
        }

        return dto;
    }
}