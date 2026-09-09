package com.neuroforge.backend.dto.user;

import lombok.*;

import java.time.LocalDateTime;

/** No passwordHash field — it must never leave the service layer. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private Integer userId;
    private String fullName;
    private String email;
    private Integer roleId;
    private String roleName;
    private Boolean isActive;
    private Boolean isArchived;
    private LocalDateTime lastLoginAt;
    private Integer createdByUserId;
    private LocalDateTime createdAt;
    private Integer modifiedByUserId;
    private LocalDateTime modifiedAt;
}