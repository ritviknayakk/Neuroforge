package com.neuroforge.backend.dto.userstory;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStoryStatusUpdateRequestDTO {

    @NotBlank(message = "Status is required")
    private String status;
}