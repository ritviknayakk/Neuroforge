package com.neuroforge.backend.repository;

import com.neuroforge.backend.entity.TestCases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TestCasesRepository extends JpaRepository<TestCases, Integer> {

    List<TestCases> findByProject_ProjectId(Integer projectId);

    List<TestCases> findByUserStory_UserStoryId(Integer userStoryId);

    List<TestCases> findByProject_ProjectIdAndIsArchivedFalse(Integer projectId);

    List<TestCases> findByUserStory_UserStoryIdAndIsArchivedFalse(Integer userStoryId);

    List<TestCases> findByStatus(String status);

    List<TestCases> findByIsAIGeneratedTrue();

    List<TestCases> findByIsAIGeneratedFalse();

    Optional<TestCases> findByProject_ProjectIdAndTestCaseId(Integer projectId, Integer testCaseId);

    @Query("SELECT tc FROM TestCases tc WHERE tc.project.projectId = :projectId AND tc.status IN :statuses")
    List<TestCases> findByProjectIdAndStatuses(@Param("projectId") Integer projectId,
            @Param("statuses") List<String> statuses);

    @Query("SELECT tc FROM TestCases tc WHERE tc.featureName LIKE %:keyword% AND tc.isArchived = false")
    List<TestCases> searchActiveTestCases(@Param("keyword") String keyword);

    long countByProject_ProjectId(Integer projectId);

    long countByProject_ProjectIdAndIsArchivedFalse(Integer projectId);

    long countByUserStory_UserStoryId(Integer userStoryId);

    long countByStatus(String status);
}