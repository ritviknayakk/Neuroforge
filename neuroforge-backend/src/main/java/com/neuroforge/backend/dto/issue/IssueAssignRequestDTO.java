package com.neuroforge.backend.dto.issue;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueAssignRequestDTO {

    private Integer assignedToUserId;
}