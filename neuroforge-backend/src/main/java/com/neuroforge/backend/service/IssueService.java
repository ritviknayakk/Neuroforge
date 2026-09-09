package com.neuroforge.backend.service;

import com.neuroforge.backend.dto.issue.IssueAssignRequestDTO;
import com.neuroforge.backend.dto.issue.IssueCloseRequestDTO;
import com.neuroforge.backend.dto.issue.IssueRequestDTO;
import com.neuroforge.backend.dto.issue.IssueResponseDTO;
import com.neuroforge.backend.dto.issue.IssueStatusUpdateRequestDTO;
import com.neuroforge.backend.dto.issue.IssueUpdateRequestDTO;
import com.neuroforge.backend.entity.Issues;
import com.neuroforge.backend.entity.Project;
import com.neuroforge.backend.entity.Tasks;
import com.neuroforge.backend.entity.TestRuns;
import com.neuroforge.backend.entity.User;
import com.neuroforge.backend.mapper.IssueMapper;
import com.neuroforge.backend.repository.IssuesRepository;
import com.neuroforge.backend.repository.ProjectRepository;
import com.neuroforge.backend.repository.TasksRepository;
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
public class IssueService {

    private final IssuesRepository issuesRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TestRunsRepository testRunsRepository;
    private final TasksRepository tasksRepository;
    private final IssueMapper issueMapper;

    private static final List<String> VALID_STATUSES = Arrays.asList("Open", "InProgress", "Closed");
    private static final List<String> VALID_SEVERITIES = Arrays.asList("Low", "Medium", "High");

