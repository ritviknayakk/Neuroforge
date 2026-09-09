package com.neuroforge.backend.dto.usersession;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull; // ← Add this import
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSessionRequestDTO {

    @NotNull(message = "User ID is required")
    private Integer userId;

    @NotBlank(message = "Token is required")
    private String token;

    @NotNull(message = "Expires at is required")
    private Long expiresInSeconds;

    private String ipAddress;
}