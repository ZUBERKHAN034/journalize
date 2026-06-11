package com.journalize.journalize.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.journalize.journalize.entities.Journal;

public interface JournalRepository extends MongoRepository<Journal, String> {
    boolean existsByTitleAndUserId(String title, String userId);

    List<Journal> findAllByUserId(String userId);

    Optional<Journal> findByIdAndUserId(String id, String userId);

    long deleteAllByUserId(String userId);

    boolean deleteByIdAndUserId(String id, String userId);
}