package com.journalize.journalize.services;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.journalize.journalize.entities.Journal;
import com.journalize.journalize.repositories.JournalRepository;

@Service
@RequiredArgsConstructor
public class JournalService {

    private final JournalRepository journalRepository;

    public Journal createJournal(Journal journal) {
        final var createdJournal = journalRepository.save(journal);
        return createdJournal;
    }

    public Journal updateJournal(String id, Journal journal) {
        final var existingJournal = journalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Journal not found"));

        existingJournal.setTitle(journal.getTitle());
        existingJournal.setContent(journal.getContent());

        return journalRepository.save(existingJournal);
    }

    public List<Journal> getAllJournals() {
        final var journals = journalRepository.findAll();
        return journals;
    }

    public Journal getJournalById(String id) {
        final var journal = journalRepository.findById(id).orElseThrow(() -> new RuntimeException("Journal not found"));
        return journal;
    }

    public void deleteJournal(String id) {
        journalRepository.deleteById(id);
    }
}