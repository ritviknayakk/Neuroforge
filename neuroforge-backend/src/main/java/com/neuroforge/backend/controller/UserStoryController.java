package com.neuroforge.backend.controller;

import com.neuroforge.backend.dto.userstory.UserStoryAcceptRequestDTO;
import com.neuroforge.backend.dto.userstory.UserStoryRequestDTO;
import com.neuroforge.backend.dto.userstory.UserStoryResponseDTO;
import com.neuroforge.backend.dto.userstory.UserStoryStatusUpdateRequestDTO;
import com.neuroforge.backend.dto.userstory.UserStoryUpdateRequestDTO;
import com.neuroforge.backend.service.UserStoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-stories")
@RequiredArgsConstructor
@Tag(name = "User Stories", description = "User story management endpoints")
public class UserStoryController {

    private final UserStoryService userStoryService;

    @GetMapping
    @Operation(summary = "Get all user stories", description = "Returns a list of all user stories")
    public List<UserStoryResponseDTO> getAllUserStories() {
        return userStoryService.getAllUserStories();
    }

    @GetMapping("/requirement/{requirementId}")
    @Operation(summary = "Get user stories by requirement ID", description = "Returns all user stories for a specific requirement")
    public List<UserStoryResponseDTO> getUserStoriesByRequirementId(@PathVariable Integer requirementId) {
        return userStoryService.getUserStoriesByRequirementId(requirementId);
    }

    @GetMapping("/requirement/{requirementId}/active")
    @Operation(summary = "Get active user stories by requirement ID", description = "Returns active (non-archived) user stories for a requirement")
    public List<UserStoryResponseDTO> getActiveUserStoriesByRequirementId(@PathVariable Integer requirementId) {
        return userStoryService.getActiveUserStoriesByRequirementId(requirementId);
    }

    @GetMapping("/sprint/{sprintId}")
    @Operation(summary = "Get user stories by sprint ID", description = "Returns all user stories for a specific sprint")
    public List<UserStoryResponseDTO> getUserStoriesBySprintId(@PathVariable Integer sprintId) {
        return userStoryService.getUserStoriesBySprintId(sprintId);
    }

    @GetMapping("/sprint/{sprintId}/active")
    @Operation(summary = "Get active user stories by sprint ID", description = "Returns active (non-archived) user stories for a sprint")
    public List<UserStoryResponseDTO> getActiveUserStoriesBySprintId(@PathVariable Integer sprintId) {
        return userStoryService.getActiveUserStoriesBySprintId(sprintId);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get user stories by status", description = "Returns user stories with a specific status")
    public List<UserStoryResponseDTO> getUserStoriesByStatus(@PathVariable String status) {
        return userStoryService.getUserStoriesByStatus(status);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user story by ID", description = "Returns a single user story by its ID")
    public UserStoryResponseDTO getUserStoryById(@PathVariable Integer id) {
        return userStoryService.getUserStoryById(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search user stories", description = "Searches active user stories by keyword")
    public List<UserStoryResponseDTO> searchUserStories(@RequestParam String keyword) {
        return userStoryService.searchUserStories(keyword);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new user story", description = "Creates a new user story for a requirement")
    public UserStoryResponseDTO createUserStory(@Valid @RequestBody UserStoryRequestDTO request) {
        return userStoryService.createUserStory(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user story", description = "Updates an existing user story")
    public UserStoryResponseDTO updateUserStory(
            @PathVariable Integer id,
            @Valid @RequestBody UserStoryUpdateRequestDTO request) {
        return userStoryService.updateUserStory(id, request);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update user story status", description = "Updates the status of a user story")
    public UserStoryResponseDTO updateUserStoryStatus(
            @PathVariable Integer id,
            @Valid @RequestBody UserStoryStatusUpdateRequestDTO request) {
        return userStoryService.updateUserStoryStatus(id, request);
    }

    @PostMapping("/{id}/accept")
    @Operation(summary = "Accept user story", description = "Accepts a user story")
    public UserStoryResponseDTO acceptUserStory(
            @PathVariable Integer id,
            @Valid @RequestBody UserStoryAcceptRequestDTO request) {
        return userStoryService.acceptUserStory(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a user story", description = "Deletes a user story by its ID")
    public void deleteUserStory(@PathVariable Integer id) {
        userStoryService.deleteUserStory(id);
    }

    @GetMapping("/requirement/{requirementId}/count")
    @Operation(summary = "Get user story count by requirement", description = "Returns the total count of user stories for a requirement")
    public long getUserStoryCountByRequirement(@PathVariable Integer requirementId) {
        return userStoryService.getUserStoryCountByRequirement(requirementId);
    }

    @GetMapping("/requirement/{requirementId}/count-active")
    @Operation(summary = "Get active user story count by requirement", description = "Returns the count of active user stories for a requirement")
    public long getActiveUserStoryCountByRequirement(@PathVariable Integer requirementId) {
        return userStoryService.getActiveUserStoryCountByRequirement(requirementId);
    }

    @GetMapping("/sprint/{sprintId}/count")
    @Operation(summary = "Get user story count by sprint", description = "Returns the total count of user stories for a sprint")
    public long getUserStoryCountBySprint(@PathVariable Integer sprintId) {
        return userStoryService.getUserStoryCountBySprint(sprintId);
    }
}