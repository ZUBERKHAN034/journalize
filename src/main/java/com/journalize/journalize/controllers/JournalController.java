package com.journalize.journalize.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.journalize.journalize.entities.Journal;
import com.journalize.journalize.services.JournalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/journals")
@RequiredArgsConstructor
public class JournalController {

    private final JournalService journalService;

    @PostMapping
    public ResponseEntity<Journal> createJournal(@RequestBody Journal journal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(journalService.createJournal(journal));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Journal> updateJournal(@PathVariable String id, @RequestBody Journal journal) {
        return ResponseEntity.ok(journalService.updateJournal(id, journal));
    }

    @GetMapping
    public ResponseEntity<List<Journal>> getAllJournals() {
        return ResponseEntity.ok(journalService.getAllJournals());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Journal> getJournalById(@PathVariable String id) {
        return ResponseEntity.ok(journalService.getJournalById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJournal(@PathVariable String id) {
        journalService.deleteJournal(id);
        return ResponseEntity.noContent().build();
    }
}