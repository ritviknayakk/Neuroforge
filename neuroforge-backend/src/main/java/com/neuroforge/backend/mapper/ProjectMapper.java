package com.neuroforge.backend.mapper;

import com.neuroforge.backend.dto.project.ProjectRequestDTO;
import com.neuroforge.backend.dto.project.ProjectResponseDTO;
import com.neuroforge.backend.dto.project.ProjectUpdateRequestDTO;
import com.neuroforge.backend.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProjectMapper {

    @Mapping(target = "projectId", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedByUserId", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "archivedAt", ignore = true)
    @Mapping(target = "rowVersion", ignore = true)
    @Mapping(target = "status", constant = "Active")
    Project toEntity(ProjectRequestDTO dto);

    ProjectResponseDTO toResponseDto(Project entity);

    @Mapping(target = "projectId", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedByUserId", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "archivedAt", ignore = true)
    @Mapping(target = "rowVersion", ignore = true)
    void updateEntityFromDto(ProjectUpdateRequestDTO dto, @MappingTarget Project entity);
}