package com.neuroforge.backend.dto.notification;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationMarkReadRequestDTO {

    private Boolean isRead;
}