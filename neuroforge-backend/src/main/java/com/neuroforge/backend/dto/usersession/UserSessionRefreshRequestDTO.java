package com.neuroforge.backend.dto.usersession;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull; // ← Add this import
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSessionRefreshRequestDTO {

    @NotBlank(message = "Token is required")
    private String token;

    @NotNull(message = "New expiration duration is required")
    private Long newExpiresInSeconds;
}