package com.neuroforge.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "Issues", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class Issues {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IssueID")
    @EqualsAndHashCode.Include
    private Integer issueId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProjectID", nullable = false)
    private Project project;

    @Column(name = "Title", nullable = false, length = 200)
    private String title;

    @Column(name = "Description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "Severity", nullable = false, length = 10)
    private String severity;

    @Column(name = "Status", nullable = false, length = 15)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ReportedByUserID", nullable = false)
    private User reportedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AssignedToUserID")
    private User assignedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RelatedTestRunID")
    private TestRuns relatedTestRun;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RelatedTaskID")
    private Tasks relatedTask;

    @Column(name = "IsArchived", nullable = false)
    private Boolean isArchived;

    @CreatedDate
    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "ModifiedByUserID")
    private Integer modifiedByUserId;

    @LastModifiedDate
    @Column(name = "ModifiedAt")
    private LocalDateTime modifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ClosedByUserID")
    private User closedBy;

    @Column(name = "ClosedAt")
    private LocalDateTime closedAt;

    @Version
    @Generated(event = { EventType.INSERT, EventType.UPDATE })
    @Column(name = "RowVersion", columnDefinition = "timestamp", insertable = false, updatable = false)
    private byte[] rowVersion;
}