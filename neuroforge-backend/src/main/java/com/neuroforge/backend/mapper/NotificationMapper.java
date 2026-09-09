package com.neuroforge.backend.mapper;

import com.neuroforge.backend.dto.notification.NotificationRequestDTO;
import com.neuroforge.backend.dto.notification.NotificationResponseDTO;
import com.neuroforge.backend.dto.notification.NotificationUpdateRequestDTO;
import com.neuroforge.backend.entity.Notifications;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NotificationMapper {

    @Mapping(target = "notificationId", ignore = true)
    @Mapping(target = "recipient", ignore = true)
    @Mapping(target = "deployment", ignore = true)
    @Mapping(target = "issue", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isRead", constant = "false")
    Notifications toEntity(NotificationRequestDTO dto);

    @Mapping(source = "recipient.userId", target = "recipientUserId")
    @Mapping(target = "recipientFullName", ignore = true)
    @Mapping(source = "deployment.deploymentId", target = "deploymentId")
    @Mapping(source = "deployment.version", target = "deploymentVersion")
    @Mapping(source = "issue.issueId", target = "issueId")
    @Mapping(source = "issue.title", target = "issueTitle")
    NotificationResponseDTO toResponseDto(Notifications entity);

    @Mapping(target = "notificationId", ignore = true)
    @Mapping(target = "recipient", ignore = true)
    @Mapping(target = "deployment", ignore = true)
    @Mapping(target = "issue", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(NotificationUpdateRequestDTO dto, @MappingTarget Notifications entity);
}