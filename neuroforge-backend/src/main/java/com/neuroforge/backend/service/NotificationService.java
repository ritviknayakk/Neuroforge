package com.neuroforge.backend.service;

import com.neuroforge.backend.dto.notification.NotificationMarkReadRequestDTO;
import com.neuroforge.backend.dto.notification.NotificationRequestDTO;
import com.neuroforge.backend.dto.notification.NotificationResponseDTO;
import com.neuroforge.backend.dto.notification.NotificationUpdateRequestDTO;
import com.neuroforge.backend.entity.Deployments;
import com.neuroforge.backend.entity.Issues;
import com.neuroforge.backend.entity.Notifications;
import com.neuroforge.backend.entity.User;
import com.neuroforge.backend.mapper.NotificationMapper;
import com.neuroforge.backend.repository.DeploymentsRepository;
import com.neuroforge.backend.repository.IssuesRepository;
import com.neuroforge.backend.repository.NotificationsRepository;
import com.neuroforge.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationsRepository notificationsRepository;
    private final UserRepository userRepository;
    private final DeploymentsRepository deploymentsRepository;
    private final IssuesRepository issuesRepository;
    private final NotificationMapper notificationMapper;

    private static final List<String> VALID_TYPES = Arrays.asList(
            "DeploymentSuccess", "DeploymentFailure", "IssueAssigned", "General");

    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> getAllNotifications() {
        return notificationsRepository.findAll().stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> getNotificationsByUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        return notificationsRepository.findRecentNotificationsByUser(userId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> getUnreadNotificationsByUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        return notificationsRepository.findUnreadNotificationsByUser(userId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> getNotificationsByType(String notificationType) {
        if (!VALID_TYPES.contains(notificationType)) {
            throw new RuntimeException("Invalid notification type: " + notificationType +
                    ". Valid types: " + VALID_TYPES);
        }
        return notificationsRepository.findByNotificationType(notificationType).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NotificationResponseDTO getNotificationById(Integer id) {
        Notifications notification = notificationsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        return enrichResponseDto(notification);
    }

    @Transactional(readOnly = true)
    public long getUnreadNotificationCount(Integer userId) {
        return notificationsRepository.countByRecipient_UserIdAndIsReadFalse(userId);
    }

    public NotificationResponseDTO createNotification(NotificationRequestDTO request) {
        // Check if recipient exists
        User recipient = userRepository.findById(request.getRecipientUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getRecipientUserId()));

        // Validate notification type
        if (!VALID_TYPES.contains(request.getNotificationType())) {
            throw new RuntimeException("Invalid notification type: " + request.getNotificationType() +
                    ". Valid types: " + VALID_TYPES);
        }

        // Check if deployment exists (if provided)
        Deployments deployment = null;
        if (request.getDeploymentId() != null) {
            deployment = deploymentsRepository.findById(request.getDeploymentId())
                    .orElseThrow(
                            () -> new RuntimeException("Deployment not found with id: " + request.getDeploymentId()));
        }

        // Check if issue exists (if provided)
        Issues issue = null;
        if (request.getIssueId() != null) {
            issue = issuesRepository.findById(request.getIssueId())
                    .orElseThrow(() -> new RuntimeException("Issue not found with id: " + request.getIssueId()));
        }

        Notifications notification = notificationMapper.toEntity(request);
        notification.setRecipient(recipient);
        notification.setDeployment(deployment);
        notification.setIssue(issue);

        // Set default values if not provided
        if (notification.getIsRead() == null) {
            notification.setIsRead(false);
        }

        Notifications savedNotification = notificationsRepository.save(notification);
        return enrichResponseDto(savedNotification);
    }

    public NotificationResponseDTO updateNotification(Integer id, NotificationUpdateRequestDTO request) {
        Notifications notification = notificationsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));

        // Validate notification type if being updated
        if (request.getNotificationType() != null && !VALID_TYPES.contains(request.getNotificationType())) {
            throw new RuntimeException("Invalid notification type: " + request.getNotificationType() +
                    ". Valid types: " + VALID_TYPES);
        }

        notificationMapper.updateEntityFromDto(request, notification);
        Notifications updatedNotification = notificationsRepository.save(notification);
        return enrichResponseDto(updatedNotification);
    }

    public NotificationResponseDTO markNotificationRead(Integer id, NotificationMarkReadRequestDTO request) {
        Notifications notification = notificationsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));

        notification.setIsRead(request.getIsRead() != null ? request.getIsRead() : true);
        Notifications updatedNotification = notificationsRepository.save(notification);
        return enrichResponseDto(updatedNotification);
    }

    public void markAllNotificationsAsRead(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        notificationsRepository.markAllAsReadForUser(userId);
    }

    public void deleteNotification(Integer id) {
        if (!notificationsRepository.existsById(id)) {
            throw new RuntimeException("Notification not found with id: " + id);
        }
        notificationsRepository.deleteById(id);
    }

    public void deleteAllNotificationsForUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        List<Notifications> notifications = notificationsRepository.findByRecipient_UserId(userId);
        notificationsRepository.deleteAll(notifications);
    }

    @Transactional(readOnly = true)
    public long getNotificationCountByUser(Integer userId) {
        return notificationsRepository.countByRecipient_UserId(userId);
    }

    private NotificationResponseDTO enrichResponseDto(Notifications entity) {
        NotificationResponseDTO dto = notificationMapper.toResponseDto(entity);

        // Set recipient full name
        if (entity.getRecipient() != null && entity.getRecipient().getUserId() != null) {
            userRepository.findById(entity.getRecipient().getUserId())
                    .ifPresent(user -> dto.setRecipientFullName(user.getFullName()));
        }

        return dto;
    }
}