package com.neuroforge.backend.dto.designsuggestion;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DesignSuggestionStatusUpdateRequestDTO {

    @NotBlank(message = "Status is required")
    private String status;
}