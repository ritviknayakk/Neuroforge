package com.neuroforge.backend.repository;

import com.neuroforge.backend.entity.Sprints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SprintsRepository extends JpaRepository<Sprints, Integer> {

    List<Sprints> findByProject_ProjectId(Integer projectId);

    List<Sprints> findByProject_ProjectIdAndStatus(Integer projectId, String status);

    List<Sprints> findByStatus(String status);

    Optional<Sprints> findByProject_ProjectIdAndSprintName(Integer projectId, String sprintName);

    @Query("SELECT s FROM Sprints s WHERE s.project.projectId = :projectId AND s.startDate <= :date AND s.endDate >= :date")
    List<Sprints> findActiveSprintsOnDate(@Param("projectId") Integer projectId, @Param("date") LocalDate date);

    @Query("SELECT s FROM Sprints s WHERE s.project.projectId = :projectId AND s.status = 'Active' AND s.endDate < :date")
    List<Sprints> findOverdueActiveSprints(@Param("projectId") Integer projectId, @Param("date") LocalDate date);

    @Query("SELECT s FROM Sprints s WHERE s.endDate < :date AND s.status != 'Completed' AND s.status != 'Cancelled'")
    List<Sprints> findOverdueSprints(@Param("date") LocalDate date);

    long countByProject_ProjectId(Integer projectId);

    long countByProject_ProjectIdAndStatus(Integer projectId, String status);

    boolean existsByProject_ProjectIdAndSprintNameIgnoreCase(Integer projectId, String sprintName);
}