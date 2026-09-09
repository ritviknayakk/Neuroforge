package com.neuroforge.backend.service;

import com.neuroforge.backend.dto.usersession.UserSessionRequestDTO;
import com.neuroforge.backend.dto.usersession.UserSessionResponseDTO;
import com.neuroforge.backend.dto.usersession.UserSessionRefreshRequestDTO;
import com.neuroforge.backend.entity.User;
import com.neuroforge.backend.entity.UserSessions;
import com.neuroforge.backend.mapper.UserSessionMapper;
import com.neuroforge.backend.repository.UserRepository;
import com.neuroforge.backend.repository.UserSessionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserSessionService {

    private final UserSessionsRepository userSessionsRepository;
    private final UserRepository userRepository;
    private final UserSessionMapper userSessionMapper;

    @Transactional(readOnly = true)
    public List<UserSessionResponseDTO> getAllSessions() {
        return userSessionsRepository.findAll().stream()
                .map(userSessionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserSessionResponseDTO> getSessionsByUserId(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        return userSessionsRepository.findByUser_UserId(userId).stream()
                .map(userSessionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserSessionResponseDTO> getActiveSessionsByUserId(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        return userSessionsRepository.findByUser_UserIdAndRevokedAtIsNull(userId).stream()
                .map(userSessionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserSessionResponseDTO getSessionById(Long sessionId) {
        UserSessions session = userSessionsRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found with id: " + sessionId));
        return userSessionMapper.toResponseDto(session);
    }

    @Transactional(readOnly = true)
    public UserSessionResponseDTO getSessionByToken(String token) {
        // In production, you would hash the token first
        String tokenHash = hashToken(token);
        UserSessions session = userSessionsRepository.findByTokenHashAndRevokedAtIsNull(tokenHash)
                .orElseThrow(() -> new RuntimeException("Invalid or expired session token"));
        return userSessionMapper.toResponseDto(session);
    }

    @Transactional(readOnly = true)
    public boolean isSessionValid(String token) {
        String tokenHash = hashToken(token);
        return userSessionsRepository.existsByTokenHashAndRevokedAtIsNull(tokenHash);
    }

    public UserSessionResponseDTO createSession(UserSessionRequestDTO request) {
        // Check if user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        // Hash the token (in production, use a proper hashing algorithm)
        String tokenHash = hashToken(request.getToken());

        // Create session
        UserSessions session = userSessionMapper.toEntity(request);
        session.setUser(user);
        session.setTokenHash(tokenHash);
        session.setIssuedAt(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plusSeconds(request.getExpiresInSeconds()));

        // Invalidate any existing active sessions if needed (optional)
        // userSessionsRepository.revokeAllActiveUserSessions(request.getUserId(),
        // LocalDateTime.now());

        UserSessions savedSession = userSessionsRepository.save(session);
        return userSessionMapper.toResponseDto(savedSession);
    }

    public UserSessionResponseDTO refreshSession(UserSessionRefreshRequestDTO request) {
        String tokenHash = hashToken(request.getToken());

        UserSessions session = userSessionsRepository.findByTokenHashAndRevokedAtIsNull(tokenHash)
                .orElseThrow(() -> new RuntimeException("Session not found or already revoked"));

        // Check if session is expired
        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Session has expired");
        }

        // Update expiration
        session.setExpiresAt(LocalDateTime.now().plusSeconds(request.getNewExpiresInSeconds()));

        UserSessions updatedSession = userSessionsRepository.save(session);
        return userSessionMapper.toResponseDto(updatedSession);
    }

    public void revokeSession(String token) {
        String tokenHash = hashToken(token);
        UserSessions session = userSessionsRepository.findByTokenHashAndRevokedAtIsNull(tokenHash)
                .orElseThrow(() -> new RuntimeException("Session not found or already revoked"));

        session.setRevokedAt(LocalDateTime.now());
        userSessionsRepository.save(session);
    }

    public void revokeAllUserSessions(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        userSessionsRepository.revokeAllActiveUserSessions(userId, LocalDateTime.now());
    }

    public void deleteSession(Long sessionId) {
        if (!userSessionsRepository.existsById(sessionId)) {
            throw new RuntimeException("Session not found with id: " + sessionId);
        }
        userSessionsRepository.deleteById(sessionId);
    }

    @Transactional
    public void cleanupExpiredSessions() {
        // Delete sessions that have expired and have been revoked
        userSessionsRepository.deleteExpiredAndRevokedSessions(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public long getActiveSessionCount(Integer userId) {
        return userSessionsRepository.countByUser_UserIdAndRevokedAtIsNull(userId);
    }

    @Transactional(readOnly = true)
    public long getActiveAndValidSessionCount(Integer userId) {
        return userSessionsRepository.countByUser_UserIdAndRevokedAtIsNullAndExpiresAtAfter(
                userId, LocalDateTime.now());
    }

    // In production, use a proper hashing algorithm like SHA-256
    private String hashToken(String token) {
        // For development, just return the token as-is
        // In production: return DigestUtils.sha256Hex(token);
        return token;
    }
}