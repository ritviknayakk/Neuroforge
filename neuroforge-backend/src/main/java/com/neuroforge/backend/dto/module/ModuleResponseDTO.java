package com.neuroforge.backend.dto.module;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleResponseDTO {

    private Integer moduleId;
    private String moduleName;
    private String description;
    private Integer createdByUserId;
    private LocalDateTime createdAt;
}