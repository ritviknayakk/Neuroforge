package com.neuroforge.backend.dto.designsuggestion;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DesignSuggestionAcceptRequestDTO {

    @NotNull(message = "Accepted by user ID is required")
    private Integer acceptedByUserId;
}