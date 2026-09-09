package com.neuroforge.backend.dto.usersession;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSessionRevokeRequestDTO {

    @NotBlank(message = "Token is required")
    private String token;
}