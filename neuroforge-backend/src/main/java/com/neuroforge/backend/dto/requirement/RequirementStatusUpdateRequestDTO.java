package com.neuroforge.backend.dto.requirement;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequirementStatusUpdateRequestDTO {

    @NotBlank(message = "Status is required")
    private String status;
}