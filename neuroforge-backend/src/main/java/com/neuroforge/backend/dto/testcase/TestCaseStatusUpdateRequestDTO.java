package com.neuroforge.backend.dto.testcase;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestCaseStatusUpdateRequestDTO {

    @NotBlank(message = "Status is required")
    private String status;
}