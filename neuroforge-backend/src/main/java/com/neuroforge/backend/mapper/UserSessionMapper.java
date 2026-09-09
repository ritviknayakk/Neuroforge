package com.neuroforge.backend.mapper;

import com.neuroforge.backend.dto.usersession.UserSessionRequestDTO;
import com.neuroforge.backend.dto.usersession.UserSessionResponseDTO;
import com.neuroforge.backend.entity.UserSessions;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {
        UserMapper.class })
public interface UserSessionMapper {

    @Mapping(target = "sessionId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "tokenHash", ignore = true)
    @Mapping(target = "issuedAt", ignore = true)
    @Mapping(target = "expiresAt", ignore = true)
    @Mapping(target = "revokedAt", ignore = true)
    @Mapping(target = "ipAddress", source = "ipAddress")
    UserSessions toEntity(UserSessionRequestDTO dto);

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.fullName", target = "userFullName")
    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(target = "isActive", expression = "java(entity.getRevokedAt() == null)")
    @Mapping(target = "isExpired", expression = "java(entity.getExpiresAt() != null && entity.getExpiresAt().isBefore(java.time.LocalDateTime.now()))")
    UserSessionResponseDTO toResponseDto(UserSessions entity);
}