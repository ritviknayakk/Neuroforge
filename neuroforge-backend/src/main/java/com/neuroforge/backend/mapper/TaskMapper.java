package com.neuroforge.backend.mapper;

import com.neuroforge.backend.dto.task.TaskRequestDTO;
import com.neuroforge.backend.dto.task.TaskResponseDTO;
import com.neuroforge.backend.dto.task.TaskUpdateRequestDTO;
import com.neuroforge.backend.entity.Tasks;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {
        ProjectMapper.class, UserStoryMapper.class, SprintMapper.class, UserMapper.class })
public interface TaskMapper {

    @Mapping(target = "taskId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "userStory", ignore = true)
    @Mapping(target = "sprint", ignore = true)
    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedByUserId", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "rowVersion", ignore = true)
    @Mapping(target = "status", defaultValue = "ToDo")
    @Mapping(target = "priority", defaultValue = "Medium")
    @Mapping(target = "isArchived", defaultValue = "false")
    Tasks toEntity(TaskRequestDTO dto);

    @Mapping(source = "project.projectId", target = "projectId")
    @Mapping(source = "project.projectName", target = "projectName")
    @Mapping(source = "userStory.userStoryId", target = "userStoryId")
    @Mapping(source = "userStory.storyText", target = "userStoryText")
    @Mapping(source = "sprint.sprintId", target = "sprintId")
    @Mapping(source = "sprint.sprintName", target = "sprintName")
    @Mapping(source = "assignedTo.userId", target = "assignedToUserId")
    @Mapping(target = "assignedToFullName", ignore = true)
    @Mapping(target = "createdByFullName", ignore = true)
    @Mapping(target = "modifiedByFullName", ignore = true)
    TaskResponseDTO toResponseDto(Tasks entity);

    @Mapping(target = "taskId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "userStory", ignore = true)
    @Mapping(target = "sprint", ignore = true)
    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedByUserId", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "rowVersion", ignore = true)
    void updateEntityFromDto(TaskUpdateRequestDTO dto, @MappingTarget Tasks entity);
}