package com.journalize.journalize.exceptions.journal;

public class JournalAlreadyExistsException extends RuntimeException {
    public JournalAlreadyExistsException(String title) {
        super("Journal already exists with title: " + title);
    }
}