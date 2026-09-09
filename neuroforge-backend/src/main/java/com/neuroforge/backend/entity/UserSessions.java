package com.neuroforge.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "UserSessions", schema = "dbo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSessions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SessionID")
    private Long sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @Column(name = "TokenHash", nullable = false, length = 255)
    private String tokenHash;

    @Column(name = "IssuedAt", nullable = false, updatable = false)
    private LocalDateTime issuedAt;

    @Column(name = "ExpiresAt", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "RevokedAt")
    private LocalDateTime revokedAt;

    @Column(name = "IPAddress", length = 45)
    private String ipAddress;
}