package com.journalize.journalize.services;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.journalize.journalize.dto.ApiResponse;
import com.journalize.journalize.dto.journal.CreateJournalRequest;
import com.journalize.journalize.dto.journal.UpdateJournalRequest;
import com.journalize.journalize.entities.Journal;
import com.journalize.journalize.exceptions.journal.JournalAlreadyExistsException;
import com.journalize.journalize.exceptions.journal.JournalNotFoundException;
import com.journalize.journalize.repositories.JournalRepository;

@Service
@RequiredArgsConstructor
public class JournalService {

    private final JournalRepository journalRepository;

    public ApiResponse<Journal> createJournal(CreateJournalRequest request) {
        try {
            // check if journal with the same title already exists
            if (journalRepository.existsByTitle(request.getTitle())) {
                throw new JournalAlreadyExistsException(request.getTitle());
            }

            // save the journal to the database
            final Journal journal = Journal.builder().title(request.getTitle()).content(request.getContent()).build();

            final Journal savedJournal = journalRepository.save(journal);
            return ApiResponse.success("Journal created successfully", savedJournal);
        } catch (Exception e) {
            throw e;
        }
    }

    public ApiResponse<Journal> updateJournal(String id, UpdateJournalRequest request) {
        try {
            // check if journal exists
            Journal existingJournal = journalRepository.findById(id)
                    .orElseThrow(() -> new JournalNotFoundException(id));

            // check if title is already taken
            if (journalRepository.existsByTitle(request.getTitle())) {
                throw new JournalAlreadyExistsException(request.getTitle());
            }

            existingJournal.setTitle(request.getTitle());
            existingJournal.setContent(request.getContent());

            final Journal updatedJournal = journalRepository.save(existingJournal);
            return ApiResponse.success("Journal updated successfully", updatedJournal);
        } catch (Exception e) {
            throw e;
        }
    }

    public ApiResponse<List<Journal>> getAllJournals() {
        try {
            final List<Journal> journals = journalRepository.findAll();
            return ApiResponse.success("Journal found successfully", journals);
        } catch (Exception e) {
            throw e;
        }
    }

    public ApiResponse<Journal> getJournalById(String id) {
        try {
            // check if journal exists
            final Journal journal = journalRepository.findById(id).orElseThrow(() -> new JournalNotFoundException(id));
            return ApiResponse.success("Journal found successfully", journal);
        } catch (Exception e) {
            throw e;
        }
    }

    public ApiResponse<Void> deleteJournal(String id) {
        try {

            // check if journal exists
            if (!journalRepository.existsById(id)) {
                throw new JournalNotFoundException(id);
            }

            // delete journal from the database
            journalRepository.deleteById(id);

            return ApiResponse.success("Journal deleted successfully");
        } catch (Exception e) {
            throw e;
        }
    }
}