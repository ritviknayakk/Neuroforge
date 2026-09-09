package com.neuroforge.backend.repository;

import com.neuroforge.backend.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// @Repository is not needed - Spring Data JPA handles this automatically
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Integer> {

    // Basic queries
    List<ProjectMember> findByProject_ProjectId(Integer projectId);

    List<ProjectMember> findByUser_UserId(Integer userId);

    List<ProjectMember> findByProject_ProjectIdAndRemovedAtIsNull(Integer projectId);

    // Active member checks
    boolean existsByProject_ProjectIdAndUser_UserIdAndRemovedAtIsNull(Integer projectId, Integer userId);

    Optional<ProjectMember> findByProject_ProjectIdAndUser_UserIdAndRemovedAtIsNull(Integer projectId, Integer userId);

    // Safe way to find the most recently removed member
    Optional<ProjectMember> findFirstByProject_ProjectIdAndUser_UserIdAndRemovedAtIsNotNullOrderByRemovedAtDesc(
            Integer projectId, Integer userId);

    // Get all removed members for a project (for reporting/audit purposes)
    @Query("SELECT pm FROM ProjectMember pm WHERE pm.project.projectId = :projectId AND pm.removedAt IS NOT NULL")
    List<ProjectMember> findRemovedMembers(@Param("projectId") Integer projectId);

    // Get active members count for a project
    long countByProject_ProjectIdAndRemovedAtIsNull(Integer projectId);
}