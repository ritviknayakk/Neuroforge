package com.neuroforge.backend.dto.userstory;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStoryAcceptRequestDTO {

    @NotNull(message = "Accepted by user ID is required")
    private Integer acceptedByUserId;
}