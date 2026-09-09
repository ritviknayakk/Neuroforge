package com.neuroforge.backend.repository;

import com.neuroforge.backend.entity.DesignSuggestions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DesignSuggestionsRepository extends JpaRepository<DesignSuggestions, Integer> {

    List<DesignSuggestions> findByProject_ProjectId(Integer projectId);

    List<DesignSuggestions> findByRequirement_RequirementId(Integer requirementId);

    List<DesignSuggestions> findByProject_ProjectIdAndStatus(Integer projectId, String status);

    List<DesignSuggestions> findByStatus(String status);

    List<DesignSuggestions> findByIsAIGeneratedTrue();

    List<DesignSuggestions> findByIsAIGeneratedFalse();

    Optional<DesignSuggestions> findByProject_ProjectIdAndDesignSuggestionId(Integer projectId,
            Integer designSuggestionId);

    @Query("SELECT ds FROM DesignSuggestions ds WHERE ds.project.projectId = :projectId AND ds.status IN :statuses")
    List<DesignSuggestions> findByProjectIdAndStatuses(@Param("projectId") Integer projectId,
            @Param("statuses") List<String> statuses);

    @Query("SELECT ds FROM DesignSuggestions ds WHERE ds.componentsDescription LIKE %:keyword%")
    List<DesignSuggestions> searchByComponentsDescription(@Param("keyword") String keyword);

    long countByProject_ProjectId(Integer projectId);

    long countByProject_ProjectIdAndStatus(Integer projectId, String status);

    long countByStatus(String status);
}