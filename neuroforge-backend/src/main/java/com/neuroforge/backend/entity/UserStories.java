package com.neuroforge.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "UserStories", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class UserStories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserStoryID")
    @EqualsAndHashCode.Include
    private Integer userStoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RequirementID", nullable = false)
    private Requirements requirement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SprintID")
    private Sprints sprint;

    @Column(name = "ActorType", length = 100)
    private String actorType;

    @Column(name = "Goal", length = 500)
    private String goal;

    @Column(name = "Reason", length = 500)
    private String reason;

    @Column(name = "StoryText", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String storyText;

    @Column(name = "Priority", nullable = false, length = 10)
    private String priority;

    @Column(name = "Status", nullable = false, length = 20)
    private String status;

    @Column(name = "IsAIGenerated", nullable = false)
    private Boolean isAIGenerated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AcceptedByUserID")
    private User acceptedBy;

    @Column(name = "AcceptedAt")
    private LocalDateTime acceptedAt;

    @Column(name = "IsArchived", nullable = false)
    private Boolean isArchived;

    @CreatedBy
    @Column(name = "CreatedByUserID", nullable = false, updatable = false)
    private Integer createdByUserId;

    @CreatedDate
    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "ModifiedByUserID")
    private Integer modifiedByUserId;

    @LastModifiedDate
    @Column(name = "ModifiedAt")
    private LocalDateTime modifiedAt;

    @Version
    @Generated(event = { EventType.INSERT, EventType.UPDATE })
    @Column(name = "RowVersion", columnDefinition = "timestamp", insertable = false, updatable = false)
    private byte[] rowVersion;
}