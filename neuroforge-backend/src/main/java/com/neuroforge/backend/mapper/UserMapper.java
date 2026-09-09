package com.neuroforge.backend.mapper;

import com.neuroforge.backend.dto.user.UserCreateRequestDTO;
import com.neuroforge.backend.dto.user.UserResponseDTO;
import com.neuroforge.backend.dto.user.UserUpdateRequestDTO;
import com.neuroforge.backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * roleId (DTO) <-> role (entity) is intentionally NOT auto-mapped — turning
 * a raw roleId into a managed Role reference needs a repository lookup
 * (and a "role not found" check), which belongs in the service layer, not
 * a mapper. Same reasoning for passwordHash: the service hashes the
 * plaintext password from UserCreateRequestDTO and sets it on the entity
 * itself after calling toEntity().
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "isArchived", constant = "false")
    @Mapping(target = "lastLoginAt", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedByUserId", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "rowVersion", ignore = true)
    User toEntity(UserCreateRequestDTO dto);

    @Mapping(source = "role.roleId", target = "roleId")
    @Mapping(source = "role.roleName", target = "roleName")
    UserResponseDTO toResponseDto(User entity);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "isArchived", ignore = true)
    @Mapping(target = "lastLoginAt", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedByUserId", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "rowVersion", ignore = true)
    void updateEntityFromDto(UserUpdateRequestDTO dto, @MappingTarget User entity);
}