package com.neuroforge.backend.mapper;

import com.neuroforge.backend.dto.testrun.TestRunRequestDTO;
import com.neuroforge.backend.dto.testrun.TestRunResponseDTO;
import com.neuroforge.backend.dto.testrun.TestRunUpdateRequestDTO;
import com.neuroforge.backend.entity.TestRuns;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TestRunMapper {

    @Mapping(target = "testRunId", ignore = true)
    @Mapping(target = "testCase", ignore = true)
    @Mapping(target = "executedBy", ignore = true)
    @Mapping(target = "executedAt", ignore = true)
    TestRuns toEntity(TestRunRequestDTO dto);

    @Mapping(source = "testCase.testCaseId", target = "testCaseId")
    @Mapping(source = "testCase.featureName", target = "testCaseFeatureName")
    @Mapping(source = "executedBy.userId", target = "executedByUserId")
    @Mapping(target = "executedByFullName", ignore = true)
    TestRunResponseDTO toResponseDto(TestRuns entity);

    @Mapping(target = "testRunId", ignore = true)
    @Mapping(target = "testCase", ignore = true)
    @Mapping(target = "executedBy", ignore = true)
    @Mapping(target = "executedAt", ignore = true)
    void updateEntityFromDto(TestRunUpdateRequestDTO dto, @MappingTarget TestRuns entity);
}