package com.journalize.journalize.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.journalize.journalize.dto.ApiResponse;
import com.journalize.journalize.dto.admin.CreateUserRequest;
import com.journalize.journalize.dto.admin.UpdateUserRequest;
import com.journalize.journalize.dto.weather.WeatherResponse;
import com.journalize.journalize.entities.User;
import com.journalize.journalize.services.AdminService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/create-user")
    public ResponseEntity<ApiResponse<?>> createUser(@Valid @RequestBody CreateUserRequest request) {
        ApiResponse<User> response = adminService.createUser(request);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<ApiResponse<?>> updateUser(@PathVariable String id,
            @Valid @RequestBody UpdateUserRequest request) {
        ApiResponse<User> response = adminService.updateUser(id, request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/all-users")
    public ResponseEntity<ApiResponse<?>> getUsers() {
        ApiResponse<List<User>> response = adminService.getUsers();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/get-user/{id}")
    public ResponseEntity<ApiResponse<?>> getUser(@PathVariable String id) {
        ApiResponse<User> response = adminService.getUser(id);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("delete-user/{id}")
    public ResponseEntity<ApiResponse<?>> deteteUser(@PathVariable String id) {
        ApiResponse<Void> response = adminService.deteteUser(id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/weather/{city}")
    public ResponseEntity<ApiResponse<?>> weather(@PathVariable String city) {
        ApiResponse<WeatherResponse.Current> response = adminService.weather(city);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/refresh-app-cache")
    public ResponseEntity<ApiResponse<?>> refreshAppCache() {
        ApiResponse<Void> response = adminService.refreshAppCache();
        return ResponseEntity.ok().body(response);
    }
}