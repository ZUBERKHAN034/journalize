package com.journalize.journalize.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.journalize.journalize.dto.ApiResponse;
import com.journalize.journalize.dto.auth.LoginRequest;
import com.journalize.journalize.dto.auth.TokenResponse;
import com.journalize.journalize.dto.auth.RegisterRequest;
import com.journalize.journalize.entities.User;
import com.journalize.journalize.services.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterRequest request) {
        final ApiResponse<User> response = authService.register(request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginRequest request) {
        final ApiResponse<TokenResponse> response = authService.login(request);
        return ResponseEntity.ok().body(response);
    }

}