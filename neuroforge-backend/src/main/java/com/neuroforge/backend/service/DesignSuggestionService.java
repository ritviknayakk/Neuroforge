package com.neuroforge.backend.service;

import com.neuroforge.backend.dto.designsuggestion.DesignSuggestionAcceptRequestDTO;
import com.neuroforge.backend.dto.designsuggestion.DesignSuggestionRequestDTO;
import com.neuroforge.backend.dto.designsuggestion.DesignSuggestionResponseDTO;
import com.neuroforge.backend.dto.designsuggestion.DesignSuggestionStatusUpdateRequestDTO;
import com.neuroforge.backend.dto.designsuggestion.DesignSuggestionUpdateRequestDTO;
import com.neuroforge.backend.entity.DesignSuggestions;
import com.neuroforge.backend.entity.Project;
import com.neuroforge.backend.entity.Requirements;
import com.neuroforge.backend.entity.User;
import com.neuroforge.backend.mapper.DesignSuggestionMapper;
import com.neuroforge.backend.repository.DesignSuggestionsRepository;
import com.neuroforge.backend.repository.ProjectRepository;
import com.neuroforge.backend.repository.RequirementsRepository;
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
public class DesignSuggestionService {

    private final DesignSuggestionsRepository designSuggestionsRepository;
    private final ProjectRepository projectRepository;
    private final RequirementsRepository requirementsRepository;
    private final UserRepository userRepository;
    private final DesignSuggestionMapper designSuggestionMapper;

    private static final List<String> VALID_STATUSES = Arrays.asList("Pending", "Accepted", "Edited", "Rejected");

    @Transactional(readOnly = true)
    public List<DesignSuggestionResponseDTO> getAllDesignSuggestions() {
        return designSuggestionsRepository.findAll().stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DesignSuggestionResponseDTO> getDesignSuggestionsByProjectId(Integer projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Project not found with id: " + projectId);
        }
        return designSuggestionsRepository.findByProject_ProjectId(projectId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DesignSuggestionResponseDTO> getDesignSuggestionsByRequirementId(Integer requirementId) {
        if (!requirementsRepository.existsById(requirementId)) {
            throw new RuntimeException("Requirement not found with id: " + requirementId);
        }
        return designSuggestionsRepository.findByRequirement_RequirementId(requirementId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DesignSuggestionResponseDTO> getDesignSuggestionsByProjectAndStatus(Integer projectId, String status) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Project not found with id: " + projectId);
        }
        if (!VALID_STATUSES.contains(status)) {
            throw new RuntimeException("Invalid status: " + status + ". Valid statuses: " + VALID_STATUSES);
        }
        return designSuggestionsRepository.findByProject_ProjectIdAndStatus(projectId, status).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DesignSuggestionResponseDTO> getDesignSuggestionsByStatus(String status) {
        if (!VALID_STATUSES.contains(status)) {
            throw new RuntimeException("Invalid status: " + status + ". Valid statuses: " + VALID_STATUSES);
        }
        return designSuggestionsRepository.findByStatus(status).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DesignSuggestionResponseDTO getDesignSuggestionById(Integer id) {
        DesignSuggestions suggestion = designSuggestionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Design suggestion not found with id: " + id));
        return enrichResponseDto(suggestion);
    }

    @Transactional(readOnly = true)
    public List<DesignSuggestionResponseDTO> getAIGeneratedSuggestions() {
        return designSuggestionsRepository.findByIsAIGeneratedTrue().stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DesignSuggestionResponseDTO> searchByComponentsDescription(String keyword) {
        return designSuggestionsRepository.searchByComponentsDescription(keyword).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    public DesignSuggestionResponseDTO createDesignSuggestion(DesignSuggestionRequestDTO request) {
        // Check if project exists
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + request.getProjectId()));

        // Check if requirement exists (if provided)
        Requirements requirement = null;
        if (request.getRequirementId() != null) {
            requirement = requirementsRepository.findById(request.getRequirementId())
                    .orElseThrow(
                            () -> new RuntimeException("Requirement not found with id: " + request.getRequirementId()));
        }

        // Validate status if provided
        if (request.getStatus() != null && !VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        DesignSuggestions suggestion = designSuggestionMapper.toEntity(request);
        suggestion.setProject(project);
        suggestion.setRequirement(requirement);

        // Set default values if not provided
        if (suggestion.getIsAIGenerated() == null) {
            suggestion.setIsAIGenerated(true);
        }
        if (suggestion.getStatus() == null) {
            suggestion.setStatus("Pending");
        }

        DesignSuggestions savedSuggestion = designSuggestionsRepository.save(suggestion);
        return enrichResponseDto(savedSuggestion);
    }

    public DesignSuggestionResponseDTO updateDesignSuggestion(Integer id, DesignSuggestionUpdateRequestDTO request) {
        DesignSuggestions suggestion = designSuggestionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Design suggestion not found with id: " + id));

        // Validate status if being updated
        if (request.getStatus() != null && !VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        designSuggestionMapper.updateEntityFromDto(request, suggestion);
        DesignSuggestions updatedSuggestion = designSuggestionsRepository.save(suggestion);
        return enrichResponseDto(updatedSuggestion);
    }

    public DesignSuggestionResponseDTO updateSuggestionStatus(Integer id,
            DesignSuggestionStatusUpdateRequestDTO request) {
        DesignSuggestions suggestion = designSuggestionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Design suggestion not found with id: " + id));

        if (!VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        suggestion.setStatus(request.getStatus());
        DesignSuggestions updatedSuggestion = designSuggestionsRepository.save(suggestion);
        return enrichResponseDto(updatedSuggestion);
    }

    public DesignSuggestionResponseDTO acceptDesignSuggestion(Integer id, DesignSuggestionAcceptRequestDTO request) {
        DesignSuggestions suggestion = designSuggestionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Design suggestion not found with id: " + id));

        // Check if user exists
        User user = userRepository.findById(request.getAcceptedByUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getAcceptedByUserId()));

        suggestion.setAcceptedBy(user);
        suggestion.setAcceptedAt(LocalDateTime.now());
        suggestion.setStatus("Accepted");

        DesignSuggestions acceptedSuggestion = designSuggestionsRepository.save(suggestion);
        return enrichResponseDto(acceptedSuggestion);
    }

    public void deleteDesignSuggestion(Integer id) {
        if (!designSuggestionsRepository.existsById(id)) {
            throw new RuntimeException("Design suggestion not found with id: " + id);
        }
        designSuggestionsRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long getDesignSuggestionCountByProject(Integer projectId) {
        return designSuggestionsRepository.countByProject_ProjectId(projectId);
    }

    @Transactional(readOnly = true)
    public long getDesignSuggestionCountByProjectAndStatus(Integer projectId, String status) {
        return designSuggestionsRepository.countByProject_ProjectIdAndStatus(projectId, status);
    }

    private DesignSuggestionResponseDTO enrichResponseDto(DesignSuggestions entity) {
        DesignSuggestionResponseDTO dto = designSuggestionMapper.toResponseDto(entity);

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