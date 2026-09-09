package com.neuroforge.backend.dto.testcase;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestCaseReviewRequestDTO {

    @NotNull(message = "Reviewed by user ID is required")
    private Integer reviewedByUserId;

    @NotBlank(message = "Status is required")
    private String status; // Reviewed or Approved
}