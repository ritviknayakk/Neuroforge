package com.neuroforge.backend.repository;

import com.neuroforge.backend.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationsRepository extends JpaRepository<Notifications, Integer> {

    List<Notifications> findByRecipient_UserId(Integer userId);

    List<Notifications> findByRecipient_UserIdAndIsReadFalse(Integer userId);

    List<Notifications> findByRecipient_UserIdAndIsReadTrue(Integer userId);

    List<Notifications> findByNotificationType(String notificationType);

    List<Notifications> findByDeployment_DeploymentId(Integer deploymentId);

    List<Notifications> findByIssue_IssueId(Integer issueId);

    @Query("SELECT n FROM Notifications n WHERE n.recipient.userId = :userId ORDER BY n.createdAt DESC")
    List<Notifications> findRecentNotificationsByUser(@Param("userId") Integer userId);

    @Query("SELECT n FROM Notifications n WHERE n.recipient.userId = :userId AND n.isRead = false ORDER BY n.createdAt DESC")
    List<Notifications> findUnreadNotificationsByUser(@Param("userId") Integer userId);

    @Modifying
    @Query("UPDATE Notifications n SET n.isRead = true WHERE n.recipient.userId = :userId AND n.isRead = false")
    void markAllAsReadForUser(@Param("userId") Integer userId);

    @Modifying
    @Query("UPDATE Notifications n SET n.isRead = :isRead WHERE n.notificationId = :notificationId")
    void updateReadStatus(@Param("notificationId") Integer notificationId, @Param("isRead") Boolean isRead);

    long countByRecipient_UserIdAndIsReadFalse(Integer userId);

    long countByRecipient_UserId(Integer userId);

    long countByNotificationType(String notificationType);
}