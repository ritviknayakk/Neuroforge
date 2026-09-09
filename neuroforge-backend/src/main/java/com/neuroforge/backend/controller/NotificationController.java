package com.neuroforge.backend.controller;

import com.neuroforge.backend.dto.notification.NotificationMarkReadRequestDTO;
import com.neuroforge.backend.dto.notification.NotificationRequestDTO;
import com.neuroforge.backend.dto.notification.NotificationResponseDTO;
import com.neuroforge.backend.dto.notification.NotificationUpdateRequestDTO;
import com.neuroforge.backend.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Notification management endpoints")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Get all notifications", description = "Returns a list of all notifications")
    public List<NotificationResponseDTO> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get notifications by user", description = "Returns all notifications for a specific user")
    public List<NotificationResponseDTO> getNotificationsByUser(@PathVariable Integer userId) {
        return notificationService.getNotificationsByUser(userId);
    }

    @GetMapping("/user/{userId}/unread")
    @Operation(summary = "Get unread notifications by user", description = "Returns unread notifications for a specific user")
    public List<NotificationResponseDTO> getUnreadNotificationsByUser(@PathVariable Integer userId) {
        return notificationService.getUnreadNotificationsByUser(userId);
    }

    @GetMapping("/type/{notificationType}")
    @Operation(summary = "Get notifications by type", description = "Returns notifications with a specific type")
    public List<NotificationResponseDTO> getNotificationsByType(@PathVariable String notificationType) {
        return notificationService.getNotificationsByType(notificationType);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get notification by ID", description = "Returns a single notification by its ID")
    public NotificationResponseDTO getNotificationById(@PathVariable Integer id) {
        return notificationService.getNotificationById(id);
    }

    @GetMapping("/user/{userId}/unread-count")
    @Operation(summary = "Get unread notification count", description = "Returns the count of unread notifications for a user")
    public long getUnreadNotificationCount(@PathVariable Integer userId) {
        return notificationService.getUnreadNotificationCount(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new notification", description = "Creates a new notification")
    public NotificationResponseDTO createNotification(@Valid @RequestBody NotificationRequestDTO request) {
        return notificationService.createNotification(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a notification", description = "Updates an existing notification")
    public NotificationResponseDTO updateNotification(
            @PathVariable Integer id,
            @Valid @RequestBody NotificationUpdateRequestDTO request) {
        return notificationService.updateNotification(id, request);
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Mark notification as read/unread", description = "Marks a notification as read or unread")
    public NotificationResponseDTO markNotificationRead(
            @PathVariable Integer id,
            @Valid @RequestBody NotificationMarkReadRequestDTO request) {
        return notificationService.markNotificationRead(id, request);
    }

    @PostMapping("/user/{userId}/mark-all-read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Mark all notifications as read", description = "Marks all notifications for a user as read")
    public void markAllNotificationsAsRead(@PathVariable Integer userId) {
        notificationService.markAllNotificationsAsRead(userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a notification", description = "Deletes a notification by its ID")
    public void deleteNotification(@PathVariable Integer id) {
        notificationService.deleteNotification(id);
    }

    @DeleteMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete all notifications for user", description = "Deletes all notifications for a specific user")
    public void deleteAllNotificationsForUser(@PathVariable Integer userId) {
        notificationService.deleteAllNotificationsForUser(userId);
    }

    @GetMapping("/user/{userId}/count")
    @Operation(summary = "Get notification count by user", description = "Returns the total count of notifications for a user")
    public long getNotificationCountByUser(@PathVariable Integer userId) {
        return notificationService.getNotificationCountByUser(userId);
    }
}