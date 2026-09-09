package com.neuroforge.backend.mapper;

import com.neuroforge.backend.dto.testcase.TestCaseRequestDTO;
import com.neuroforge.backend.dto.testcase.TestCaseResponseDTO;
import com.neuroforge.backend.dto.testcase.TestCaseUpdateRequestDTO;
import com.neuroforge.backend.entity.TestCases;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TestCaseMapper {

    @Mapping(target = "testCaseId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "userStory", ignore = true)
    @Mapping(target = "reviewedBy", ignore = true)
    @Mapping(target = "reviewedAt", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedByUserId", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "rowVersion", ignore = true)
    @Mapping(target = "isAIGenerated", constant = "true")
    @Mapping(target = "status", constant = "Draft")
    @Mapping(target = "isArchived", constant = "false")
    TestCases toEntity(TestCaseRequestDTO dto);

    @Mapping(source = "project.projectId", target = "projectId")
    @Mapping(source = "project.projectName", target = "projectName")
    @Mapping(source = "userStory.userStoryId", target = "userStoryId")
    @Mapping(source = "userStory.storyText", target = "userStoryText")
    @Mapping(source = "reviewedBy.userId", target = "reviewedByUserId")
    @Mapping(target = "reviewedByFullName", ignore = true)
    @Mapping(target = "createdByFullName", ignore = true)
    @Mapping(target = "modifiedByFullName", ignore = true)
    TestCaseResponseDTO toResponseDto(TestCases entity);

    @Mapping(target = "testCaseId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "userStory", ignore = true)
    @Mapping(target = "reviewedBy", ignore = true)
    @Mapping(target = "reviewedAt", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedByUserId", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "rowVersion", ignore = true)
    void updateEntityFromDto(TestCaseUpdateRequestDTO dto, @MappingTarget TestCases entity);
}