    @Transactional(readOnly = true)
    public List<IssueResponseDTO> getAllIssues() {
        return issuesRepository.findAll().stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<IssueResponseDTO> getIssuesByProjectId(Integer projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Project not found with id: " + projectId);
        }
        return issuesRepository.findByProject_ProjectId(projectId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<IssueResponseDTO> getActiveIssuesByProjectId(Integer projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Project not found with id: " + projectId);
        }
        return issuesRepository.findByProject_ProjectIdAndIsArchivedFalse(projectId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<IssueResponseDTO> getIssuesByStatus(String status) {
        if (!VALID_STATUSES.contains(status)) {
            throw new RuntimeException("Invalid status: " + status + ". Valid statuses: " + VALID_STATUSES);
        }
        return issuesRepository.findByStatus(status).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<IssueResponseDTO> getIssuesBySeverity(String severity) {
        if (!VALID_SEVERITIES.contains(severity)) {
            throw new RuntimeException("Invalid severity: " + severity + ". Valid severities: " + VALID_SEVERITIES);
        }
        return issuesRepository.findBySeverity(severity).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<IssueResponseDTO> getIssuesAssignedToUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        return issuesRepository.findByAssignedTo_UserId(userId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<IssueResponseDTO> getOpenIssuesAssignedToUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        return issuesRepository.findOpenIssuesAssignedToUser(userId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public IssueResponseDTO getIssueById(Integer id) {
        Issues issue = issuesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Issue not found with id: " + id));
        return enrichResponseDto(issue);
    }

    @Transactional(readOnly = true)
    public List<IssueResponseDTO> searchIssues(String keyword) {
        return issuesRepository.searchIssues(keyword).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    public IssueResponseDTO createIssue(IssueRequestDTO request) {
        // Check if project exists
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + request.getProjectId()));

        // Check if reported by user exists
        User reportedBy = userRepository.findById(request.getReportedByUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getReportedByUserId()));

        // Validate severity
        if (!VALID_SEVERITIES.contains(request.getSeverity())) {
            throw new RuntimeException(
                    "Invalid severity: " + request.getSeverity() + ". Valid severities: " + VALID_SEVERITIES);
        }

        // Validate status if provided
        if (request.getStatus() != null && !VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        // Check if assigned to user exists (if provided)
        User assignedTo = null;
        if (request.getAssignedToUserId() != null) {
            assignedTo = userRepository.findById(request.getAssignedToUserId())
                    .orElseThrow(
                            () -> new RuntimeException("User not found with id: " + request.getAssignedToUserId()));
        }

        // Check if related test run exists (if provided)
        TestRuns relatedTestRun = null;
        if (request.getRelatedTestRunId() != null) {
            relatedTestRun = testRunsRepository.findById(request.getRelatedTestRunId())
                    .orElseThrow(
                            () -> new RuntimeException("Test run not found with id: " + request.getRelatedTestRunId()));
        }

        // Check if related task exists (if provided)
        Tasks relatedTask = null;
        if (request.getRelatedTaskId() != null) {
            relatedTask = tasksRepository.findById(request.getRelatedTaskId())
                    .orElseThrow(() -> new RuntimeException("Task not found with id: " + request.getRelatedTaskId()));
        }

        Issues issue = issueMapper.toEntity(request);
        issue.setProject(project);
        issue.setReportedBy(reportedBy);
        issue.setAssignedTo(assignedTo);
        issue.setRelatedTestRun(relatedTestRun);
        issue.setRelatedTask(relatedTask);

        // Set default values if not provided
        if (issue.getStatus() == null) {
            issue.setStatus("Open");
        }
        if (issue.getIsArchived() == null) {
            issue.setIsArchived(false);
        }

        Issues savedIssue = issuesRepository.save(issue);
        return enrichResponseDto(savedIssue);
    }

    public IssueResponseDTO updateIssue(Integer id, IssueUpdateRequestDTO request) {
        Issues issue = issuesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Issue not found with id: " + id));

        // Validate severity if being updated
        if (request.getSeverity() != null && !VALID_SEVERITIES.contains(request.getSeverity())) {
            throw new RuntimeException(
                    "Invalid severity: " + request.getSeverity() + ". Valid severities: " + VALID_SEVERITIES);
        }

        // Validate status if being updated
        if (request.getStatus() != null && !VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        // Check if assigned to user exists (if being updated)
        if (request.getAssignedToUserId() != null) {
            User assignedTo = userRepository.findById(request.getAssignedToUserId())
                    .orElseThrow(
                            () -> new RuntimeException("User not found with id: " + request.getAssignedToUserId()));
            issue.setAssignedTo(assignedTo);
        }

        // Check if related test run exists (if being updated)
        if (request.getRelatedTestRunId() != null) {
            TestRuns relatedTestRun = testRunsRepository.findById(request.getRelatedTestRunId())
                    .orElseThrow(
                            () -> new RuntimeException("Test run not found with id: " + request.getRelatedTestRunId()));
            issue.setRelatedTestRun(relatedTestRun);
        }

        // Check if related task exists (if being updated)
        if (request.getRelatedTaskId() != null) {
            Tasks relatedTask = tasksRepository.findById(request.getRelatedTaskId())
                    .orElseThrow(() -> new RuntimeException("Task not found with id: " + request.getRelatedTaskId()));
            issue.setRelatedTask(relatedTask);
        }

        issueMapper.updateEntityFromDto(request, issue);
        Issues updatedIssue = issuesRepository.save(issue);
        return enrichResponseDto(updatedIssue);
    }

    public IssueResponseDTO updateIssueStatus(Integer id, IssueStatusUpdateRequestDTO request) {
        Issues issue = issuesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Issue not found with id: " + id));

        if (!VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        issue.setStatus(request.getStatus());

        // If status is Closed, set closedAt
        if ("Closed".equals(request.getStatus())) {
            issue.setClosedAt(LocalDateTime.now());
        }

        Issues updatedIssue = issuesRepository.save(issue);
        return enrichResponseDto(updatedIssue);
    }

    public IssueResponseDTO assignIssue(Integer id, IssueAssignRequestDTO request) {
        Issues issue = issuesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Issue not found with id: " + id));

        User assignedTo = null;
        if (request.getAssignedToUserId() != null) {
            assignedTo = userRepository.findById(request.getAssignedToUserId())
                    .orElseThrow(
                            () -> new RuntimeException("User not found with id: " + request.getAssignedToUserId()));
        }

        issue.setAssignedTo(assignedTo);

        // If assigning and status is Open, change to InProgress
        if (assignedTo != null && "Open".equals(issue.getStatus())) {
            issue.setStatus("InProgress");
        }

        Issues updatedIssue = issuesRepository.save(issue);
        return enrichResponseDto(updatedIssue);
    }

    public IssueResponseDTO closeIssue(Integer id, IssueCloseRequestDTO request) {
        Issues issue = issuesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Issue not found with id: " + id));

        // Check if user exists
        User closedBy = userRepository.findById(request.getClosedByUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getClosedByUserId()));

        issue.setClosedBy(closedBy);
        issue.setClosedAt(LocalDateTime.now());
        issue.setStatus("Closed");

        Issues closedIssue = issuesRepository.save(issue);
        return enrichResponseDto(closedIssue);
    }

    public void deleteIssue(Integer id) {
        if (!issuesRepository.existsById(id)) {
            throw new RuntimeException("Issue not found with id: " + id);
        }
        issuesRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long getIssueCountByProject(Integer projectId) {
        return issuesRepository.countByProject_ProjectId(projectId);
    }

    @Transactional(readOnly = true)
    public long getIssueCountByProjectAndStatus(Integer projectId, String status) {
        return issuesRepository.countByProject_ProjectIdAndStatus(projectId, status);
    }

    private IssueResponseDTO enrichResponseDto(Issues entity) {
        IssueResponseDTO dto = issueMapper.toResponseDto(entity);

        // Set reported by full name
        if (entity.getReportedBy() != null && entity.getReportedBy().getUserId() != null) {
            userRepository.findById(entity.getReportedBy().getUserId())
                    .ifPresent(user -> dto.setReportedByFullName(user.getFullName()));
        }

        // Set assigned to full name
        if (entity.getAssignedTo() != null && entity.getAssignedTo().getUserId() != null) {
            userRepository.findById(entity.getAssignedTo().getUserId())
                    .ifPresent(user -> dto.setAssignedToFullName(user.getFullName()));
        }

        // Set closed by full name
        if (entity.getClosedBy() != null && entity.getClosedBy().getUserId() != null) {
            userRepository.findById(entity.getClosedBy().getUserId())
                    .ifPresent(user -> dto.setClosedByFullName(user.getFullName()));
        }

        // Set modified by full name
        if (entity.getModifiedByUserId() != null) {
            userRepository.findById(entity.getModifiedByUserId())
                    .ifPresent(user -> dto.setModifiedByFullName(user.getFullName()));
        }

        return dto;
    }
}