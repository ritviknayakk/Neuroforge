package com.neuroforge.backend.mapper;

import com.neuroforge.backend.dto.designsuggestion.DesignSuggestionRequestDTO;
import com.neuroforge.backend.dto.designsuggestion.DesignSuggestionResponseDTO;
import com.neuroforge.backend.dto.designsuggestion.DesignSuggestionUpdateRequestDTO;
import com.neuroforge.backend.entity.DesignSuggestions;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {
        ProjectMapper.class, RequirementMapper.class, UserMapper.class })
public interface DesignSuggestionMapper {

    @Mapping(target = "designSuggestionId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "requirement", ignore = true)
    @Mapping(target = "acceptedBy", ignore = true)
    @Mapping(target = "acceptedAt", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedByUserId", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "isAIGenerated", constant = "true")
    @Mapping(target = "status", constant = "Pending")
    DesignSuggestions toEntity(DesignSuggestionRequestDTO dto);

    @Mapping(source = "project.projectId", target = "projectId")
    @Mapping(source = "project.projectName", target = "projectName")
    @Mapping(source = "requirement.requirementId", target = "requirementId")
    @Mapping(source = "requirement.requirementText", target = "requirementText")
    @Mapping(source = "acceptedBy.userId", target = "acceptedByUserId")
    @Mapping(target = "acceptedByFullName", ignore = true)
    @Mapping(target = "createdByFullName", ignore = true)
    @Mapping(target = "modifiedByFullName", ignore = true)
    DesignSuggestionResponseDTO toResponseDto(DesignSuggestions entity);

    @Mapping(target = "designSuggestionId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "requirement", ignore = true)
    @Mapping(target = "acceptedBy", ignore = true)
    @Mapping(target = "acceptedAt", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedByUserId", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    void updateEntityFromDto(DesignSuggestionUpdateRequestDTO dto, @MappingTarget DesignSuggestions entity);
}