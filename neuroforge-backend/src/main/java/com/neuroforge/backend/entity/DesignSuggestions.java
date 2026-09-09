package com.neuroforge.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "DesignSuggestions", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class DesignSuggestions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DesignSuggestionID")
    @EqualsAndHashCode.Include
    private Integer designSuggestionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProjectID", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RequirementID")
    private Requirements requirement;

    @Column(name = "Title", length = 200)
    private String title;

    @Column(name = "ComponentsDescription", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String componentsDescription;

    @Column(name = "IsAIGenerated", nullable = false)
    private Boolean isAIGenerated;

    @Column(name = "Status", nullable = false, length = 20)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AcceptedByUserID")
    private User acceptedBy;

    @Column(name = "AcceptedAt")
    private LocalDateTime acceptedAt;

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
}