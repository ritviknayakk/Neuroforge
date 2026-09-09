package com.neuroforge.backend.repository;

import com.neuroforge.backend.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByUser_UserId(Integer userId);

    List<AuditLog> findByAction(String action);

    List<AuditLog> findByEntityType(String entityType);

    List<AuditLog> findByEntityTypeAndEntityId(String entityType, Integer entityId);

    @Query("SELECT a FROM AuditLog a WHERE a.user.userId = :userId ORDER BY a.createdAt DESC")
    List<AuditLog> findRecentByUser(@Param("userId") Integer userId);

    @Query("SELECT a FROM AuditLog a WHERE a.createdAt BETWEEN :startDate AND :endDate ORDER BY a.createdAt DESC")
    List<AuditLog> findBetweenDates(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT a FROM AuditLog a WHERE a.action LIKE %:action% AND a.entityType = :entityType")
    List<AuditLog> searchByActionAndEntityType(@Param("action") String action, @Param("entityType") String entityType);

    @Query("SELECT a FROM AuditLog a WHERE a.user.userId = :userId AND a.action = :action AND a.entityType = :entityType AND a.entityId = :entityId")
    List<AuditLog> findByUserActionAndEntity(@Param("userId") Integer userId, @Param("action") String action,
            @Param("entityType") String entityType, @Param("entityId") Integer entityId);

    @Query("SELECT DISTINCT a.action FROM AuditLog a")
    List<String> findAllActions();

    @Query("SELECT DISTINCT a.entityType FROM AuditLog a")
    List<String> findAllEntityTypes();

    long countByUser_UserId(Integer userId);

    long countByEntityType(String entityType);

    long countByAction(String action);
}