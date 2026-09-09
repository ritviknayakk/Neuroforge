package com.neuroforge.backend.service;

import com.neuroforge.backend.dto.auditlog.AuditLogRequestDTO;
import com.neuroforge.backend.dto.auditlog.AuditLogResponseDTO;
import com.neuroforge.backend.dto.auditlog.AuditLogSearchRequestDTO;
import com.neuroforge.backend.entity.AuditLog;
import com.neuroforge.backend.entity.User;
import com.neuroforge.backend.mapper.AuditLogMapper;
import com.neuroforge.backend.repository.AuditLogRepository;
import com.neuroforge.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;
    private final AuditLogMapper auditLogMapper;

    @Transactional(readOnly = true)
    public List<AuditLogResponseDTO> getAllAuditLogs() {
        return auditLogRepository.findAll().stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AuditLogResponseDTO> getAuditLogsByUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        return auditLogRepository.findRecentByUser(userId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AuditLogResponseDTO> getAuditLogsByAction(String action) {
        return auditLogRepository.findByAction(action).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AuditLogResponseDTO> getAuditLogsByEntityType(String entityType) {
        return auditLogRepository.findByEntityType(entityType).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AuditLogResponseDTO> getAuditLogsByEntity(String entityType, Integer entityId) {
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AuditLogResponseDTO getAuditLogById(Long id) {
        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Audit log not found with id: " + id));
        return enrichResponseDto(auditLog);
    }

    @Transactional(readOnly = true)
    public List<AuditLogResponseDTO> getAuditLogsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findBetweenDates(startDate, endDate).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AuditLogResponseDTO> searchAuditLogs(AuditLogSearchRequestDTO searchRequest) {
        // Build query based on search criteria
        // For complex search, you might want to use Specification or QueryDSL
        // For now, we'll use a simple approach
        List<AuditLog> results = auditLogRepository.findAll();

        // Filter in memory (for simplicity - in production, use Specifications)
        return results.stream()
                .filter(log -> searchRequest.getUserId() == null ||
                        (log.getUser() != null && log.getUser().getUserId().equals(searchRequest.getUserId())))
                .filter(log -> searchRequest.getAction() == null ||
                        log.getAction().equalsIgnoreCase(searchRequest.getAction()))
                .filter(log -> searchRequest.getEntityType() == null ||
                        log.getEntityType().equalsIgnoreCase(searchRequest.getEntityType()))
                .filter(log -> searchRequest.getEntityId() == null ||
                        log.getEntityId().equals(searchRequest.getEntityId()))
                .filter(log -> searchRequest.getStartDate() == null ||
                        log.getCreatedAt().isAfter(searchRequest.getStartDate()))
                .filter(log -> searchRequest.getEndDate() == null ||
                        log.getCreatedAt().isBefore(searchRequest.getEndDate()))
                .filter(log -> searchRequest.getIpAddress() == null ||
                        (log.getIpAddress() != null && log.getIpAddress().equals(searchRequest.getIpAddress())))
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<String> getAllActions() {
        return auditLogRepository.findAllActions();
    }

    @Transactional(readOnly = true)
    public List<String> getAllEntityTypes() {
        return auditLogRepository.findAllEntityTypes();
    }

    public AuditLogResponseDTO createAuditLog(AuditLogRequestDTO request) {
        // Check if user exists (if provided)
        User user = null;
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));
        }

        AuditLog auditLog = auditLogMapper.toEntity(request);
        auditLog.setUser(user);

        AuditLog savedAuditLog = auditLogRepository.save(auditLog);
        return enrichResponseDto(savedAuditLog);
    }

    public void deleteAuditLog(Long id) {
        if (!auditLogRepository.existsById(id)) {
            throw new RuntimeException("Audit log not found with id: " + id);
        }
        auditLogRepository.deleteById(id);
    }

    public void deleteOldAuditLogs(LocalDateTime beforeDate) {
        List<AuditLog> oldLogs = auditLogRepository.findBetweenDates(
                LocalDateTime.MIN, beforeDate);
        auditLogRepository.deleteAll(oldLogs);
    }

    @Transactional(readOnly = true)
    public long getAuditLogCountByUser(Integer userId) {
        return auditLogRepository.countByUser_UserId(userId);
    }

    @Transactional(readOnly = true)
    public long getAuditLogCountByEntityType(String entityType) {
        return auditLogRepository.countByEntityType(entityType);
    }

    private AuditLogResponseDTO enrichResponseDto(AuditLog entity) {
        AuditLogResponseDTO dto = auditLogMapper.toResponseDto(entity);

        // Set user full name and email
        if (entity.getUser() != null && entity.getUser().getUserId() != null) {
            userRepository.findById(entity.getUser().getUserId())
                    .ifPresent(user -> {
                        dto.setUserFullName(user.getFullName());
                        dto.setUserEmail(user.getEmail());
                    });
        }

        return dto;
    }
}