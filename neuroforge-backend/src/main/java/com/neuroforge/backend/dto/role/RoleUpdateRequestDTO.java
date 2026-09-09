package com.neuroforge.backend.dto.role;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleUpdateRequestDTO {

    @Size(max = 50, message = "Role name must not exceed 50 characters")
    private String roleName;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;
}