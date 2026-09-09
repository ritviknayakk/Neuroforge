package com.neuroforge.backend.dto.issue;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueStatusUpdateRequestDTO {

    @NotBlank(message = "Status is required")
    private String status; // Open, InProgress, Closed
}