package com.neuroforge.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "TestRuns", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class TestRuns {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TestRunID")
    @EqualsAndHashCode.Include
    private Integer testRunId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TestCaseID", nullable = false)
    private TestCases testCase;

    @Column(name = "ExecutionType", nullable = false, length = 10)
    private String executionType;

    @Column(name = "Result", nullable = false, length = 10)
    private String result;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ExecutedByUserID")
    private User executedBy;

    @Column(name = "Notes", columnDefinition = "NVARCHAR(MAX)")
    private String notes;

    @CreatedDate
    @Column(name = "ExecutedAt", updatable = false)
    private LocalDateTime executedAt;
}