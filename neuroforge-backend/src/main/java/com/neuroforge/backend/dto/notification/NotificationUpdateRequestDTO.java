package com.neuroforge.backend.dto.notification;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationUpdateRequestDTO {

    private String message;
    private String notificationType;
    private Boolean isRead;
}