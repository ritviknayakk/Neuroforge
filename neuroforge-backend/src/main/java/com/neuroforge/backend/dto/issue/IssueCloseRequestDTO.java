package com.neuroforge.backend.dto.issue;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueCloseRequestDTO {

    @NotNull(message = "Closed by user ID is required")
    private Integer closedByUserId;
}