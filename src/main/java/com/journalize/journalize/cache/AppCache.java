package com.journalize.journalize.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.journalize.journalize.entities.Config;
import com.journalize.journalize.repositories.ConfigRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppCache {

    private final ConfigRepository configRepository;

    private Map<String, String> appCache;

    public String get(String key) {
        return appCache.get(key);
    }

    @PostConstruct
    public void init() {
        appCache = new HashMap<>();

        List<Config> configs = configRepository.findAll();

        for (Config config : configs) {
            appCache.put(config.getKey(), config.getValue());
        }
    }
}