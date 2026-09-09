package com.neuroforge.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "AuditLog", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AuditLogID")
    @EqualsAndHashCode.Include
    private Long auditLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID")
    private User user;

    @Column(name = "Action", nullable = false, length = 100)
    private String action;

    @Column(name = "EntityType", nullable = false, length = 50)
    private String entityType;

    @Column(name = "EntityID")
    private Integer entityId;

    @Column(name = "Details", columnDefinition = "NVARCHAR(MAX)")
    private String details;

    @Column(name = "IPAddress", length = 45)
    private String ipAddress;

    @CreatedDate
    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime createdAt;
}