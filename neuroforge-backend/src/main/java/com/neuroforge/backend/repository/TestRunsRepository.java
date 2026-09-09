package com.neuroforge.backend.repository;

import com.neuroforge.backend.entity.TestRuns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TestRunsRepository extends JpaRepository<TestRuns, Integer> {

    List<TestRuns> findByTestCase_TestCaseId(Integer testCaseId);

    List<TestRuns> findByExecutedBy_UserId(Integer userId);

    List<TestRuns> findByResult(String result);

    List<TestRuns> findByExecutionType(String executionType);

    Optional<TestRuns> findByTestCase_TestCaseIdAndTestRunId(Integer testCaseId, Integer testRunId);

    @Query("SELECT tr FROM TestRuns tr WHERE tr.testCase.testCaseId = :testCaseId ORDER BY tr.executedAt DESC")
    List<TestRuns> findLatestTestRunsForTestCase(@Param("testCaseId") Integer testCaseId);

    @Query("SELECT tr FROM TestRuns tr WHERE tr.executedAt BETWEEN :startDate AND :endDate")
    List<TestRuns> findTestRunsBetweenDates(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT tr FROM TestRuns tr WHERE tr.result = 'Fail' AND tr.executedAt >= :since")
    List<TestRuns> findRecentFailures(@Param("since") LocalDateTime since);

    long countByTestCase_TestCaseId(Integer testCaseId);

    long countByResult(String result);

    long countByExecutionType(String executionType);

    @Query("SELECT COUNT(tr) FROM TestRuns tr WHERE tr.testCase.testCaseId = :testCaseId AND tr.result = 'Pass'")
    long countPassedRunsForTestCase(@Param("testCaseId") Integer testCaseId);

    @Query("SELECT COUNT(tr) FROM TestRuns tr WHERE tr.testCase.testCaseId = :testCaseId AND tr.result = 'Fail'")
    long countFailedRunsForTestCase(@Param("testCaseId") Integer testCaseId);
}