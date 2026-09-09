package com.neuroforge.backend.service;

import com.neuroforge.backend.dto.projectmember.ProjectMemberRequestDTO;
import com.neuroforge.backend.dto.projectmember.ProjectMemberResponseDTO;
import com.neuroforge.backend.dto.projectmember.ProjectMemberUpdateRequestDTO;
import com.neuroforge.backend.entity.Project;
import com.neuroforge.backend.entity.ProjectMember;
import com.neuroforge.backend.entity.Role;
import com.neuroforge.backend.entity.User;
import com.neuroforge.backend.mapper.ProjectMemberMapper;
import com.neuroforge.backend.repository.ProjectMemberRepository;
import com.neuroforge.backend.repository.ProjectRepository;
import com.neuroforge.backend.repository.RoleRepository;
import com.neuroforge.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProjectMemberMapper projectMemberMapper;

    @Transactional(readOnly = true)
    public List<ProjectMemberResponseDTO> getAllProjectMembers() {
        return projectMemberRepository.findAll().stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProjectMemberResponseDTO> getMembersByProjectId(Integer projectId) {
        return projectMemberRepository.findByProject_ProjectId(projectId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProjectMemberResponseDTO> getActiveMembersByProjectId(Integer projectId) {
        return projectMemberRepository.findByProject_ProjectIdAndRemovedAtIsNull(projectId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProjectMemberResponseDTO> getMembersByUserId(Integer userId) {
        return projectMemberRepository.findByUser_UserId(userId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProjectMemberResponseDTO getProjectMemberById(Integer id) {
        ProjectMember projectMember = projectMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project member not found with id: " + id));
        return enrichResponseDto(projectMember);
    }

    public ProjectMemberResponseDTO addMemberToProject(ProjectMemberRequestDTO request) {
        // Check if project exists
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + request.getProjectId()));

        // Check if user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        // Check if role exists
        Role role = roleRepository.findById(request.getProjectRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + request.getProjectRoleId()));

        // Check if user is already an active member
        if (projectMemberRepository.existsByProject_ProjectIdAndUser_UserIdAndRemovedAtIsNull(
                request.getProjectId(), request.getUserId())) {
            throw new RuntimeException("User is already an active member of this project");
        }

        // Check if user was previously removed (to reactivate)
        // Uses findFirst...OrderBy...Desc which is safe with LIMIT 1
        Optional<ProjectMember> existingMember = projectMemberRepository
                .findFirstByProject_ProjectIdAndUser_UserIdAndRemovedAtIsNotNullOrderByRemovedAtDesc(
                        request.getProjectId(), request.getUserId());

        ProjectMember projectMember;
        if (existingMember.isPresent()) {
            // Reactivate the most recently removed member
            ProjectMember member = existingMember.get();
            member.setRemovedAt(null);
            member.setRemovedByUserId(null);
            member.setProjectRole(role);
            member.setAddedByUserId(request.getAddedByUserId());
            projectMember = projectMemberRepository.save(member);
        } else {
            // Create new member
            projectMember = projectMemberMapper.toEntity(request);
            projectMember.setProject(project);
            projectMember.setUser(user);
            projectMember.setProjectRole(role);
            projectMember = projectMemberRepository.save(projectMember);
        }

        return enrichResponseDto(projectMember);
    }

    public ProjectMemberResponseDTO updateMemberRole(Integer id, ProjectMemberUpdateRequestDTO request) {
        ProjectMember projectMember = projectMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project member not found with id: " + id));

        // Check if role exists
        Role role = roleRepository.findById(request.getProjectRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + request.getProjectRoleId()));

        projectMember.setProjectRole(role);
        ProjectMember updatedMember = projectMemberRepository.save(projectMember);
        return enrichResponseDto(updatedMember);
    }

    public ProjectMemberResponseDTO removeMemberFromProject(Integer id, Integer removedByUserId) {
        ProjectMember projectMember = projectMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project member not found with id: " + id));

        if (projectMember.getRemovedAt() != null) {
            throw new RuntimeException("Member is already removed from this project");
        }

        projectMember.setRemovedAt(LocalDateTime.now());
        projectMember.setRemovedByUserId(removedByUserId);

        ProjectMember removedMember = projectMemberRepository.save(projectMember);
        return enrichResponseDto(removedMember);
    }

    public void deleteProjectMember(Integer id) {
        if (!projectMemberRepository.existsById(id)) {
            throw new RuntimeException("Project member not found with id: " + id);
        }
        projectMemberRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long getActiveMemberCount(Integer projectId) {
        return projectMemberRepository.countByProject_ProjectIdAndRemovedAtIsNull(projectId);
    }

    private ProjectMemberResponseDTO enrichResponseDto(ProjectMember entity) {
        ProjectMemberResponseDTO dto = projectMemberMapper.toResponseDto(entity);

        // Set added by full name
        if (entity.getAddedByUserId() != null) {
            userRepository.findById(entity.getAddedByUserId())
                    .ifPresent(user -> dto.setAddedByFullName(user.getFullName()));
        }

        // Set removed by full name
        if (entity.getRemovedByUserId() != null) {
            userRepository.findById(entity.getRemovedByUserId())
                    .ifPresent(user -> dto.setRemovedByFullName(user.getFullName()));
        }

        return dto;
    }
}