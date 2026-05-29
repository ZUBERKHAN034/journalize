package com.journalize.journalize.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.journalize.journalize.entities.Journal;

@Service
@RequiredArgsConstructor
public class JournalService {

    private Map<Long, Journal> journals = new HashMap<>();

    public Journal createJournal(Journal journal) {
        journals.put(journal.getId(), journal);
        return journal;
    }

    public Journal updateJournal(Long id, Journal journal) {
        journals.put(id, journal);
        return journal;
    }

    public List<Journal> getAllJournals() {
        final var journalsList = List.copyOf(journals.values());
        return journalsList;
    }

    public Journal getJournalById(Long id) {
        final var journal = journals.get(id);
        return journal;
    }

    public void deleteJournal(Long id) {
        journals.remove(id);
    }
}