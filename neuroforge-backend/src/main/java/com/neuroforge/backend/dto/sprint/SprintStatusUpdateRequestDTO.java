package com.neuroforge.backend.dto.sprint;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SprintStatusUpdateRequestDTO {

    @NotBlank(message = "Status is required")
    private String status;
}