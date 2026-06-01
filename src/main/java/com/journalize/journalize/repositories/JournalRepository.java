package com.journalize.journalize.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.journalize.journalize.entities.Journal;

@Repository
public interface JournalRepository extends MongoRepository<Journal, String> {
    boolean existsByTitle(String title);
}