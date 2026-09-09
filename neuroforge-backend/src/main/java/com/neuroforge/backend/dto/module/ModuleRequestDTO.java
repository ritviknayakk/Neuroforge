package com.neuroforge.backend.dto.module;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleRequestDTO {

    @NotBlank(message = "Module name is required")
    @Size(max = 50, message = "Module name must not exceed 50 characters")
    private String moduleName;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;
}