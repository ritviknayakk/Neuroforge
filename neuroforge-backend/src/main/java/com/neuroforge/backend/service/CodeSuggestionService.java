package com.neuroforge.backend.service;

import com.neuroforge.backend.dto.codesuggestion.CodeSuggestionDecisionRequestDTO;
import com.neuroforge.backend.dto.codesuggestion.CodeSuggestionRequestDTO;
import com.neuroforge.backend.dto.codesuggestion.CodeSuggestionResponseDTO;
import com.neuroforge.backend.dto.codesuggestion.CodeSuggestionStatusUpdateRequestDTO;
import com.neuroforge.backend.dto.codesuggestion.CodeSuggestionUpdateRequestDTO;
import com.neuroforge.backend.entity.CodeSuggestions;
import com.neuroforge.backend.entity.Tasks;
import com.neuroforge.backend.entity.User;
import com.neuroforge.backend.mapper.CodeSuggestionMapper;
import com.neuroforge.backend.repository.CodeSuggestionsRepository;
import com.neuroforge.backend.repository.TasksRepository;
import com.neuroforge.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CodeSuggestionService {

    private final CodeSuggestionsRepository codeSuggestionsRepository;
    private final TasksRepository tasksRepository;
    private final UserRepository userRepository;
    private final CodeSuggestionMapper codeSuggestionMapper;

    private static final List<String> VALID_STATUSES = Arrays.asList("Pending", "Accepted", "Edited", "Dismissed");

    @Transactional(readOnly = true)
    public List<CodeSuggestionResponseDTO> getAllCodeSuggestions() {
        return codeSuggestionsRepository.findAll().stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CodeSuggestionResponseDTO> getCodeSuggestionsByTaskId(Integer taskId) {
        if (!tasksRepository.existsById(taskId)) {
            throw new RuntimeException("Task not found with id: " + taskId);
        }
        return codeSuggestionsRepository.findByTask_TaskId(taskId).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CodeSuggestionResponseDTO> getCodeSuggestionsByTaskAndStatus(Integer taskId, String status) {
        if (!tasksRepository.existsById(taskId)) {
            throw new RuntimeException("Task not found with id: " + taskId);
        }
        if (!VALID_STATUSES.contains(status)) {
            throw new RuntimeException("Invalid status: " + status + ". Valid statuses: " + VALID_STATUSES);
        }
        return codeSuggestionsRepository.findByTask_TaskIdAndStatus(taskId, status).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CodeSuggestionResponseDTO> getCodeSuggestionsByStatus(String status) {
        if (!VALID_STATUSES.contains(status)) {
            throw new RuntimeException("Invalid status: " + status + ". Valid statuses: " + VALID_STATUSES);
        }
        return codeSuggestionsRepository.findByStatus(status).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CodeSuggestionResponseDTO> getAIGeneratedSuggestions() {
        return codeSuggestionsRepository.findByIsAIGeneratedTrue().stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CodeSuggestionResponseDTO> getSuggestionsByLanguage(String language) {
        return codeSuggestionsRepository.findByLanguage(language).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CodeSuggestionResponseDTO getCodeSuggestionById(Integer id) {
        CodeSuggestions suggestion = codeSuggestionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Code suggestion not found with id: " + id));
        return enrichResponseDto(suggestion);
    }

    @Transactional(readOnly = true)
    public CodeSuggestionResponseDTO getAcceptedSuggestionForTask(Integer taskId) {
        CodeSuggestions suggestion = codeSuggestionsRepository.findAcceptedSuggestionForTask(taskId)
                .orElseThrow(() -> new RuntimeException("No accepted code suggestion found for task: " + taskId));
        return enrichResponseDto(suggestion);
    }

    @Transactional(readOnly = true)
    public List<CodeSuggestionResponseDTO> searchByCode(String keyword) {
        return codeSuggestionsRepository.searchByCode(keyword).stream()
                .map(this::enrichResponseDto)
                .collect(Collectors.toList());
    }

    public CodeSuggestionResponseDTO createCodeSuggestion(CodeSuggestionRequestDTO request) {
        // Check if task exists
        Tasks task = tasksRepository.findById(request.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + request.getTaskId()));

        // Validate status if provided
        if (request.getStatus() != null && !VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        CodeSuggestions suggestion = codeSuggestionMapper.toEntity(request);
        suggestion.setTask(task);

        // Set default values if not provided
        if (suggestion.getIsAIGenerated() == null) {
            suggestion.setIsAIGenerated(true);
        }
        if (suggestion.getStatus() == null) {
            suggestion.setStatus("Pending");
        }

        CodeSuggestions savedSuggestion = codeSuggestionsRepository.save(suggestion);
        return enrichResponseDto(savedSuggestion);
    }

    public CodeSuggestionResponseDTO updateCodeSuggestion(Integer id, CodeSuggestionUpdateRequestDTO request) {
        CodeSuggestions suggestion = codeSuggestionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Code suggestion not found with id: " + id));

        // Validate status if being updated
        if (request.getStatus() != null && !VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        codeSuggestionMapper.updateEntityFromDto(request, suggestion);
        CodeSuggestions updatedSuggestion = codeSuggestionsRepository.save(suggestion);
        return enrichResponseDto(updatedSuggestion);
    }

    public CodeSuggestionResponseDTO updateSuggestionStatus(Integer id, CodeSuggestionStatusUpdateRequestDTO request) {
        CodeSuggestions suggestion = codeSuggestionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Code suggestion not found with id: " + id));

        if (!VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        suggestion.setStatus(request.getStatus());

        // If the status is not Pending, set decidedAt
        if (!"Pending".equals(request.getStatus())) {
            suggestion.setDecidedAt(LocalDateTime.now());
        }

        CodeSuggestions updatedSuggestion = codeSuggestionsRepository.save(suggestion);
        return enrichResponseDto(updatedSuggestion);
    }

    public CodeSuggestionResponseDTO decideSuggestion(Integer id, CodeSuggestionDecisionRequestDTO request) {
        CodeSuggestions suggestion = codeSuggestionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Code suggestion not found with id: " + id));

        if (!VALID_STATUSES.contains(request.getStatus())) {
            throw new RuntimeException(
                    "Invalid status: " + request.getStatus() + ". Valid statuses: " + VALID_STATUSES);
        }

        // Check if user exists
        User user = userRepository.findById(request.getDecidedByUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getDecidedByUserId()));

        suggestion.setDecidedBy(user);
        suggestion.setDecidedAt(LocalDateTime.now());
        suggestion.setStatus(request.getStatus());

        CodeSuggestions decidedSuggestion = codeSuggestionsRepository.save(suggestion);
        return enrichResponseDto(decidedSuggestion);
    }

    public void deleteCodeSuggestion(Integer id) {
        if (!codeSuggestionsRepository.existsById(id)) {
            throw new RuntimeException("Code suggestion not found with id: " + id);
        }
        codeSuggestionsRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long getCodeSuggestionCountByTask(Integer taskId) {
        return codeSuggestionsRepository.countByTask_TaskId(taskId);
    }

    @Transactional(readOnly = true)
    public long getCodeSuggestionCountByTaskAndStatus(Integer taskId, String status) {
        return codeSuggestionsRepository.countByTask_TaskIdAndStatus(taskId, status);
    }

    private CodeSuggestionResponseDTO enrichResponseDto(CodeSuggestions entity) {
        CodeSuggestionResponseDTO dto = codeSuggestionMapper.toResponseDto(entity);

        // Set decided by full name
        if (entity.getDecidedBy() != null && entity.getDecidedBy().getUserId() != null) {
            userRepository.findById(entity.getDecidedBy().getUserId())
                    .ifPresent(user -> dto.setDecidedByFullName(user.getFullName()));
        }

        return dto;
    }
}