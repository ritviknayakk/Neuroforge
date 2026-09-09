package com.neuroforge.backend.dto.codesuggestion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeSuggestionDecisionRequestDTO {

    @NotNull(message = "Decided by user ID is required")
    private Integer decidedByUserId;

    @NotBlank(message = "Status is required")
    private String status; // Accepted, Dismissed, or Edited
}