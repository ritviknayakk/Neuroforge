package com.neuroforge.backend.controller;

import com.neuroforge.backend.dto.auditlog.AuditLogRequestDTO;
import com.neuroforge.backend.dto.auditlog.AuditLogResponseDTO;
import com.neuroforge.backend.dto.auditlog.AuditLogSearchRequestDTO;
import com.neuroforge.backend.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/audit-logs")
@RequiredArgsConstructor
@Tag(name = "Audit Logs", description = "Audit log management endpoints")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    @Operation(summary = "Get all audit logs", description = "Returns a list of all audit logs")
    public List<AuditLogResponseDTO> getAllAuditLogs() {
        return auditLogService.getAllAuditLogs();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get audit logs by user", description = "Returns all audit logs for a specific user")
    public List<AuditLogResponseDTO> getAuditLogsByUser(@PathVariable Integer userId) {
        return auditLogService.getAuditLogsByUser(userId);
    }

    @GetMapping("/action/{action}")
    @Operation(summary = "Get audit logs by action", description = "Returns audit logs with a specific action")
    public List<AuditLogResponseDTO> getAuditLogsByAction(@PathVariable String action) {
        return auditLogService.getAuditLogsByAction(action);
    }

    @GetMapping("/entity-type/{entityType}")
    @Operation(summary = "Get audit logs by entity type", description = "Returns audit logs for a specific entity type")
    public List<AuditLogResponseDTO> getAuditLogsByEntityType(@PathVariable String entityType) {
        return auditLogService.getAuditLogsByEntityType(entityType);
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    @Operation(summary = "Get audit logs by entity", description = "Returns audit logs for a specific entity")
    public List<AuditLogResponseDTO> getAuditLogsByEntity(
            @PathVariable String entityType,
            @PathVariable Integer entityId) {
        return auditLogService.getAuditLogsByEntity(entityType, entityId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get audit log by ID", description = "Returns a single audit log by its ID")
    public AuditLogResponseDTO getAuditLogById(@PathVariable Long id) {
        return auditLogService.getAuditLogById(id);
    }

    @GetMapping("/between-dates")
    @Operation(summary = "Get audit logs between dates", description = "Returns audit logs between two dates")
    public List<AuditLogResponseDTO> getAuditLogsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return auditLogService.getAuditLogsBetweenDates(startDate, endDate);
    }

    @GetMapping("/actions")
    @Operation(summary = "Get all actions", description = "Returns a list of all unique actions")
    public List<String> getAllActions() {
        return auditLogService.getAllActions();
    }

    @GetMapping("/entity-types")
    @Operation(summary = "Get all entity types", description = "Returns a list of all unique entity types")
    public List<String> getAllEntityTypes() {
        return auditLogService.getAllEntityTypes();
    }

    @PostMapping("/search")
    @Operation(summary = "Search audit logs", description = "Searches audit logs with multiple criteria")
    public List<AuditLogResponseDTO> searchAuditLogs(@Valid @RequestBody AuditLogSearchRequestDTO searchRequest) {
        return auditLogService.searchAuditLogs(searchRequest);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new audit log", description = "Creates a new audit log entry")
    public AuditLogResponseDTO createAuditLog(@Valid @RequestBody AuditLogRequestDTO request) {
        return auditLogService.createAuditLog(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an audit log", description = "Deletes an audit log by its ID")
    public void deleteAuditLog(@PathVariable Long id) {
        auditLogService.deleteAuditLog(id);
    }

    @DeleteMapping("/cleanup")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete old audit logs", description = "Deletes audit logs older than the specified date")
    public void deleteOldAuditLogs(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime beforeDate) {
        auditLogService.deleteOldAuditLogs(beforeDate);
    }

    @GetMapping("/user/{userId}/count")
    @Operation(summary = "Get audit log count by user", description = "Returns the total count of audit logs for a user")
    public long getAuditLogCountByUser(@PathVariable Integer userId) {
        return auditLogService.getAuditLogCountByUser(userId);
    }

    @GetMapping("/entity-type/{entityType}/count")
    @Operation(summary = "Get audit log count by entity type", description = "Returns the total count of audit logs for an entity type")
    public long getAuditLogCountByEntityType(@PathVariable String entityType) {
        return auditLogService.getAuditLogCountByEntityType(entityType);
    }
}