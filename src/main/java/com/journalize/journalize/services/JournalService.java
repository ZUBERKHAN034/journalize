package com.journalize.journalize.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.journalize.journalize.dto.ApiResponse;
import com.journalize.journalize.dto.journal.CreateJournalRequest;
import com.journalize.journalize.dto.journal.UpdateJournalRequest;
import com.journalize.journalize.entities.Journal;
import com.journalize.journalize.entities.User;
import com.journalize.journalize.exceptions.BadRequestException;
import com.journalize.journalize.exceptions.journal.JournalAlreadyExistsException;
import com.journalize.journalize.exceptions.journal.JournalNotFoundException;
import com.journalize.journalize.exceptions.user.UserNotFoundException;
import com.journalize.journalize.repositories.JournalRepository;
import com.journalize.journalize.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class JournalService {

    private final JournalRepository journalRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    @Transactional
    public ApiResponse<Journal> createJournal(CreateJournalRequest request) {
        // check if user is authenticated
        String email = authService.getCurrentUserDetails().getUsername();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        // check if journal with the same title already exists
        if (journalRepository.existsByTitleAndUserId(request.getTitle(), user.getId())) {
            throw new JournalAlreadyExistsException(request.getTitle());
        }

        // create journal payload and save the journal to the database
        Journal journal = Journal.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .userId(user.getId()).build();

        if (user.getSentimentAnalysis()) {
            if (request.getSentiment() == null) {
                throw new BadRequestException("Sentiment is required");
            }

            journal.setSentiment(request.getSentiment());
        }

        Journal createdJournal = journalRepository.save(journal);

        // add the journal to the user's journals
        user.getJournals().add(createdJournal);
        userRepository.save(user);

        return ApiResponse.success("Journal created successfully", createdJournal);
    }

    public ApiResponse<Journal> updateJournal(String id, UpdateJournalRequest request) {
        // check if user is authenticated
        String email = authService.getCurrentUserDetails().getUsername();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        // check if journal exists
        Journal existingJournal = journalRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new JournalNotFoundException(id));

        if (request.getTitle() == null && request.getContent() == null) {
            throw new BadRequestException(
                    "At least one field must be provided ['title', 'content']");
        }

        // check if title is provided
        if (request.getTitle() != null) {
            // check if title is already taken
            if (journalRepository.existsByTitleAndUserId(request.getTitle(), user.getId())) {
                throw new JournalAlreadyExistsException(request.getTitle());
            }

            existingJournal.setTitle(request.getTitle());
        }

        // check if content is provided
        if (request.getContent() != null) {
            existingJournal.setContent(request.getContent());
        }

        // check if sentiment is provided
        if (request.getSentiment() != null) {
            if (user.getSentimentAnalysis()) {
                existingJournal.setSentiment(request.getSentiment());
            } else {
                throw new BadRequestException("Sentiment analysis is disabled");
            }
        }

        Journal updatedJournal = journalRepository.save(existingJournal);

        return ApiResponse.success("Journal updated successfully", updatedJournal);
    }

    public ApiResponse<List<Journal>> getAllJournals() {
        // check if user is authenticated
        String email = authService.getCurrentUserDetails().getUsername();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        final List<Journal> journals = journalRepository.findAllByUserId(user.getId());

        return ApiResponse.success("Journals found successfully", journals);
    }

    public ApiResponse<Journal> getJournalById(String id) {
        // check if user is authenticated
        String email = authService.getCurrentUserDetails().getUsername();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        // check if journal exists
        Journal journal = journalRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new JournalNotFoundException(id));

        return ApiResponse.success("Journal found successfully", journal);
    }

    @Transactional
    public ApiResponse<Void> deleteJournal(String id) {
        // check if user is authenticated
        String email = authService.getCurrentUserDetails().getUsername();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        // check if journal exists
        journalRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new JournalNotFoundException(id));

        // delete journal from the database
        journalRepository.deleteByIdAndUserId(id, user.getId());

        // delete journal from the user's journals
        user.getJournals().removeIf(journal -> journal.getId().equals(id));
        userRepository.save(user);

        return ApiResponse.success("Journal deleted successfully");
    }
}