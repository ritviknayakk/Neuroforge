package com.neuroforge.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "Deployments", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class Deployments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DeploymentID")
    @EqualsAndHashCode.Include
    private Integer deploymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProjectID", nullable = false)
    private Project project;

    @Column(name = "Version", nullable = false, length = 50)
    private String version;

    @Column(name = "Status", nullable = false, length = 15)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TriggeredByUserID", nullable = false)
    private User triggeredBy;

    @CreatedDate
    @Column(name = "StartedAt", nullable = false, updatable = false)
    private LocalDateTime startedAt;

    @Column(name = "CompletedAt")
    private LocalDateTime completedAt;

    @Column(name = "ErrorLogURL", length = 500)
    private String errorLogUrl;

    @Version
    @Generated(event = { EventType.INSERT, EventType.UPDATE })
    @Column(name = "RowVersion", columnDefinition = "timestamp", insertable = false, updatable = false)
    private byte[] rowVersion;
}