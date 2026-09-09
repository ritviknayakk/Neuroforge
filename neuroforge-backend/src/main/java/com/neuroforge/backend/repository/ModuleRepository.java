package com.neuroforge.backend.repository;

import com.neuroforge.backend.entity.Modules;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ModuleRepository extends JpaRepository<Modules, Integer> {

    Optional<Modules> findByModuleName(String moduleName);

    Optional<Modules> findByModuleNameIgnoreCase(String moduleName);

    boolean existsByModuleName(String moduleName);

    boolean existsByModuleNameIgnoreCase(String moduleName);

    List<Modules> findByModuleNameContainingIgnoreCase(String moduleName);

    List<Modules> findByCreatedByUserId(Integer userId);

    long count();
}