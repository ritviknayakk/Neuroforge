package com.neuroforge.backend.mapper;

import com.neuroforge.backend.dto.deployment.DeploymentRequestDTO;
import com.neuroforge.backend.dto.deployment.DeploymentResponseDTO;
import com.neuroforge.backend.dto.deployment.DeploymentUpdateRequestDTO;
import com.neuroforge.backend.entity.Deployments;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.Duration; // Keep this - it's used in the expression

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DeploymentMapper {

    @Mapping(target = "deploymentId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "triggeredBy", ignore = true)
    @Mapping(target = "startedAt", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "rowVersion", ignore = true)
    @Mapping(target = "status", constant = "InProgress")
    Deployments toEntity(DeploymentRequestDTO dto);

    @Mapping(source = "project.projectId", target = "projectId")
    @Mapping(source = "project.projectName", target = "projectName")
    @Mapping(source = "triggeredBy.userId", target = "triggeredByUserId")
    @Mapping(target = "triggeredByFullName", ignore = true)
    @Mapping(target = "durationInSeconds", expression = "java(entity.getCompletedAt() != null ? java.time.Duration.between(entity.getStartedAt(), entity.getCompletedAt()).getSeconds() : null)")
    DeploymentResponseDTO toResponseDto(Deployments entity);

    @Mapping(target = "deploymentId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "triggeredBy", ignore = true)
    @Mapping(target = "startedAt", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "rowVersion", ignore = true)
    @Mapping(target = "version", ignore = true) // ← Add this to ignore version in update
    void updateEntityFromDto(DeploymentUpdateRequestDTO dto, @MappingTarget Deployments entity);
}