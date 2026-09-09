package com.neuroforge.backend.dto.codesuggestion;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeSuggestionResponseDTO {

    private Integer codeSuggestionId;
    private Integer taskId;
    private String taskTitle;
    private String suggestedCode;
    private String language;
    private Boolean isAIGenerated;
    private String status;
    private Integer decidedByUserId;
    private String decidedByFullName;
    private LocalDateTime decidedAt;
    private LocalDateTime createdAt;
}