package com.neuroforge.backend.repository;

import com.neuroforge.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByRoleName(String roleName);

    Optional<Role> findByRoleNameIgnoreCase(String roleName);

    boolean existsByRoleName(String roleName);

    boolean existsByRoleNameIgnoreCase(String roleName);

    List<Role> findByRoleNameContainingIgnoreCase(String roleName);

    long count();
}