package com.neuroforge.backend.service;

import com.neuroforge.backend.dto.userstory.UserStoryAcceptRequestDTO;
import com.neuroforge.backend.dto.userstory.UserStoryRequestDTO;
import com.neuroforge.backend.dto.userstory.UserStoryResponseDTO;
import com.neuroforge.backend.dto.userstory.UserStoryStatusUpdateRequestDTO;
import com.neuroforge.backend.dto.userstory.UserStoryUpdateRequestDTO;
import com.neuroforge.backend.entity.Requirements;
import com.neuroforge.backend.entity.Sprints;
import com.neuroforge.backend.entity.User;
import com.neuroforge.backend.entity.UserStories;
import com.neuroforge.backend.mapper.UserStoryMapper;
import com.neuroforge.backend.repository.RequirementsRepository;
import com.neuroforge.backend.repository.SprintsRepository;
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
public class UserStoryService {

    private final UserStoriesRepository userStoriesRepository;
    private final RequirementsRepository requirementsRepository;
    private final SprintsRepository sprintsRepository;
    private final UserRepository userRepository;
    private final UserStoryMapper userStoryMapper;

    private static final List<String> VALID_STATUSES = Arrays.asList("Draft", "Backlog", "InSprint", "Accepted",
            "Rejected");
    private static final List<String> VALID_PRIORITIES = Arrays.asList("Low", "Medium", "High");

    @Transactional(readOnly = true)
    public List<UserStoryResponseDTO> getAllUserStories() {
        return userStoriesRepository.findAll().stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserStoryResponseDTO> getUserStoriesByRequirementId(Integer requirementId) {
        if (!requirementsRepository.existsById(requirementId)) {
            throw new RuntimeException("Requirement not found with id: " + requirementId);
        }
        return userStoriesRepository.findByRequirement_RequirementId(requirementId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserStoryResponseDTO> getActiveUserStoriesByRequirementId(Integer requirementId) {
        if (!requirementsRepository.existsById(requirementId)) {
            throw new RuntimeException("Requirement not found with id: " + requirementId);
        }
        return userStoriesRepository.findByRequirement_RequirementIdAndIsArchivedFalse(requirementId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserStoryResponseDTO> getUserStoriesBySprintId(Integer sprintId) {
        if (!sprintsRepository.existsById(sprintId)) {
            throw new RuntimeException("Sprint not found with id: " + sprintId);
        }
        return userStoriesRepository.findBySprint_SprintId(sprintId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserStoryResponseDTO> getActiveUserStoriesBySprintId(Integer sprintId) {
        if (!sprintsRepository.existsById(sprintId)) {
            throw new RuntimeException("Sprint not found with id: " + sprintId);
        }
        return userStoriesRepository.findBySprint_SprintIdAndIsArchivedFalse(sprintId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserStoryResponseDTO> getUserStoriesByStatus(String status) {
        if (!VALID_STATUSES.contains(status)) {
            throw new RuntimeException("Invalid status: " + status + ". Valid statuses: " + VALID_STATUSES);
        }
        return userStoriesRepository.findByStatus(status).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserStoryResponseDTO getUserStoryById(Integer id) {
        UserStories userStory = userStoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User story not found with id: " + id));
        return enrichResponseDto(userStory);
    }

    public UserStoryResponseDTO createUserStory(UserStoryRequestDTO request) {
        // Check if requirement exists
        Requirements requirement = requirementsRepository.findById(request.getRequirementId())
                .orElseThrow(
                        () -> new RuntimeException("Requirement not found with id: " + request.getRequirementId()));

        // Check if sprint exists (if provided)
        Sprints sprint = null;
        if (request.getSprintId() != null) {
            sprint = sprintsRepository.findById(request.getSprintId())
                    .orElseThrow(() -> new RuntimeException("Sprint not found with id: " + request.getSprintId()));
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

        UserStories userStory = userStoryMapper.toEntity(request);
        userStory.setRequirement(requirement);
        userStory.setSprint(sprint);

        // Set default values if not provided
        if (userStory.getPriority() == null) {
            userStory.setPriority("Medium");
        }
        if (userStory.getStatus() == null) {
            userStory.setStatus("Draft");
        }
        if (userStory.getIsAIGenerated() == null) {
            userStory.setIsAIGenerated(false);
        }
        if (userStory.getIsArchived() == null) {
            userStory.setIsArchived(false);
        }

        UserStories savedUserStory = userStoriesRepository.save(userStory);
        return enrichResponseDto(savedUserStory);
    }

    public UserStoryResponseDTO updateUserStory(Integer id, UserStoryUpdateRequestDTO request) {
        UserStories userStory = userStoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User story not found with id: " + id));

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

        // Check if sprint exists (if being updated)
        if (request.getSprintId() != null) {
            Sprints sprint = sprintsRepository.findById(request.getSprintId())
                    .orElseThrow(() -> new RuntimeException("Sprint not found with id: " + request.getSprintId()));
            userStory.setSprint(sprint);
        }

        userStoryMapper.updateEntityFromDto(request, userStory);
        UserStories updatedUserStory = userStoriesRepository.save(userStory);
        return enrichResponseDto(updatedUserStory);
    }

    public UserStoryResponseDTO updateUserStoryStatus(Integer id, UserStoryStatusUpdateRequestDTO request) {
        UserStories userStory = userStoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User story not found with id: " + id));

        if (!VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        userStory.setStatus(request.getStatus());
        UserStories updatedUserStory = userStoriesRepository.save(userStory);
        return enrichResponseDto(updatedUserStory);
    }

    public UserStoryResponseDTO acceptUserStory(Integer id, UserStoryAcceptRequestDTO request) {
        UserStories userStory = userStoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User story not found with id: " + id));

        // Check if user exists
        User user = userRepository.findById(request.getAcceptedByUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getAcceptedByUserId()));

        userStory.setAcceptedBy(user);
        userStory.setAcceptedAt(LocalDateTime.now());
        userStory.setStatus("Accepted");

        UserStories acceptedUserStory = userStoriesRepository.save(userStory);
        return enrichResponseDto(acceptedUserStory);
    }

    public void deleteUserStory(Integer id) {
        if (!userStoriesRepository.existsById(id)) {
            throw new RuntimeException("User story not found with id: " + id);
        }
        userStoriesRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long getUserStoryCountByRequirement(Integer requirementId) {
        return userStoriesRepository.countByRequirement_RequirementId(requirementId);
    }

    @Transactional(readOnly = true)
    public long getActiveUserStoryCountByRequirement(Integer requirementId) {
        return userStoriesRepository.countByRequirement_RequirementIdAndIsArchivedFalse(requirementId);
    }

    @Transactional(readOnly = true)
    public long getUserStoryCountBySprint(Integer sprintId) {
        return userStoriesRepository.countBySprint_SprintId(sprintId);
    }

    @Transactional(readOnly = true)
    public List<UserStoryResponseDTO> searchUserStories(String keyword) {
        return userStoriesRepository.searchActiveUserStories(keyword).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    private UserStoryResponseDTO enrichResponseDto(UserStories entity) {
        UserStoryResponseDTO dto = userStoryMapper.toResponseDto(entity);

        // Set accepted by full name
        if (entity.getAcceptedBy() != null && entity.getAcceptedBy().getUserId() != null) {
            userRepository.findById(entity.getAcceptedBy().getUserId())
                    .ifPresent(user -> dto.setAcceptedByFullName(user.getFullName()));
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