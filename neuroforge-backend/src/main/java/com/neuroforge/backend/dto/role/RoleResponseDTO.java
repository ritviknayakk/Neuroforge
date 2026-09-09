package com.neuroforge.backend.dto.role;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponseDTO {

    private Integer roleId;
    private String roleName;
    private String description;
    private LocalDateTime createdAt;
}