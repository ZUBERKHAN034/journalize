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
        final ApiResponse<User> response = adminService.createUser(request);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<ApiResponse<?>> updateUser(@PathVariable String id,
            @Valid @RequestBody UpdateUserRequest request) {
        final ApiResponse<User> response = adminService.updateUser(id, request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/all-users")
    public ResponseEntity<ApiResponse<?>> getUsers() {
        final ApiResponse<List<User>> response = adminService.getUsers();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/get-user/{id}")
    public ResponseEntity<ApiResponse<?>> getUser(@PathVariable String id) {
        final ApiResponse<User> response = adminService.getUser(id);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("delete-user/{id}")
    public ResponseEntity<ApiResponse<?>> deteteUser(@PathVariable String id) {
        final ApiResponse<Void> response = adminService.deteteUser(id);
        return ResponseEntity.ok().body(response);
    }
}