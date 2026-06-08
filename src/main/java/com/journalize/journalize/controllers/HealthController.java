package com.journalize.journalize.controllers;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public String health() {
        return LocalDateTime.now().toString() + " - OK";
    }
}