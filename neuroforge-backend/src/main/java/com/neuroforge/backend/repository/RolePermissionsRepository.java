package com.neuroforge.backend.repository;

import com.neuroforge.backend.entity.RolePermissions;
import com.neuroforge.backend.entity.RolePermissionsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RolePermissionsRepository extends JpaRepository<RolePermissions, RolePermissionsId> {

    List<RolePermissions> findByRole_RoleId(Integer roleId);

    List<RolePermissions> findByModule_ModuleId(Integer moduleId);

    Optional<RolePermissions> findByRole_RoleIdAndModule_ModuleId(Integer roleId, Integer moduleId);

    List<RolePermissions> findByRole_RoleIdAndCanViewTrue(Integer roleId);

    List<RolePermissions> findByRole_RoleIdAndCanCreateTrue(Integer roleId);

    List<RolePermissions> findByRole_RoleIdAndCanEditTrue(Integer roleId);

    List<RolePermissions> findByRole_RoleIdAndCanDeleteTrue(Integer roleId);

    boolean existsByRole_RoleIdAndModule_ModuleId(Integer roleId, Integer moduleId);

    @Modifying
    @Query("DELETE FROM RolePermissions rp WHERE rp.role.roleId = :roleId")
    void deleteByRoleId(@Param("roleId") Integer roleId);

    @Modifying
    @Query("DELETE FROM RolePermissions rp WHERE rp.module.moduleId = :moduleId")
    void deleteByModuleId(@Param("moduleId") Integer moduleId);

    @Query("SELECT rp FROM RolePermissions rp WHERE rp.role.roleId = :roleId AND rp.module.moduleId IN :moduleIds")
    List<RolePermissions> findByRoleIdAndModuleIds(@Param("roleId") Integer roleId,
            @Param("moduleIds") List<Integer> moduleIds);
}