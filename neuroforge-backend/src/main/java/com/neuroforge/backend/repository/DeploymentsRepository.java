package com.neuroforge.backend.repository;

import com.neuroforge.backend.entity.Deployments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DeploymentsRepository extends JpaRepository<Deployments, Integer> {

    List<Deployments> findByProject_ProjectId(Integer projectId);

    List<Deployments> findByStatus(String status);

    List<Deployments> findByTriggeredBy_UserId(Integer userId);

    List<Deployments> findByProject_ProjectIdAndStatus(Integer projectId, String status);

    Optional<Deployments> findByProject_ProjectIdAndVersion(Integer projectId, String version);

    @Query("SELECT d FROM Deployments d WHERE d.project.projectId = :projectId AND d.status = 'Successful' ORDER BY d.startedAt DESC")
    List<Deployments> findRecentSuccessfulDeployments(@Param("projectId") Integer projectId);

    @Query("SELECT d FROM Deployments d WHERE d.project.projectId = :projectId AND d.status = 'Failed' ORDER BY d.startedAt DESC")
    List<Deployments> findRecentFailedDeployments(@Param("projectId") Integer projectId);

    @Query("SELECT d FROM Deployments d WHERE d.startedAt BETWEEN :startDate AND :endDate")
    List<Deployments> findDeploymentsBetweenDates(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT d FROM Deployments d WHERE d.status = 'InProgress' AND d.startedAt < :timeout")
    List<Deployments> findStalledDeployments(@Param("timeout") LocalDateTime timeout);

    long countByProject_ProjectId(Integer projectId);

    long countByProject_ProjectIdAndStatus(Integer projectId, String status);

    long countByStatus(String status);
}