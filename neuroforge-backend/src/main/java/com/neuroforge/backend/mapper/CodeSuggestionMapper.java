package com.neuroforge.backend.mapper;

import com.neuroforge.backend.dto.codesuggestion.CodeSuggestionRequestDTO;
import com.neuroforge.backend.dto.codesuggestion.CodeSuggestionResponseDTO;
import com.neuroforge.backend.dto.codesuggestion.CodeSuggestionUpdateRequestDTO;
import com.neuroforge.backend.entity.CodeSuggestions;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {
        TaskMapper.class, UserMapper.class })
public interface CodeSuggestionMapper {

    @Mapping(target = "codeSuggestionId", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "decidedBy", ignore = true)
    @Mapping(target = "decidedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isAIGenerated", constant = "true")
    @Mapping(target = "status", constant = "Pending")
    CodeSuggestions toEntity(CodeSuggestionRequestDTO dto);

    @Mapping(source = "task.taskId", target = "taskId")
    @Mapping(source = "task.title", target = "taskTitle")
    @Mapping(source = "decidedBy.userId", target = "decidedByUserId")
    @Mapping(target = "decidedByFullName", ignore = true)
    CodeSuggestionResponseDTO toResponseDto(CodeSuggestions entity);

    @Mapping(target = "codeSuggestionId", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "decidedBy", ignore = true)
    @Mapping(target = "decidedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(CodeSuggestionUpdateRequestDTO dto, @MappingTarget CodeSuggestions entity);
}