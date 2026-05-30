package com.journalize.journalize.controllers;

import java.util.List;

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
    public Journal createJournal(@RequestBody Journal journal) {
        return journalService.createJournal(journal);
    }

    @PutMapping("/{id}")
    public Journal updateJournal(@PathVariable String id, @RequestBody Journal journal) {
        return journalService.updateJournal(id, journal);
    }

    @GetMapping
    public List<Journal> getAllJournals() {
        return journalService.getAllJournals();
    }

    @GetMapping("/{id}")
    public Journal getJournalById(@PathVariable String id) {
        return journalService.getJournalById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteJournal(@PathVariable String id) {
        journalService.deleteJournal(id);
    }
}