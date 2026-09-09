package com.neuroforge.backend.mapper;

import com.neuroforge.backend.dto.rolepermission.RolePermissionRequestDTO;
import com.neuroforge.backend.dto.rolepermission.RolePermissionResponseDTO;
import com.neuroforge.backend.dto.rolepermission.RolePermissionUpdateRequestDTO;
import com.neuroforge.backend.entity.RolePermissions;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {
        RoleMapper.class, ModuleMapper.class })
public interface RolePermissionMapper {

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "module", ignore = true)
    RolePermissions toEntity(RolePermissionRequestDTO dto);

    @Mapping(source = "role.roleId", target = "roleId")
    @Mapping(source = "role.roleName", target = "roleName")
    @Mapping(source = "module.moduleId", target = "moduleId")
    @Mapping(source = "module.moduleName", target = "moduleName")
    RolePermissionResponseDTO toResponseDto(RolePermissions entity);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "module", ignore = true)
    void updateEntityFromDto(RolePermissionUpdateRequestDTO dto, @MappingTarget RolePermissions entity);
}