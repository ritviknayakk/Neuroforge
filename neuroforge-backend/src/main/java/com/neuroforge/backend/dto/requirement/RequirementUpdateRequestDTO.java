package com.neuroforge.backend.dto.requirement;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequirementUpdateRequestDTO {

    private String requirementText;
    private String status;
    private Boolean isArchived;
}