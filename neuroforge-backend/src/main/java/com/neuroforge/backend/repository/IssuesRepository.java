package com.neuroforge.backend.repository;

import com.neuroforge.backend.entity.Issues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IssuesRepository extends JpaRepository<Issues, Integer> {

    List<Issues> findByProject_ProjectId(Integer projectId);

    List<Issues> findByStatus(String status);

    List<Issues> findBySeverity(String severity);

    List<Issues> findByAssignedTo_UserId(Integer userId);

    List<Issues> findByReportedBy_UserId(Integer userId);

    List<Issues> findByProject_ProjectIdAndIsArchivedFalse(Integer projectId);

    List<Issues> findByProject_ProjectIdAndStatus(Integer projectId, String status);

    List<Issues> findByAssignedTo_UserIdAndStatus(Integer userId, String status);

    Optional<Issues> findByProject_ProjectIdAndIssueId(Integer projectId, Integer issueId);

    @Query("SELECT i FROM Issues i WHERE i.project.projectId = :projectId AND i.status IN :statuses")
    List<Issues> findByProjectIdAndStatuses(@Param("projectId") Integer projectId,
            @Param("statuses") List<String> statuses);

    @Query("SELECT i FROM Issues i WHERE i.title LIKE %:keyword% OR i.description LIKE %:keyword%")
    List<Issues> searchIssues(@Param("keyword") String keyword);

    @Query("SELECT i FROM Issues i WHERE i.assignedTo.userId = :userId AND i.status != 'Closed'")
    List<Issues> findOpenIssuesAssignedToUser(@Param("userId") Integer userId);

    long countByProject_ProjectId(Integer projectId);

    long countByProject_ProjectIdAndStatus(Integer projectId, String status);

    long countByStatus(String status);

    long countBySeverity(String severity);
}