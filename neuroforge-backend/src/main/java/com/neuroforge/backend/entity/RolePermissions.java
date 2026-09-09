package com.neuroforge.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RolePermissions", schema = "dbo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RolePermissionsId.class)
public class RolePermissions {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RoleID", nullable = false)
    private Role role;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ModuleID", nullable = false)
    private Modules module;

    @Column(name = "CanView", nullable = false)
    @Builder.Default
    private Boolean canView = false;

    @Column(name = "CanCreate", nullable = false)
    @Builder.Default
    private Boolean canCreate = false;

    @Column(name = "CanEdit", nullable = false)
    @Builder.Default
    private Boolean canEdit = false;

    @Column(name = "CanDelete", nullable = false)
    @Builder.Default
    private Boolean canDelete = false;
}