package com.neuroforge.backend.repository;

import com.neuroforge.backend.entity.CodeSuggestions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CodeSuggestionsRepository extends JpaRepository<CodeSuggestions, Integer> {

    List<CodeSuggestions> findByTask_TaskId(Integer taskId);

    List<CodeSuggestions> findByTask_TaskIdAndStatus(Integer taskId, String status);

    List<CodeSuggestions> findByStatus(String status);

    List<CodeSuggestions> findByIsAIGeneratedTrue();

    List<CodeSuggestions> findByIsAIGeneratedFalse();

    List<CodeSuggestions> findByLanguage(String language);

    Optional<CodeSuggestions> findByTask_TaskIdAndCodeSuggestionId(Integer taskId, Integer codeSuggestionId);

    @Query("SELECT cs FROM CodeSuggestions cs WHERE cs.task.taskId = :taskId AND cs.status IN :statuses")
    List<CodeSuggestions> findByTaskIdAndStatuses(@Param("taskId") Integer taskId,
            @Param("statuses") List<String> statuses);

    @Query("SELECT cs FROM CodeSuggestions cs WHERE cs.suggestedCode LIKE %:keyword%")
    List<CodeSuggestions> searchByCode(@Param("keyword") String keyword);

    @Query("SELECT cs FROM CodeSuggestions cs WHERE cs.task.taskId = :taskId AND cs.status = 'Accepted'")
    Optional<CodeSuggestions> findAcceptedSuggestionForTask(@Param("taskId") Integer taskId);

    long countByTask_TaskId(Integer taskId);

    long countByTask_TaskIdAndStatus(Integer taskId, String status);

    long countByStatus(String status);
}