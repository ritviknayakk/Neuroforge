package com.neuroforge.backend.service;

import com.neuroforge.backend.dto.sprint.SprintRequestDTO;
import com.neuroforge.backend.dto.sprint.SprintResponseDTO;
import com.neuroforge.backend.dto.sprint.SprintStatusUpdateRequestDTO;
import com.neuroforge.backend.dto.sprint.SprintUpdateRequestDTO;
import com.neuroforge.backend.entity.Project;
import com.neuroforge.backend.entity.Sprints;
import com.neuroforge.backend.mapper.SprintMapper;
import com.neuroforge.backend.repository.ProjectRepository;
import com.neuroforge.backend.repository.SprintsRepository;
import com.neuroforge.backend.repository.UserRepository;
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
public class SprintService {

    private final SprintsRepository sprintsRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final SprintMapper sprintMapper;

    private static final List<String> VALID_STATUSES = Arrays.asList("Planned", "Active", "Completed", "Cancelled");

    @Transactional(readOnly = true)
    public List<SprintResponseDTO> getAllSprints() {
        return sprintsRepository.findAll().stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SprintResponseDTO> getSprintsByProjectId(Integer projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Project not found with id: " + projectId);
        }
        return sprintsRepository.findByProject_ProjectId(projectId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SprintResponseDTO> getSprintsByProjectAndStatus(Integer projectId, String status) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Project not found with id: " + projectId);
        }
        if (!VALID_STATUSES.contains(status)) {
            throw new RuntimeException("Invalid status: " + status + ". Valid statuses: " + VALID_STATUSES);
        }
        return sprintsRepository.findByProject_ProjectIdAndStatus(projectId, status).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SprintResponseDTO> getSprintsByStatus(String status) {
        if (!VALID_STATUSES.contains(status)) {
            throw new RuntimeException("Invalid status: " + status + ". Valid statuses: " + VALID_STATUSES);
        }
        return sprintsRepository.findByStatus(status).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SprintResponseDTO getSprintById(Integer id) {
        Sprints sprint = sprintsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sprint not found with id: " + id));
        return enrichResponseDto(sprint);
    }

    @Transactional(readOnly = true)
    public List<SprintResponseDTO> getActiveSprintsOnDate(Integer projectId, LocalDate date) {
        return sprintsRepository.findActiveSprintsOnDate(projectId, date).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    public SprintResponseDTO createSprint(SprintRequestDTO request) {
        // Check if project exists
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + request.getProjectId()));

        // Validate dates
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new RuntimeException("End date cannot be before start date");
        }

        // Check if sprint name already exists for this project
        if (sprintsRepository.existsByProject_ProjectIdAndSprintNameIgnoreCase(
                request.getProjectId(), request.getSprintName())) {
            throw new RuntimeException("Sprint with name '" + request.getSprintName() +
                    "' already exists for this project");
        }

        // Validate status if provided
        if (request.getStatus() != null && !VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        Sprints sprint = sprintMapper.toEntity(request);
        sprint.setProject(project);

        // Set default status if not provided
        if (sprint.getStatus() == null) {
            sprint.setStatus("Planned");
        }

        Sprints savedSprint = sprintsRepository.save(sprint);
        return enrichResponseDto(savedSprint);
    }

    public SprintResponseDTO updateSprint(Integer id, SprintUpdateRequestDTO request) {
        Sprints sprint = sprintsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sprint not found with id: " + id));

        // Validate dates if both are provided
        if (request.getStartDate() != null && request.getEndDate() != null) {
            if (request.getEndDate().isBefore(request.getStartDate())) {
                throw new RuntimeException("End date cannot be before start date");
            }
        } else if (request.getStartDate() != null && sprint.getEndDate() != null) {
            if (sprint.getEndDate().isBefore(request.getStartDate())) {
                throw new RuntimeException("End date cannot be before start date");
            }
        } else if (request.getEndDate() != null && sprint.getStartDate() != null) {
            if (request.getEndDate().isBefore(sprint.getStartDate())) {
                throw new RuntimeException("End date cannot be before start date");
            }
        }

        // Validate status if being updated
        if (request.getStatus() != null && !VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        // Check if sprint name is being changed and if it already exists for this
        // project
        if (request.getSprintName() != null &&
                !request.getSprintName().equalsIgnoreCase(sprint.getSprintName())) {
            if (sprintsRepository.existsByProject_ProjectIdAndSprintNameIgnoreCase(
                    sprint.getProject().getProjectId(), request.getSprintName())) {
                throw new RuntimeException("Sprint with name '" + request.getSprintName() +
                        "' already exists for this project");
            }
        }

        sprintMapper.updateEntityFromDto(request, sprint);
        Sprints updatedSprint = sprintsRepository.save(sprint);
        return enrichResponseDto(updatedSprint);
    }

    public SprintResponseDTO updateSprintStatus(Integer id, SprintStatusUpdateRequestDTO request) {
        Sprints sprint = sprintsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sprint not found with id: " + id));

        if (!VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        sprint.setStatus(request.getStatus());
        Sprints updatedSprint = sprintsRepository.save(sprint);
        return enrichResponseDto(updatedSprint);
    }

    public void deleteSprint(Integer id) {
        if (!sprintsRepository.existsById(id)) {
            throw new RuntimeException("Sprint not found with id: " + id);
        }
        sprintsRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long getSprintCountByProject(Integer projectId) {
        return sprintsRepository.countByProject_ProjectId(projectId);
    }

    @Transactional(readOnly = true)
    public long getSprintCountByProjectAndStatus(Integer projectId, String status) {
        return sprintsRepository.countByProject_ProjectIdAndStatus(projectId, status);
    }

    @Transactional(readOnly = true)
    public List<SprintResponseDTO> getOverdueSprints() {
        return sprintsRepository.findOverdueSprints(LocalDate.now()).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    private SprintResponseDTO enrichResponseDto(Sprints entity) {
        SprintResponseDTO dto = sprintMapper.toResponseDto(entity);

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