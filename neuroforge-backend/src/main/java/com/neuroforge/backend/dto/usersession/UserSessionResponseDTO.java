package com.neuroforge.backend.dto.usersession;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSessionResponseDTO {

    private Long sessionId;
    private Integer userId;
    private String userFullName;
    private String userEmail;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime revokedAt;
    private String ipAddress;
    private Boolean isActive;
    private Boolean isExpired;
}