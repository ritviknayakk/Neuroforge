package com.neuroforge.backend.dto.user;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * "password" is the plaintext password from the client — it is hashed in
 * the service layer (deliverable #6) before ever touching the entity or
 * the User.passwordHash column. Never log or return this DTO as-is.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequestDTO {

    @NotBlank
    @Size(max = 100)
    private String fullName;

    @NotBlank
    @Email
    @Size(max = 256)
    private String email;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    @NotNull
    private Integer roleId;
}