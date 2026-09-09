package com.neuroforge.backend.service;

import com.neuroforge.backend.dto.module.ModuleRequestDTO;
import com.neuroforge.backend.dto.module.ModuleResponseDTO;
import com.neuroforge.backend.dto.module.ModuleUpdateRequestDTO;
import com.neuroforge.backend.entity.Modules;
import com.neuroforge.backend.mapper.ModuleMapper;
import com.neuroforge.backend.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final ModuleMapper moduleMapper;

    @Transactional(readOnly = true)
    public List<ModuleResponseDTO> getAllModules() {
        return moduleRepository.findAll().stream()
                .map(moduleMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ModuleResponseDTO getModuleById(Integer id) {
        Modules module = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found with id: " + id));
        return moduleMapper.toResponseDto(module);
    }

    @Transactional(readOnly = true)
    public ModuleResponseDTO getModuleByName(String moduleName) {
        Modules module = moduleRepository.findByModuleNameIgnoreCase(moduleName)
                .orElseThrow(() -> new RuntimeException("Module not found with name: " + moduleName));
        return moduleMapper.toResponseDto(module);
    }

    @Transactional(readOnly = true)
    public List<ModuleResponseDTO> searchModulesByName(String moduleName) {
        return moduleRepository.findByModuleNameContainingIgnoreCase(moduleName).stream()
                .map(moduleMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ModuleResponseDTO> getModulesByCreator(Integer userId) {
        return moduleRepository.findByCreatedByUserId(userId).stream()
                .map(moduleMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public ModuleResponseDTO createModule(ModuleRequestDTO request) {
        // Check if module name already exists
        if (moduleRepository.existsByModuleNameIgnoreCase(request.getModuleName())) {
            throw new RuntimeException("Module with name '" + request.getModuleName() + "' already exists");
        }

        Modules module = moduleMapper.toEntity(request);
        // Capitalize the first letter of module name
        module.setModuleName(capitalizeFirstLetter(request.getModuleName()));

        Modules savedModule = moduleRepository.save(module);
        return moduleMapper.toResponseDto(savedModule);
    }

    public ModuleResponseDTO updateModule(Integer id, ModuleUpdateRequestDTO request) {
        Modules module = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found with id: " + id));

        // Check if module name is being changed and if it already exists
        if (request.getModuleName() != null && !request.getModuleName().equalsIgnoreCase(module.getModuleName())) {
            if (moduleRepository.existsByModuleNameIgnoreCase(request.getModuleName())) {
                throw new RuntimeException("Module with name '" + request.getModuleName() + "' already exists");
            }
            // Capitalize the module name
            request.setModuleName(capitalizeFirstLetter(request.getModuleName()));
        }

        moduleMapper.updateEntityFromDto(request, module);
        Modules updatedModule = moduleRepository.save(module);
        return moduleMapper.toResponseDto(updatedModule);
    }

    public void deleteModule(Integer id) {
        if (!moduleRepository.existsById(id)) {
            throw new RuntimeException("Module not found with id: " + id);
        }
        moduleRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long getModuleCount() {
        return moduleRepository.count();
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}