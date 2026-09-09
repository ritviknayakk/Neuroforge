package com.neuroforge.backend.mapper;

import com.neuroforge.backend.dto.requirement.RequirementRequestDTO;
import com.neuroforge.backend.dto.requirement.RequirementResponseDTO;
import com.neuroforge.backend.dto.requirement.RequirementUpdateRequestDTO;
import com.neuroforge.backend.entity.Requirements;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {
        ProjectMapper.class, UserMapper.class })
public interface RequirementMapper {

    @Mapping(target = "requirementId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedByUserId", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "rowVersion", ignore = true)
    @Mapping(target = "status", constant = "Draft")
    @Mapping(target = "isArchived", constant = "false")
    Requirements toEntity(RequirementRequestDTO dto);

    @Mapping(source = "project.projectId", target = "projectId")
    @Mapping(source = "project.projectName", target = "projectName")
    @Mapping(target = "createdByFullName", ignore = true)
    @Mapping(target = "modifiedByFullName", ignore = true)
    RequirementResponseDTO toResponseDto(Requirements entity);

    @Mapping(target = "requirementId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedByUserId", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "rowVersion", ignore = true)
    void updateEntityFromDto(RequirementUpdateRequestDTO dto, @MappingTarget Requirements entity);
}