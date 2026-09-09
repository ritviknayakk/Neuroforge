package com.neuroforge.backend.repository;

import com.neuroforge.backend.entity.Requirements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RequirementsRepository extends JpaRepository<Requirements, Integer> {

    List<Requirements> findByProject_ProjectId(Integer projectId);

    List<Requirements> findByProject_ProjectIdAndIsArchivedFalse(Integer projectId);

    // FIXED: Changed parameter type from String to Integer
    List<Requirements> findByProject_ProjectIdAndStatus(Integer projectId, String status);

    List<Requirements> findByStatus(String status);

    List<Requirements> findByIsArchivedFalse();

    Optional<Requirements> findByProject_ProjectIdAndRequirementId(Integer projectId, Integer requirementId);

    boolean existsByProject_ProjectIdAndRequirementId(Integer projectId, Integer requirementId);

    @Query("SELECT r FROM Requirements r WHERE r.project.projectId = :projectId AND r.status IN :statuses")
    List<Requirements> findByProjectIdAndStatuses(@Param("projectId") Integer projectId,
            @Param("statuses") List<String> statuses);

    @Query("SELECT r FROM Requirements r WHERE r.requirementText LIKE %:keyword% AND r.isArchived = false")
    List<Requirements> searchActiveRequirements(@Param("keyword") String keyword);

    long countByProject_ProjectId(Integer projectId);

    long countByProject_ProjectIdAndIsArchivedFalse(Integer projectId);

    long countByProject_ProjectIdAndStatus(Integer projectId, String status);
}