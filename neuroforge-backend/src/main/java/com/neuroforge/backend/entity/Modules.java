package com.neuroforge.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "Modules", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class Modules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ModuleID")
    @EqualsAndHashCode.Include
    private Integer moduleId;

    @Column(name = "ModuleName", nullable = false, unique = true, length = 50)
    private String moduleName;

    @Column(name = "Description", length = 255)
    private String description;

    @CreatedBy
    @Column(name = "CreatedByUserID", updatable = false)
    private Integer createdByUserId;

    @CreatedDate
    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}