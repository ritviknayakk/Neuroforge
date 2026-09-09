package com.neuroforge.backend.dto.testrun;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestRunUpdateRequestDTO {

    private String executionType;
    private String result;
    private Integer executedByUserId;
    private String notes;
}