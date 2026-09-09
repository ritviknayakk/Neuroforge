package com.neuroforge.backend.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * No email or password here on purpose — those go through dedicated
 * change-email / change-password flows (deliverable #6) with their own
 * verification, not a generic PATCH-everything endpoint.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequestDTO {

    @Size(max = 100)
    private String fullName;

    private Integer roleId;

    @NotNull
    private Boolean isActive;
}