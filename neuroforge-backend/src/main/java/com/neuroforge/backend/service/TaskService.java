package com.neuroforge.backend.service;

import com.neuroforge.backend.dto.task.TaskAssignmentRequestDTO;
import com.neuroforge.backend.dto.task.TaskRequestDTO;
import com.neuroforge.backend.dto.task.TaskResponseDTO;
import com.neuroforge.backend.dto.task.TaskStatusUpdateRequestDTO;
import com.neuroforge.backend.dto.task.TaskUpdateRequestDTO;
import com.neuroforge.backend.entity.Project;
import com.neuroforge.backend.entity.Sprints;
import com.neuroforge.backend.entity.Tasks;
import com.neuroforge.backend.entity.User;
import com.neuroforge.backend.entity.UserStories;
import com.neuroforge.backend.mapper.TaskMapper;
import com.neuroforge.backend.repository.ProjectRepository;
import com.neuroforge.backend.repository.SprintsRepository;
import com.neuroforge.backend.repository.TasksRepository;
import com.neuroforge.backend.repository.UserRepository;
import com.neuroforge.backend.repository.UserStoriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TasksRepository tasksRepository;
    private final ProjectRepository projectRepository;
    private final UserStoriesRepository userStoriesRepository;
    private final SprintsRepository sprintsRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    private static final List<String> VALID_STATUSES = Arrays.asList("ToDo", "InProgress", "InReview", "Done",
            "Blocked");
    private static final List<String> VALID_PRIORITIES = Arrays.asList("Low", "Medium", "High");

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getAllTasks() {
        return tasksRepository.findAll().stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getTasksByProjectId(Integer projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Project not found with id: " + projectId);
        }
        return tasksRepository.findByProject_ProjectId(projectId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getActiveTasksByProjectId(Integer projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Project not found with id: " + projectId);
        }
        return tasksRepository.findByProject_ProjectIdAndIsArchivedFalse(projectId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getTasksByUserStoryId(Integer userStoryId) {
        if (!userStoriesRepository.existsById(userStoryId)) {
            throw new RuntimeException("User story not found with id: " + userStoryId);
        }
        return tasksRepository.findByUserStory_UserStoryId(userStoryId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getActiveTasksByUserStoryId(Integer userStoryId) {
        if (!userStoriesRepository.existsById(userStoryId)) {
            throw new RuntimeException("User story not found with id: " + userStoryId);
        }
        return tasksRepository.findByUserStory_UserStoryIdAndIsArchivedFalse(userStoryId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getTasksBySprintId(Integer sprintId) {
        if (!sprintsRepository.existsById(sprintId)) {
            throw new RuntimeException("Sprint not found with id: " + sprintId);
        }
        return tasksRepository.findBySprint_SprintId(sprintId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getActiveTasksBySprintId(Integer sprintId) {
        if (!sprintsRepository.existsById(sprintId)) {
            throw new RuntimeException("Sprint not found with id: " + sprintId);
        }
        return tasksRepository.findBySprint_SprintIdAndIsArchivedFalse(sprintId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getTasksByAssignedToUserId(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        return tasksRepository.findByAssignedTo_UserId(userId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getActiveTasksByAssignedToUserId(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        return tasksRepository.findByAssignedTo_UserIdAndIsArchivedFalse(userId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getTasksByStatus(String status) {
        if (!VALID_STATUSES.contains(status)) {
            throw new RuntimeException("Invalid status: " + status + ". Valid statuses: " + VALID_STATUSES);
        }
        return tasksRepository.findByStatus(status).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskResponseDTO getTaskById(Integer id) {
        Tasks task = tasksRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        return enrichResponseDto(task);
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getOverdueTasks() {
        return tasksRepository.findOverdueTasks(LocalDate.now()).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getTasksDueToday() {
        return tasksRepository.findTasksDueToday(LocalDate.now()).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    public TaskResponseDTO createTask(TaskRequestDTO request) {
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

        // Check if sprint exists (if provided)
        Sprints sprint = null;
        if (request.getSprintId() != null) {
            sprint = sprintsRepository.findById(request.getSprintId())
                    .orElseThrow(() -> new RuntimeException("Sprint not found with id: " + request.getSprintId()));
        }

        // Check if assigned user exists (if provided)
        User assignedTo = null;
        if (request.getAssignedToUserId() != null) {
            assignedTo = userRepository.findById(request.getAssignedToUserId())
                    .orElseThrow(
                            () -> new RuntimeException("User not found with id: " + request.getAssignedToUserId()));
        }

        // Validate priority if provided
        if (request.getPriority() != null && !VALID_PRIORITIES.contains(request.getPriority())) {
            throw new RuntimeException(
                    "Invalid priority: " + request.getPriority() + ". Valid priorities: " + VALID_PRIORITIES);
        }

        // Validate status if provided
        if (request.getStatus() != null && !VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        Tasks task = taskMapper.toEntity(request);
        task.setProject(project);
        task.setUserStory(userStory);
        task.setSprint(sprint);
        task.setAssignedTo(assignedTo);

        // Set default values if not provided
        if (task.getStatus() == null) {
            task.setStatus("ToDo");
        }
        if (task.getPriority() == null) {
            task.setPriority("Medium");
        }
        if (task.getIsArchived() == null) {
            task.setIsArchived(false);
        }

        Tasks savedTask = tasksRepository.save(task);
        return enrichResponseDto(savedTask);
    }

    public TaskResponseDTO updateTask(Integer id, TaskUpdateRequestDTO request) {
        Tasks task = tasksRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        // Validate priority if being updated
        if (request.getPriority() != null && !VALID_PRIORITIES.contains(request.getPriority())) {
            throw new RuntimeException(
                    "Invalid priority: " + request.getPriority() + ". Valid priorities: " + VALID_PRIORITIES);
        }

        // Validate status if being updated
        if (request.getStatus() != null && !VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        // Check if assigned user exists (if being updated)
        if (request.getAssignedToUserId() != null) {
            User assignedTo = userRepository.findById(request.getAssignedToUserId())
                    .orElseThrow(
                            () -> new RuntimeException("User not found with id: " + request.getAssignedToUserId()));
            task.setAssignedTo(assignedTo);
        }

        taskMapper.updateEntityFromDto(request, task);
        Tasks updatedTask = tasksRepository.save(task);
        return enrichResponseDto(updatedTask);
    }

    public TaskResponseDTO updateTaskStatus(Integer id, TaskStatusUpdateRequestDTO request) {
        Tasks task = tasksRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        if (!VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        task.setStatus(request.getStatus());
        Tasks updatedTask = tasksRepository.save(task);
        return enrichResponseDto(updatedTask);
    }

    public TaskResponseDTO assignTask(Integer id, TaskAssignmentRequestDTO request) {
        Tasks task = tasksRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        User assignedTo = null;
        if (request.getAssignedToUserId() != null) {
            assignedTo = userRepository.findById(request.getAssignedToUserId())
                    .orElseThrow(
                            () -> new RuntimeException("User not found with id: " + request.getAssignedToUserId()));
        }

        task.setAssignedTo(assignedTo);
        Tasks updatedTask = tasksRepository.save(task);
        return enrichResponseDto(updatedTask);
    }

    public void deleteTask(Integer id) {
        if (!tasksRepository.existsById(id)) {
            throw new RuntimeException("Task not found with id: " + id);
        }
        tasksRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long getTaskCountByProject(Integer projectId) {
        return tasksRepository.countByProject_ProjectId(projectId);
    }

    @Transactional(readOnly = true)
    public long getActiveTaskCountByProject(Integer projectId) {
        return tasksRepository.countByProject_ProjectIdAndIsArchivedFalse(projectId);
    }

    @Transactional(readOnly = true)
    public long getTaskCountByUserStory(Integer userStoryId) {
        return tasksRepository.countByUserStory_UserStoryId(userStoryId);
    }

    @Transactional(readOnly = true)
    public long getTaskCountBySprint(Integer sprintId) {
        return tasksRepository.countBySprint_SprintId(sprintId);
    }

    @Transactional(readOnly = true)
    public long getTaskCountByAssignedTo(Integer userId) {
        return tasksRepository.countByAssignedTo_UserId(userId);
    }

    private TaskResponseDTO enrichResponseDto(Tasks entity) {
        TaskResponseDTO dto = taskMapper.toResponseDto(entity);

        // Set assigned to full name
        if (entity.getAssignedTo() != null && entity.getAssignedTo().getUserId() != null) {
            userRepository.findById(entity.getAssignedTo().getUserId())
                    .ifPresent(user -> dto.setAssignedToFullName(user.getFullName()));
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