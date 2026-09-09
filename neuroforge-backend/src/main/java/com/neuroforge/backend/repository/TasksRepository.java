package com.neuroforge.backend.repository;

import com.neuroforge.backend.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TasksRepository extends JpaRepository<Tasks, Integer> {

    List<Tasks> findByProject_ProjectId(Integer projectId);

    List<Tasks> findByUserStory_UserStoryId(Integer userStoryId);

    List<Tasks> findBySprint_SprintId(Integer sprintId);

    List<Tasks> findByAssignedTo_UserId(Integer userId);

    List<Tasks> findByProject_ProjectIdAndIsArchivedFalse(Integer projectId);

    List<Tasks> findByUserStory_UserStoryIdAndIsArchivedFalse(Integer userStoryId);

    List<Tasks> findBySprint_SprintIdAndIsArchivedFalse(Integer sprintId);

    List<Tasks> findByAssignedTo_UserIdAndIsArchivedFalse(Integer userId);

    List<Tasks> findByStatus(String status);

    List<Tasks> findByPriority(String priority);

    Optional<Tasks> findByProject_ProjectIdAndTaskId(Integer projectId, Integer taskId);

    @Query("SELECT t FROM Tasks t WHERE t.project.projectId = :projectId AND t.status IN :statuses")
    List<Tasks> findByProjectIdAndStatuses(@Param("projectId") Integer projectId,
            @Param("statuses") List<String> statuses);

    @Query("SELECT t FROM Tasks t WHERE t.assignedTo.userId = :userId AND t.status IN :statuses")
    List<Tasks> findByAssignedToAndStatuses(@Param("userId") Integer userId, @Param("statuses") List<String> statuses);

    @Query("SELECT t FROM Tasks t WHERE t.dueDate <= :date AND t.status NOT IN ('Done', 'Blocked')")
    List<Tasks> findOverdueTasks(@Param("date") LocalDate date);

    @Query("SELECT t FROM Tasks t WHERE t.dueDate = :date AND t.status != 'Done'")
    List<Tasks> findTasksDueToday(@Param("date") LocalDate date);

    long countByProject_ProjectId(Integer projectId);

    long countByProject_ProjectIdAndIsArchivedFalse(Integer projectId);

    long countByUserStory_UserStoryId(Integer userStoryId);

    long countBySprint_SprintId(Integer sprintId);

    long countByAssignedTo_UserId(Integer userId);

    long countByStatus(String status);
}