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
@Table(name = "Requirements", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class Requirements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RequirementID")
    @EqualsAndHashCode.Include
    private Integer requirementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProjectID", nullable = false)
    private Project project;

    @Column(name = "RequirementText", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String requirementText;

    @Column(name = "Status", nullable = false, length = 20)
    private String status;

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