package com.neuroforge.backend.dto.codesuggestion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeSuggestionRequestDTO {

    @NotNull(message = "Task ID is required")
    private Integer taskId;

    @NotBlank(message = "Suggested code is required")
    private String suggestedCode;

    private String language;

    @Builder.Default
    private Boolean isAIGenerated = true;

    @Builder.Default
    private String status = "Pending";
}