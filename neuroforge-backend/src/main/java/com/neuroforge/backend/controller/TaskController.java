package com.neuroforge.backend.controller;

import com.neuroforge.backend.dto.task.TaskAssignmentRequestDTO;
import com.neuroforge.backend.dto.task.TaskRequestDTO;
import com.neuroforge.backend.dto.task.TaskResponseDTO;
import com.neuroforge.backend.dto.task.TaskStatusUpdateRequestDTO;
import com.neuroforge.backend.dto.task.TaskUpdateRequestDTO;
import com.neuroforge.backend.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Task management endpoints")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    @Operation(summary = "Get all tasks", description = "Returns a list of all tasks")
    public List<TaskResponseDTO> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get tasks by project ID", description = "Returns all tasks for a specific project")
    public List<TaskResponseDTO> getTasksByProjectId(@PathVariable Integer projectId) {
        return taskService.getTasksByProjectId(projectId);
    }

    @GetMapping("/project/{projectId}/active")
    @Operation(summary = "Get active tasks by project ID", description = "Returns active (non-archived) tasks for a project")
    public List<TaskResponseDTO> getActiveTasksByProjectId(@PathVariable Integer projectId) {
        return taskService.getActiveTasksByProjectId(projectId);
    }

    @GetMapping("/user-story/{userStoryId}")
    @Operation(summary = "Get tasks by user story ID", description = "Returns all tasks for a specific user story")
    public List<TaskResponseDTO> getTasksByUserStoryId(@PathVariable Integer userStoryId) {
        return taskService.getTasksByUserStoryId(userStoryId);
    }

    @GetMapping("/user-story/{userStoryId}/active")
    @Operation(summary = "Get active tasks by user story ID", description = "Returns active tasks for a user story")
    public List<TaskResponseDTO> getActiveTasksByUserStoryId(@PathVariable Integer userStoryId) {
        return taskService.getActiveTasksByUserStoryId(userStoryId);
    }

    @GetMapping("/sprint/{sprintId}")
    @Operation(summary = "Get tasks by sprint ID", description = "Returns all tasks for a specific sprint")
    public List<TaskResponseDTO> getTasksBySprintId(@PathVariable Integer sprintId) {
        return taskService.getTasksBySprintId(sprintId);
    }

    @GetMapping("/sprint/{sprintId}/active")
    @Operation(summary = "Get active tasks by sprint ID", description = "Returns active tasks for a sprint")
    public List<TaskResponseDTO> getActiveTasksBySprintId(@PathVariable Integer sprintId) {
        return taskService.getActiveTasksBySprintId(sprintId);
    }

    @GetMapping("/assigned-to/{userId}")
    @Operation(summary = "Get tasks assigned to user", description = "Returns all tasks assigned to a specific user")
    public List<TaskResponseDTO> getTasksByAssignedToUserId(@PathVariable Integer userId) {
        return taskService.getTasksByAssignedToUserId(userId);
    }

    @GetMapping("/assigned-to/{userId}/active")
    @Operation(summary = "Get active tasks assigned to user", description = "Returns active tasks assigned to a user")
    public List<TaskResponseDTO> getActiveTasksByAssignedToUserId(@PathVariable Integer userId) {
        return taskService.getActiveTasksByAssignedToUserId(userId);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get tasks by status", description = "Returns tasks with a specific status")
    public List<TaskResponseDTO> getTasksByStatus(@PathVariable String status) {
        return taskService.getTasksByStatus(status);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID", description = "Returns a single task by its ID")
    public TaskResponseDTO getTaskById(@PathVariable Integer id) {
        return taskService.getTaskById(id);
    }

    @GetMapping("/overdue")
    @Operation(summary = "Get overdue tasks", description = "Returns all overdue tasks")
    public List<TaskResponseDTO> getOverdueTasks() {
        return taskService.getOverdueTasks();
    }

    @GetMapping("/due-today")
    @Operation(summary = "Get tasks due today", description = "Returns all tasks due today")
    public List<TaskResponseDTO> getTasksDueToday() {
        return taskService.getTasksDueToday();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new task", description = "Creates a new task")
    public TaskResponseDTO createTask(@Valid @RequestBody TaskRequestDTO request) {
        return taskService.createTask(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a task", description = "Updates an existing task")
    public TaskResponseDTO updateTask(
            @PathVariable Integer id,
            @Valid @RequestBody TaskUpdateRequestDTO request) {
        return taskService.updateTask(id, request);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update task status", description = "Updates the status of a task")
    public TaskResponseDTO updateTaskStatus(
            @PathVariable Integer id,
            @Valid @RequestBody TaskStatusUpdateRequestDTO request) {
        return taskService.updateTaskStatus(id, request);
    }

    @PatchMapping("/{id}/assign")
    @Operation(summary = "Assign task to user", description = "Assigns a task to a user")
    public TaskResponseDTO assignTask(
            @PathVariable Integer id,
            @Valid @RequestBody TaskAssignmentRequestDTO request) {
        return taskService.assignTask(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a task", description = "Deletes a task by its ID")
    public void deleteTask(@PathVariable Integer id) {
        taskService.deleteTask(id);
    }

    @GetMapping("/project/{projectId}/count")
    @Operation(summary = "Get task count by project", description = "Returns the total count of tasks for a project")
    public long getTaskCountByProject(@PathVariable Integer projectId) {
        return taskService.getTaskCountByProject(projectId);
    }

    @GetMapping("/project/{projectId}/count-active")
    @Operation(summary = "Get active task count by project", description = "Returns the count of active tasks for a project")
    public long getActiveTaskCountByProject(@PathVariable Integer projectId) {
        return taskService.getActiveTaskCountByProject(projectId);
    }

    @GetMapping("/user-story/{userStoryId}/count")
    @Operation(summary = "Get task count by user story", description = "Returns the total count of tasks for a user story")
    public long getTaskCountByUserStory(@PathVariable Integer userStoryId) {
        return taskService.getTaskCountByUserStory(userStoryId);
    }

    @GetMapping("/sprint/{sprintId}/count")
    @Operation(summary = "Get task count by sprint", description = "Returns the total count of tasks for a sprint")
    public long getTaskCountBySprint(@PathVariable Integer sprintId) {
        return taskService.getTaskCountBySprint(sprintId);
    }

    @GetMapping("/assigned-to/{userId}/count")
    @Operation(summary = "Get task count assigned to user", description = "Returns the total count of tasks assigned to a user")
    public long getTaskCountByAssignedTo(@PathVariable Integer userId) {
        return taskService.getTaskCountByAssignedTo(userId);
    }
}