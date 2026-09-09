package com.neuroforge.backend.controller;

import com.neuroforge.backend.dto.usersession.UserSessionRefreshRequestDTO;
import com.neuroforge.backend.dto.usersession.UserSessionRequestDTO;
import com.neuroforge.backend.dto.usersession.UserSessionResponseDTO;
import com.neuroforge.backend.dto.usersession.UserSessionRevokeRequestDTO;
import com.neuroforge.backend.service.UserSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
@Tag(name = "User Sessions", description = "User session management endpoints")
public class UserSessionController {

    private final UserSessionService userSessionService;

    @GetMapping
    @Operation(summary = "Get all sessions", description = "Returns a list of all user sessions")
    public List<UserSessionResponseDTO> getAllSessions() {
        return userSessionService.getAllSessions();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get sessions by user ID", description = "Returns all sessions for a specific user")
    public List<UserSessionResponseDTO> getSessionsByUserId(@PathVariable Integer userId) {
        return userSessionService.getSessionsByUserId(userId);
    }

    @GetMapping("/user/{userId}/active")
    @Operation(summary = "Get active sessions by user ID", description = "Returns all active sessions for a specific user")
    public List<UserSessionResponseDTO> getActiveSessionsByUserId(@PathVariable Integer userId) {
        return userSessionService.getActiveSessionsByUserId(userId);
    }

    @GetMapping("/{sessionId}")
    @Operation(summary = "Get session by ID", description = "Returns a single session by its ID")
    public UserSessionResponseDTO getSessionById(@PathVariable Long sessionId) {
        return userSessionService.getSessionById(sessionId);
    }

    @GetMapping("/token")
    @Operation(summary = "Get session by token", description = "Returns a session by its token")
    public UserSessionResponseDTO getSessionByToken(@RequestParam String token) {
        return userSessionService.getSessionByToken(token);
    }

    @GetMapping("/validate")
    @Operation(summary = "Validate session token", description = "Checks if a session token is valid")
    public boolean validateSession(@RequestParam String token) {
        return userSessionService.isSessionValid(token);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new session", description = "Creates a new user session")
    public UserSessionResponseDTO createSession(@Valid @RequestBody UserSessionRequestDTO request) {
        return userSessionService.createSession(request);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh session", description = "Refreshes an existing session's expiration")
    public UserSessionResponseDTO refreshSession(@Valid @RequestBody UserSessionRefreshRequestDTO request) {
        return userSessionService.refreshSession(request);
    }

    @PostMapping("/revoke")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Revoke session", description = "Revokes a specific session")
    public void revokeSession(@Valid @RequestBody UserSessionRevokeRequestDTO request) {
        userSessionService.revokeSession(request.getToken());
    }

    @PostMapping("/revoke/user/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Revoke all user sessions", description = "Revokes all sessions for a specific user")
    public void revokeAllUserSessions(@PathVariable Integer userId) {
        userSessionService.revokeAllUserSessions(userId);
    }

    @DeleteMapping("/{sessionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete session", description = "Permanently deletes a session")
    public void deleteSession(@PathVariable Long sessionId) {
        userSessionService.deleteSession(sessionId);
    }

    @PostMapping("/cleanup")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cleanup expired sessions", description = "Removes expired and revoked sessions")
    public void cleanupExpiredSessions() {
        userSessionService.cleanupExpiredSessions();
    }

    @GetMapping("/user/{userId}/count")
    @Operation(summary = "Get active session count", description = "Returns the count of active sessions for a user")
    public long getActiveSessionCount(@PathVariable Integer userId) {
        return userSessionService.getActiveSessionCount(userId);
    }

    @GetMapping("/user/{userId}/count-valid")
    @Operation(summary = "Get valid session count", description = "Returns the count of active and non-expired sessions for a user")
    public long getActiveAndValidSessionCount(@PathVariable Integer userId) {
        return userSessionService.getActiveAndValidSessionCount(userId);
    }
}