package com.neuroforge.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "ProjectMembers", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProjectMemberID")
    @EqualsAndHashCode.Include
    private Integer projectMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProjectID", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProjectRoleID", nullable = false)
    private Role projectRole;

    @Column(name = "AddedByUserID", nullable = false)
    private Integer addedByUserId;

    @CreatedDate
    @Column(name = "JoinedAt", updatable = false)
    private LocalDateTime joinedAt;

    @Column(name = "RemovedByUserID")
    private Integer removedByUserId;

    @Column(name = "RemovedAt")
    private LocalDateTime removedAt;
}