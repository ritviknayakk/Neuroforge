package com.neuroforge.backend.mapper;

import com.neuroforge.backend.dto.sprint.SprintRequestDTO;
import com.neuroforge.backend.dto.sprint.SprintResponseDTO;
import com.neuroforge.backend.dto.sprint.SprintUpdateRequestDTO;
import com.neuroforge.backend.entity.Sprints;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.temporal.ChronoUnit;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, imports = {
        ChronoUnit.class })
public interface SprintMapper {

    @Mapping(target = "sprintId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedByUserId", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "rowVersion", ignore = true)
    @Mapping(target = "status", constant = "Planned")
    Sprints toEntity(SprintRequestDTO dto);

    @Mapping(source = "project.projectId", target = "projectId")
    @Mapping(source = "project.projectName", target = "projectName")
    @Mapping(target = "createdByFullName", ignore = true)
    @Mapping(target = "modifiedByFullName", ignore = true)
    @Mapping(target = "durationInDays", expression = "java((int) ChronoUnit.DAYS.between(entity.getStartDate(), entity.getEndDate()) + 1)")
    SprintResponseDTO toResponseDto(Sprints entity);

    @Mapping(target = "sprintId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedByUserId", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "rowVersion", ignore = true)
    void updateEntityFromDto(SprintUpdateRequestDTO dto, @MappingTarget Sprints entity);
}