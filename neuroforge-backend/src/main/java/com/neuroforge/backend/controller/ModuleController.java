package com.neuroforge.backend.controller;

import com.neuroforge.backend.dto.module.ModuleRequestDTO;
import com.neuroforge.backend.dto.module.ModuleResponseDTO;
import com.neuroforge.backend.dto.module.ModuleUpdateRequestDTO;
import com.neuroforge.backend.service.ModuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/modules")
@RequiredArgsConstructor
@Tag(name = "Modules", description = "Module management endpoints")
public class ModuleController {

    private final ModuleService moduleService;

    @GetMapping
    @Operation(summary = "Get all modules", description = "Returns a list of all modules")
    public List<ModuleResponseDTO> getAllModules() {
        return moduleService.getAllModules();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get module by ID", description = "Returns a single module by its ID")
    public ModuleResponseDTO getModuleById(@PathVariable Integer id) {
        return moduleService.getModuleById(id);
    }

    @GetMapping("/name/{moduleName}")
    @Operation(summary = "Get module by name", description = "Returns a single module by its name (case insensitive)")
    public ModuleResponseDTO getModuleByName(@PathVariable String moduleName) {
        return moduleService.getModuleByName(moduleName);
    }

    @GetMapping("/search")
    @Operation(summary = "Search modules by name", description = "Returns modules matching the search term (case insensitive)")
    public List<ModuleResponseDTO> searchModulesByName(@RequestParam String query) {
        return moduleService.searchModulesByName(query);
    }

    @GetMapping("/creator/{userId}")
    @Operation(summary = "Get modules by creator", description = "Returns modules created by a specific user")
    public List<ModuleResponseDTO> getModulesByCreator(@PathVariable Integer userId) {
        return moduleService.getModulesByCreator(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new module", description = "Creates a new module with the provided details")
    public ModuleResponseDTO createModule(@Valid @RequestBody ModuleRequestDTO request) {
        return moduleService.createModule(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing module", description = "Updates an existing module's details")
    public ModuleResponseDTO updateModule(@PathVariable Integer id,
            @Valid @RequestBody ModuleUpdateRequestDTO request) {
        return moduleService.updateModule(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a module", description = "Deletes a module by its ID")
    public void deleteModule(@PathVariable Integer id) {
        moduleService.deleteModule(id);
    }

    @GetMapping("/count")
    @Operation(summary = "Get total number of modules", description = "Returns the total count of modules in the system")
    public long getModuleCount() {
        return moduleService.getModuleCount();
    }
}