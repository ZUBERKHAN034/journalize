package com.journalize.journalize.services;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.journalize.journalize.entities.Journal;
import com.journalize.journalize.exceptions.JournalAlreadyExistsException;
import com.journalize.journalize.exceptions.JournalNotFoundException;
import com.journalize.journalize.repositories.JournalRepository;

@Service
@RequiredArgsConstructor
public class JournalService {

    private final JournalRepository journalRepository;

    public Journal createJournal(Journal journal) {
        // check 1 — duplicate title?
        if (journalRepository.existsByTitle(journal.getTitle())) {
            throw new JournalAlreadyExistsException(journal.getTitle());
        }

        // check 2 — empty content?
        if (journal.getContent() == null || journal.getContent().isBlank()) {
            throw new IllegalArgumentException("Journal content cannot be empty");
        }

        final var savedJournal = journalRepository.save(journal);
        return savedJournal;
    }

    public Journal updateJournal(String id, Journal journal) {
        // check 1 — does it exist?
        Journal existingJournal = journalRepository.findById(id)
                .orElseThrow(() -> new JournalNotFoundException(id));

        // check 2 — duplicate title?
        if (journalRepository.existsByTitle(journal.getTitle())) {
            throw new JournalAlreadyExistsException(journal.getTitle());
        }

        // check 3 — empty content?
        if (journal.getContent() == null || journal.getContent().isBlank()) {
            throw new IllegalArgumentException("Journal content cannot be empty");
        }

        existingJournal.setTitle(journal.getTitle());
        existingJournal.setContent(journal.getContent());

        return journalRepository.save(existingJournal);
    }

    public List<Journal> getAllJournals() {
        final var journals = journalRepository.findAll();
        return journals;
    }

    public Journal getJournalById(String id) {
        // check 1 — does it exist?
        final var journal = journalRepository.findById(id).orElseThrow(() -> new JournalNotFoundException(id));
        return journal;
    }

    public void deleteJournal(String id) {
        // check 1 — does it exist?
        if (!journalRepository.existsById(id)) {
            throw new JournalNotFoundException(id);
        }
        journalRepository.deleteById(id);
    }
}