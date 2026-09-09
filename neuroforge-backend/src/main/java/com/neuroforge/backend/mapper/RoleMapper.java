package com.neuroforge.backend.mapper;

import com.neuroforge.backend.dto.role.RoleRequestDTO;
import com.neuroforge.backend.dto.role.RoleResponseDTO;
import com.neuroforge.backend.dto.role.RoleUpdateRequestDTO;
import com.neuroforge.backend.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {

    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Role toEntity(RoleRequestDTO dto);

    RoleResponseDTO toResponseDto(Role entity);

    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(RoleUpdateRequestDTO dto, @MappingTarget Role entity);
}