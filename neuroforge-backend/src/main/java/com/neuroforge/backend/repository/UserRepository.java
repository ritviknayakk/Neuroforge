package com.neuroforge.backend.repository;

import com.neuroforge.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// @Repository is not needed - Spring Data JPA handles this automatically
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByIsActiveTrue();

    List<User> findByRole_RoleId(Integer roleId);

    Optional<User> findByEmailAndIsActiveTrue(String email);

    long countByIsActiveTrue();
}