package com.neuroforge.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "Notifications", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NotificationID")
    @EqualsAndHashCode.Include
    private Integer notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RecipientUserID", nullable = false)
    private User recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DeploymentID")
    private Deployments deployment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IssueID")
    private Issues issue;

    @Column(name = "Message", nullable = false, length = 500)
    private String message;

    @Column(name = "NotificationType", nullable = false, length = 30)
    private String notificationType;

    @Column(name = "IsRead", nullable = false)
    private Boolean isRead;

    @CreatedDate
    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime createdAt;
}