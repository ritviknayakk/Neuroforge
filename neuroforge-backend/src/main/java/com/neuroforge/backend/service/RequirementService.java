package com.neuroforge.backend.service;

import com.neuroforge.backend.dto.requirement.RequirementRequestDTO;
import com.neuroforge.backend.dto.requirement.RequirementResponseDTO;
import com.neuroforge.backend.dto.requirement.RequirementStatusUpdateRequestDTO;
import com.neuroforge.backend.dto.requirement.RequirementUpdateRequestDTO;
import com.neuroforge.backend.entity.Project;
import com.neuroforge.backend.entity.Requirements;
import com.neuroforge.backend.mapper.RequirementMapper;
import com.neuroforge.backend.repository.ProjectRepository;
import com.neuroforge.backend.repository.RequirementsRepository;
import com.neuroforge.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RequirementService {

    private final RequirementsRepository requirementsRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final RequirementMapper requirementMapper;

    private static final List<String> VALID_STATUSES = Arrays.asList("Draft", "UnderReview", "Approved", "Archived");

    @Transactional(readOnly = true)
    public List<RequirementResponseDTO> getAllRequirements() {
        return requirementsRepository.findAll().stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RequirementResponseDTO> getRequirementsByProjectId(Integer projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Project not found with id: " + projectId);
        }
        return requirementsRepository.findByProject_ProjectId(projectId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RequirementResponseDTO> getActiveRequirementsByProjectId(Integer projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Project not found with id: " + projectId);
        }
        return requirementsRepository.findByProject_ProjectIdAndIsArchivedFalse(projectId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RequirementResponseDTO> getRequirementsByStatus(String status) {
        if (!VALID_STATUSES.contains(status)) {
            throw new RuntimeException("Invalid status: " + status + ". Valid statuses: " + VALID_STATUSES);
        }
        return requirementsRepository.findByStatus(status).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RequirementResponseDTO getRequirementById(Integer id) {
        Requirements requirement = requirementsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Requirement not found with id: " + id));
        return enrichResponseDto(requirement);
    }

    @Transactional(readOnly = true)
    public RequirementResponseDTO getRequirementByProjectAndId(Integer projectId, Integer requirementId) {
        Requirements requirement = requirementsRepository
                .findByProject_ProjectIdAndRequirementId(projectId, requirementId)
                .orElseThrow(() -> new RuntimeException(
                        "Requirement not found for project " + projectId + " with id: " + requirementId));
        return enrichResponseDto(requirement);
    }

    public RequirementResponseDTO createRequirement(RequirementRequestDTO request) {
        // Check if project exists
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + request.getProjectId()));

        // Validate status if provided
        if (request.getStatus() != null && !VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        Requirements requirement = requirementMapper.toEntity(request);
        requirement.setProject(project);

        // Set default values if not provided
        if (requirement.getStatus() == null) {
            requirement.setStatus("Draft");
        }
        if (requirement.getIsArchived() == null) {
            requirement.setIsArchived(false);
        }

        Requirements savedRequirement = requirementsRepository.save(requirement);
        return enrichResponseDto(savedRequirement);
    }

    public RequirementResponseDTO updateRequirement(Integer id, RequirementUpdateRequestDTO request) {
        Requirements requirement = requirementsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Requirement not found with id: " + id));

        // Validate status if being updated
        if (request.getStatus() != null && !VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        requirementMapper.updateEntityFromDto(request, requirement);
        Requirements updatedRequirement = requirementsRepository.save(requirement);
        return enrichResponseDto(updatedRequirement);
    }

    public RequirementResponseDTO updateRequirementStatus(Integer id, RequirementStatusUpdateRequestDTO request) {
        Requirements requirement = requirementsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Requirement not found with id: " + id));

        if (!VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        requirement.setStatus(request.getStatus());
        Requirements updatedRequirement = requirementsRepository.save(requirement);
        return enrichResponseDto(updatedRequirement);
    }

    public void deleteRequirement(Integer id) {
        if (!requirementsRepository.existsById(id)) {
            throw new RuntimeException("Requirement not found with id: " + id);
        }
        requirementsRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long getRequirementCountByProject(Integer projectId) {
        return requirementsRepository.countByProject_ProjectId(projectId);
    }

    @Transactional(readOnly = true)
    public long getActiveRequirementCountByProject(Integer projectId) {
        return requirementsRepository.countByProject_ProjectIdAndIsArchivedFalse(projectId);
    }

    @Transactional(readOnly = true)
    public List<RequirementResponseDTO> searchRequirements(String keyword) {
        return requirementsRepository.searchActiveRequirements(keyword).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    private RequirementResponseDTO enrichResponseDto(Requirements entity) {
        RequirementResponseDTO dto = requirementMapper.toResponseDto(entity);

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