package com.neuroforge.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "CodeSuggestions", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class CodeSuggestions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CodeSuggestionID")
    @EqualsAndHashCode.Include
    private Integer codeSuggestionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TaskID", nullable = false)
    private Tasks task;

    @Column(name = "SuggestedCode", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String suggestedCode;

    @Column(name = "Language", length = 50)
    private String language;

    @Column(name = "IsAIGenerated", nullable = false)
    private Boolean isAIGenerated;

    @Column(name = "Status", nullable = false, length = 20)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DecidedByUserID")
    private User decidedBy;

    @Column(name = "DecidedAt")
    private LocalDateTime decidedAt;

    @CreatedDate
    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime createdAt;
}