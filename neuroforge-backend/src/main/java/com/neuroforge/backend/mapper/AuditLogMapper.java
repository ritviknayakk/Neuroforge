package com.neuroforge.backend.mapper;

import com.neuroforge.backend.dto.auditlog.AuditLogRequestDTO;
import com.neuroforge.backend.dto.auditlog.AuditLogResponseDTO;
import com.neuroforge.backend.entity.AuditLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {
        UserMapper.class })
public interface AuditLogMapper {

    @Mapping(target = "auditLogId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    AuditLog toEntity(AuditLogRequestDTO dto);

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(target = "userFullName", ignore = true)
    @Mapping(target = "userEmail", ignore = true)
    AuditLogResponseDTO toResponseDto(AuditLog entity);
}