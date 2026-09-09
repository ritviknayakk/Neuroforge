package com.neuroforge.backend.dto.project;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectUpdateRequestDTO {

    @Size(max = 150, message = "Project name must not exceed 150 characters")
    private String projectName;

    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;

    private String status;
}