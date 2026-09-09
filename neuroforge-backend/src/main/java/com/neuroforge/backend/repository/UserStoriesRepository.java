package com.neuroforge.backend.repository;

import com.neuroforge.backend.entity.UserStories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserStoriesRepository extends JpaRepository<UserStories, Integer> {

    List<UserStories> findByRequirement_RequirementId(Integer requirementId);

    List<UserStories> findBySprint_SprintId(Integer sprintId);

    List<UserStories> findByRequirement_RequirementIdAndIsArchivedFalse(Integer requirementId);

    List<UserStories> findBySprint_SprintIdAndIsArchivedFalse(Integer sprintId);

    List<UserStories> findByStatus(String status);

    List<UserStories> findByPriority(String priority);

    List<UserStories> findByIsAIGeneratedTrue();

    List<UserStories> findByIsAIGeneratedFalse();

    Optional<UserStories> findByRequirement_RequirementIdAndUserStoryId(Integer requirementId, Integer userStoryId);

    @Query("SELECT us FROM UserStories us WHERE us.requirement.requirementId = :requirementId AND us.status IN :statuses")
    List<UserStories> findByRequirementIdAndStatuses(@Param("requirementId") Integer requirementId,
            @Param("statuses") List<String> statuses);

    @Query("SELECT us FROM UserStories us WHERE us.sprint.sprintId = :sprintId AND us.status IN :statuses")
    List<UserStories> findBySprintIdAndStatuses(@Param("sprintId") Integer sprintId,
            @Param("statuses") List<String> statuses);

    @Query("SELECT us FROM UserStories us WHERE us.storyText LIKE %:keyword% AND us.isArchived = false")
    List<UserStories> searchActiveUserStories(@Param("keyword") String keyword);

    long countByRequirement_RequirementId(Integer requirementId);

    long countBySprint_SprintId(Integer sprintId);

    long countByRequirement_RequirementIdAndIsArchivedFalse(Integer requirementId);

    long countBySprint_SprintIdAndIsArchivedFalse(Integer sprintId);

    long countByStatus(String status);
}