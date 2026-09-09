package com.neuroforge.backend.service;

import com.neuroforge.backend.dto.deployment.DeploymentCompleteRequestDTO;
import com.neuroforge.backend.dto.deployment.DeploymentRequestDTO;
import com.neuroforge.backend.dto.deployment.DeploymentResponseDTO;
import com.neuroforge.backend.dto.deployment.DeploymentStatusUpdateRequestDTO;
import com.neuroforge.backend.dto.deployment.DeploymentUpdateRequestDTO;
import com.neuroforge.backend.entity.Deployments;
import com.neuroforge.backend.entity.Project;
import com.neuroforge.backend.entity.User;
import com.neuroforge.backend.mapper.DeploymentMapper;
import com.neuroforge.backend.repository.DeploymentsRepository;
import com.neuroforge.backend.repository.ProjectRepository;
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
public class DeploymentService {

    private final DeploymentsRepository deploymentsRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final DeploymentMapper deploymentMapper;

    private static final List<String> VALID_STATUSES = Arrays.asList("InProgress", "Successful", "Failed");

    @Transactional(readOnly = true)
    public List<DeploymentResponseDTO> getAllDeployments() {
        return deploymentsRepository.findAll().stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DeploymentResponseDTO> getDeploymentsByProjectId(Integer projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Project not found with id: " + projectId);
        }
        return deploymentsRepository.findByProject_ProjectId(projectId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DeploymentResponseDTO> getDeploymentsByStatus(String status) {
        if (!VALID_STATUSES.contains(status)) {
            throw new RuntimeException("Invalid status: " + status + ". Valid statuses: " + VALID_STATUSES);
        }
        return deploymentsRepository.findByStatus(status).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DeploymentResponseDTO> getDeploymentsByProjectAndStatus(Integer projectId, String status) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Project not found with id: " + projectId);
        }
        if (!VALID_STATUSES.contains(status)) {
            throw new RuntimeException("Invalid status: " + status + ". Valid statuses: " + VALID_STATUSES);
        }
        return deploymentsRepository.findByProject_ProjectIdAndStatus(projectId, status).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DeploymentResponseDTO> getRecentSuccessfulDeployments(Integer projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Project not found with id: " + projectId);
        }
        return deploymentsRepository.findRecentSuccessfulDeployments(projectId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DeploymentResponseDTO> getRecentFailedDeployments(Integer projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Project not found with id: " + projectId);
        }
        return deploymentsRepository.findRecentFailedDeployments(projectId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DeploymentResponseDTO getDeploymentById(Integer id) {
        Deployments deployment = deploymentsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deployment not found with id: " + id));
        return enrichResponseDto(deployment);
    }

    @Transactional(readOnly = true)
    public DeploymentResponseDTO getDeploymentByProjectAndVersion(Integer projectId, String version) {
        Deployments deployment = deploymentsRepository.findByProject_ProjectIdAndVersion(projectId, version)
                .orElseThrow(() -> new RuntimeException(
                        "Deployment not found for project " + projectId + " with version: " + version));
        return enrichResponseDto(deployment);
    }

    @Transactional(readOnly = true)
    public List<DeploymentResponseDTO> getDeploymentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return deploymentsRepository.findDeploymentsBetweenDates(startDate, endDate).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DeploymentResponseDTO> getStalledDeployments(int timeoutMinutes) {
        LocalDateTime timeout = LocalDateTime.now().minusMinutes(timeoutMinutes);
        return deploymentsRepository.findStalledDeployments(timeout).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    public DeploymentResponseDTO createDeployment(DeploymentRequestDTO request) {
        // Check if project exists
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + request.getProjectId()));

        // Check if triggered by user exists
        User triggeredBy = userRepository.findById(request.getTriggeredByUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getTriggeredByUserId()));

        // Check if version already exists for this project
        if (deploymentsRepository.findByProject_ProjectIdAndVersion(request.getProjectId(), request.getVersion())
                .isPresent()) {
            throw new RuntimeException("Version '" + request.getVersion() + "' already exists for this project");
        }

        // Validate status if provided
        if (request.getStatus() != null && !VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        Deployments deployment = deploymentMapper.toEntity(request);
        deployment.setProject(project);
        deployment.setTriggeredBy(triggeredBy);
        deployment.setStartedAt(LocalDateTime.now());

        // Set default values if not provided
        if (deployment.getStatus() == null) {
            deployment.setStatus("InProgress");
        }

        Deployments savedDeployment = deploymentsRepository.save(deployment);
        return enrichResponseDto(savedDeployment);
    }

    public DeploymentResponseDTO updateDeployment(Integer id, DeploymentUpdateRequestDTO request) {
        Deployments deployment = deploymentsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deployment not found with id: " + id));

        // Validate status if being updated
        if (request.getStatus() != null && !VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        deploymentMapper.updateEntityFromDto(request, deployment);
        Deployments updatedDeployment = deploymentsRepository.save(deployment);
        return enrichResponseDto(updatedDeployment);
    }

    public DeploymentResponseDTO updateDeploymentStatus(Integer id, DeploymentStatusUpdateRequestDTO request) {
        Deployments deployment = deploymentsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deployment not found with id: " + id));

        if (!VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        deployment.setStatus(request.getStatus());

        // If status is not InProgress, set completedAt
        if (!"InProgress".equals(request.getStatus())) {
            deployment.setCompletedAt(LocalDateTime.now());
        }

        Deployments updatedDeployment = deploymentsRepository.save(deployment);
        return enrichResponseDto(updatedDeployment);
    }

    public DeploymentResponseDTO completeDeployment(Integer id, DeploymentCompleteRequestDTO request) {
        Deployments deployment = deploymentsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deployment not found with id: " + id));

        if (!VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        if ("InProgress".equals(request.getStatus())) {
            throw new RuntimeException("Cannot complete deployment with status 'InProgress'");
        }

        deployment.setStatus(request.getStatus());
        deployment.setCompletedAt(LocalDateTime.now());

        if (request.getErrorLogUrl() != null) {
            deployment.setErrorLogUrl(request.getErrorLogUrl());
        }

        Deployments completedDeployment = deploymentsRepository.save(deployment);
        return enrichResponseDto(completedDeployment);
    }

    public void deleteDeployment(Integer id) {
        if (!deploymentsRepository.existsById(id)) {
            throw new RuntimeException("Deployment not found with id: " + id);
        }
        deploymentsRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long getDeploymentCountByProject(Integer projectId) {
        return deploymentsRepository.countByProject_ProjectId(projectId);
    }

    @Transactional(readOnly = true)
    public long getDeploymentCountByProjectAndStatus(Integer projectId, String status) {
        return deploymentsRepository.countByProject_ProjectIdAndStatus(projectId, status);
    }

    private DeploymentResponseDTO enrichResponseDto(Deployments entity) {
        DeploymentResponseDTO dto = deploymentMapper.toResponseDto(entity);

        // Set triggered by full name
        if (entity.getTriggeredBy() != null && entity.getTriggeredBy().getUserId() != null) {
            userRepository.findById(entity.getTriggeredBy().getUserId())
                    .ifPresent(user -> dto.setTriggeredByFullName(user.getFullName()));
        }

        return dto;
    }
}