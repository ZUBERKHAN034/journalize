package com.journalize.journalize.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.journalize.journalize.dto.ApiResponse;
import com.journalize.journalize.dto.journal.CreateJournalRequest;
import com.journalize.journalize.dto.journal.UpdateJournalRequest;
import com.journalize.journalize.entities.Journal;
import com.journalize.journalize.services.JournalService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/journals")
@SecurityRequirement(name = "bearerAuth")
public class JournalController {

    private final JournalService journalService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createJournal(@Valid @RequestBody CreateJournalRequest request) {
        final ApiResponse<Journal> response = journalService.createJournal(request);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateJournal(@PathVariable String id,
            @Valid @RequestBody UpdateJournalRequest request) {
        final ApiResponse<Journal> response = journalService.updateJournal(id, request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllJournals() {
        final ApiResponse<List<Journal>> response = journalService.getAllJournals();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getJournalById(@PathVariable String id) {
        final ApiResponse<Journal> response = journalService.getJournalById(id);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteJournal(@PathVariable String id) {
        final ApiResponse<Void> response = journalService.deleteJournal(id);
        return ResponseEntity.ok().body(response);
    }
}