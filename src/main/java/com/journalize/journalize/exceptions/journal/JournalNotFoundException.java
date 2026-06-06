package com.journalize.journalize.exceptions.journal;

public class JournalNotFoundException extends RuntimeException {
    public JournalNotFoundException(String id) {
        super("Journal not found with id: " + id);
    }
}