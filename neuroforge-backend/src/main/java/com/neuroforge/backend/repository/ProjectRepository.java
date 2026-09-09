package com.neuroforge.backend.repository;

import com.neuroforge.backend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// @Repository is not needed - Spring Data JPA handles this automatically
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    // Find by project name (exact match)
    Optional<Project> findByProjectName(String projectName);

    // Find by project name containing (case insensitive)
    List<Project> findByProjectNameContainingIgnoreCase(String projectName);

    // Find by status
    List<Project> findByStatus(String status);

    // Find by status and archivedAt null (active projects)
    List<Project> findByStatusAndArchivedAtIsNull(String status);

    // Find by created by user
    List<Project> findByCreatedByUserId(Integer userId);

    // Find active projects created by a user
    List<Project> findByCreatedByUserIdAndStatusAndArchivedAtIsNull(Integer userId, String status);

    // Check if project exists by name (ignore case)
    boolean existsByProjectNameIgnoreCase(String projectName);

    // Find projects by status with pagination (if needed later)
    // Page<Project> findByStatus(String status, Pageable pageable);

    // Count projects by status
    long countByStatus(String status);

    // Find recently created projects (limit 10)
    @Query("SELECT p FROM Project p WHERE p.archivedAt IS NULL ORDER BY p.createdAt DESC")
    List<Project> findRecentActiveProjects();

    // Find projects with descriptions containing a keyword
    @Query("SELECT p FROM Project p WHERE p.description LIKE %:keyword% AND p.archivedAt IS NULL")
    List<Project> findActiveProjectsWithDescriptionContaining(@Param("keyword") String keyword);
}