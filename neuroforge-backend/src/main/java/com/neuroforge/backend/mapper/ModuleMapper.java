package com.neuroforge.backend.mapper;

import com.neuroforge.backend.dto.module.ModuleRequestDTO;
import com.neuroforge.backend.dto.module.ModuleResponseDTO;
import com.neuroforge.backend.dto.module.ModuleUpdateRequestDTO;
import com.neuroforge.backend.entity.Modules;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ModuleMapper {

    @Mapping(target = "moduleId", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Modules toEntity(ModuleRequestDTO dto);

    ModuleResponseDTO toResponseDto(Modules module);

    @Mapping(target = "moduleId", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(ModuleUpdateRequestDTO dto, @MappingTarget Modules module);
}