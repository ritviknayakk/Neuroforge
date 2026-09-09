package com.neuroforge.backend.dto.notification;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDTO {

    private Integer notificationId;
    private Integer recipientUserId;
    private String recipientFullName;
    private Integer deploymentId;
    private String deploymentVersion;
    private Integer issueId;
    private String issueTitle;
    private String message;
    private String notificationType;
    private Boolean isRead;
    private LocalDateTime createdAt;
}