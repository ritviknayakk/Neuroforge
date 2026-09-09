package com.neuroforge.backend.mapper;

import com.neuroforge.backend.dto.issue.IssueRequestDTO;
import com.neuroforge.backend.dto.issue.IssueResponseDTO;
import com.neuroforge.backend.dto.issue.IssueUpdateRequestDTO;
import com.neuroforge.backend.entity.Issues;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface IssueMapper {

    @Mapping(target = "issueId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "reportedBy", ignore = true)
    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "relatedTestRun", ignore = true)
    @Mapping(target = "relatedTask", ignore = true)
    @Mapping(target = "closedBy", ignore = true)
    @Mapping(target = "closedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedByUserId", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "rowVersion", ignore = true)
    @Mapping(target = "status", constant = "Open")
    @Mapping(target = "isArchived", constant = "false")
    Issues toEntity(IssueRequestDTO dto);

    @Mapping(source = "project.projectId", target = "projectId")
    @Mapping(source = "project.projectName", target = "projectName")
    @Mapping(source = "reportedBy.userId", target = "reportedByUserId")
    @Mapping(source = "assignedTo.userId", target = "assignedToUserId")
    @Mapping(source = "relatedTestRun.testRunId", target = "relatedTestRunId")
    @Mapping(source = "relatedTask.taskId", target = "relatedTaskId")
    @Mapping(source = "closedBy.userId", target = "closedByUserId")
    @Mapping(target = "reportedByFullName", ignore = true)
    @Mapping(target = "assignedToFullName", ignore = true)
    @Mapping(target = "closedByFullName", ignore = true)
    @Mapping(target = "modifiedByFullName", ignore = true)
    IssueResponseDTO toResponseDto(Issues entity);

    @Mapping(target = "issueId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "reportedBy", ignore = true)
    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "relatedTestRun", ignore = true)
    @Mapping(target = "relatedTask", ignore = true)
    @Mapping(target = "closedBy", ignore = true)
    @Mapping(target = "closedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedByUserId", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "rowVersion", ignore = true)
    void updateEntityFromDto(IssueUpdateRequestDTO dto, @MappingTarget Issues entity);
}