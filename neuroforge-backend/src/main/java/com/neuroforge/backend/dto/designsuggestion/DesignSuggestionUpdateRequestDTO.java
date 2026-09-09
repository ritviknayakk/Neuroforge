package com.neuroforge.backend.dto.designsuggestion;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DesignSuggestionUpdateRequestDTO {

    private String title;
    private String componentsDescription;
    private Boolean isAIGenerated;
    private String status;
}