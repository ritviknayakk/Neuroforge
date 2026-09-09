package com.neuroforge.backend.dto.codesuggestion;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeSuggestionUpdateRequestDTO {

    private String suggestedCode;
    private String language;
    private Boolean isAIGenerated;
    private String status;
}