package com.neuroforge.backend.service;

import com.neuroforge.backend.dto.project.ProjectRequestDTO;
import com.neuroforge.backend.dto.project.ProjectResponseDTO;
import com.neuroforge.backend.dto.project.ProjectUpdateRequestDTO;
import com.neuroforge.backend.entity.Project;
import com.neuroforge.backend.mapper.ProjectMapper;
import com.neuroforge.backend.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Transactional(readOnly = true)
    public List<ProjectResponseDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProjectResponseDTO> getProjectsByStatus(String status) {
        return projectRepository.findByStatus(status).stream()
                .map(projectMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProjectResponseDTO getProjectById(Integer id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
        return projectMapper.toResponseDto(project);
    }

    public ProjectResponseDTO createProject(ProjectRequestDTO request) {
        // Check if project name already exists
        if (projectRepository.findByProjectName(request.getProjectName()).isPresent()) {
            throw new RuntimeException("Project with name '" + request.getProjectName() + "' already exists");
        }

        Project project = projectMapper.toEntity(request);
        // Set default status if not provided
        if (project.getStatus() == null) {
            project.setStatus("Active");
        }

        Project savedProject = projectRepository.save(project);
        return projectMapper.toResponseDto(savedProject);
    }

    public ProjectResponseDTO updateProject(Integer id, ProjectUpdateRequestDTO request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

        // Check if project name is being changed and if it already exists
        if (request.getProjectName() != null && !request.getProjectName().equals(project.getProjectName())) {
            if (projectRepository.findByProjectName(request.getProjectName()).isPresent()) {
                throw new RuntimeException("Project with name '" + request.getProjectName() + "' already exists");
            }
        }

        projectMapper.updateEntityFromDto(request, project);

        // If status is being changed to Archived, set ArchivedAt
        if ("Archived".equals(request.getStatus()) && !"Archived".equals(project.getStatus())) {
            project.setArchivedAt(LocalDateTime.now());
        }

        Project updatedProject = projectRepository.save(project);
        return projectMapper.toResponseDto(updatedProject);
    }

    public void deleteProject(Integer id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }

    public ProjectResponseDTO archiveProject(Integer id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

        project.setStatus("Archived");
        project.setArchivedAt(LocalDateTime.now());

        Project archivedProject = projectRepository.save(project);
        return projectMapper.toResponseDto(archivedProject);
    }
}