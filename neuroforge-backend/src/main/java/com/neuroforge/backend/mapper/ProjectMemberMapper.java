package com.neuroforge.backend.mapper;

import com.neuroforge.backend.dto.projectmember.ProjectMemberRequestDTO;
import com.neuroforge.backend.dto.projectmember.ProjectMemberResponseDTO;
import com.neuroforge.backend.dto.projectmember.ProjectMemberUpdateRequestDTO;
import com.neuroforge.backend.entity.ProjectMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {
        ProjectMapper.class, UserMapper.class, RoleMapper.class })
public interface ProjectMemberMapper {

    @Mapping(target = "projectMemberId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "projectRole", ignore = true)
    @Mapping(target = "joinedAt", ignore = true)
    @Mapping(target = "removedByUserId", ignore = true)
    @Mapping(target = "removedAt", ignore = true)
    @Mapping(target = "addedByUserId", source = "addedByUserId")
    ProjectMember toEntity(ProjectMemberRequestDTO dto);

    @Mapping(source = "project.projectId", target = "projectId")
    @Mapping(source = "project.projectName", target = "projectName")
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.fullName", target = "userFullName")
    @Mapping(source = "projectRole.roleId", target = "projectRoleId")
    @Mapping(source = "projectRole.roleName", target = "projectRoleName")
    @Mapping(source = "addedByUserId", target = "addedByUserId")
    @Mapping(target = "addedByFullName", ignore = true) // Will be set in service
    @Mapping(target = "removedByFullName", ignore = true) // Will be set in service
    @Mapping(target = "isActive", expression = "java(entity.getRemovedAt() == null)")
    ProjectMemberResponseDTO toResponseDto(ProjectMember entity);

    @Mapping(target = "projectMemberId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "projectRole", ignore = true)
    @Mapping(target = "addedByUserId", ignore = true)
    @Mapping(target = "joinedAt", ignore = true)
    @Mapping(target = "removedAt", ignore = true)
    void updateEntityFromDto(ProjectMemberUpdateRequestDTO dto, @MappingTarget ProjectMember entity);
}