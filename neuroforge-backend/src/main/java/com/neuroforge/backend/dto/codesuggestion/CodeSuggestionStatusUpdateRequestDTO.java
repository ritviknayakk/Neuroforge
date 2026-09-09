package com.neuroforge.backend.dto.codesuggestion;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeSuggestionStatusUpdateRequestDTO {

    @NotBlank(message = "Status is required")
    private String status;
}