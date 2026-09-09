package com.neuroforge.backend.dto.testrun;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestRunRequestDTO {

    @NotNull(message = "Test case ID is required")
    private Integer testCaseId;

    @NotBlank(message = "Execution type is required")
    private String executionType; // Manual or Automated

    @NotBlank(message = "Result is required")
    private String result; // Pass, Fail, or Blocked

    private Integer executedByUserId;

    private String notes;
}