package com.neuroforge.backend.dto.notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequestDTO {

    @NotNull(message = "Recipient user ID is required")
    private Integer recipientUserId;

    private Integer deploymentId;

    private Integer issueId;

    @NotBlank(message = "Message is required")
    private String message;

    @NotBlank(message = "Notification type is required")
    private String notificationType; // DeploymentSuccess, DeploymentFailure, IssueAssigned, General

    @Builder.Default
    private Boolean isRead = false;
}