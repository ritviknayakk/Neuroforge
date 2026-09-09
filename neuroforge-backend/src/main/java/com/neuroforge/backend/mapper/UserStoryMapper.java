package com.neuroforge.backend.mapper;

import com.neuroforge.backend.dto.userstory.UserStoryRequestDTO;
import com.neuroforge.backend.dto.userstory.UserStoryResponseDTO;
import com.neuroforge.backend.dto.userstory.UserStoryUpdateRequestDTO;
import com.neuroforge.backend.entity.UserStories;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {
        RequirementMapper.class, SprintMapper.class, UserMapper.class })
public interface UserStoryMapper {

    @Mapping(target = "userStoryId", ignore = true)
    @Mapping(target = "requirement", ignore = true)
    @Mapping(target = "sprint", ignore = true)
    @Mapping(target = "acceptedBy", ignore = true)
    @Mapping(target = "acceptedAt", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedByUserId", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "rowVersion", ignore = true)
    @Mapping(target = "priority", constant = "Medium")
    @Mapping(target = "status", constant = "Draft")
    @Mapping(target = "isAIGenerated", constant = "false")
    @Mapping(target = "isArchived", constant = "false")
    UserStories toEntity(UserStoryRequestDTO dto);

    @Mapping(source = "requirement.requirementId", target = "requirementId")
    @Mapping(source = "requirement.requirementText", target = "requirementText")
    @Mapping(source = "sprint.sprintId", target = "sprintId")
    @Mapping(source = "sprint.sprintName", target = "sprintName")
    @Mapping(source = "acceptedBy.userId", target = "acceptedByUserId")
    @Mapping(target = "acceptedByFullName", ignore = true)
    @Mapping(target = "createdByFullName", ignore = true)
    @Mapping(target = "modifiedByFullName", ignore = true)
    UserStoryResponseDTO toResponseDto(UserStories entity);

    @Mapping(target = "userStoryId", ignore = true)
    @Mapping(target = "requirement", ignore = true)
    @Mapping(target = "sprint", ignore = true)
    @Mapping(target = "acceptedBy", ignore = true)
    @Mapping(target = "acceptedAt", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedByUserId", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "rowVersion", ignore = true)
    void updateEntityFromDto(UserStoryUpdateRequestDTO dto, @MappingTarget UserStories entity);
}