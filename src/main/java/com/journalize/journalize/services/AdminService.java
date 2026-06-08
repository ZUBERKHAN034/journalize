package com.journalize.journalize.services;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.journalize.journalize.dto.ApiResponse;
import com.journalize.journalize.dto.admin.CreateUserRequest;
import com.journalize.journalize.dto.admin.UpdateUserRequest;
import com.journalize.journalize.entities.User;
import com.journalize.journalize.exceptions.BadRequestException;
import com.journalize.journalize.exceptions.user.UserAlreadyExistsException;
import com.journalize.journalize.exceptions.user.UserNotFoundException;
import com.journalize.journalize.repositories.JournalRepository;
import com.journalize.journalize.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final JournalRepository journalRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    public ApiResponse<User> createUser(CreateUserRequest request) {
        // Check if user with the same email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        // Create user payload
        User user = User.builder().email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .roles(List.of(request.getRole().toString()))
                .build();
        // save the user to the database
        User createdUser = userRepository.save(user);
        
        return ApiResponse.success("User created successfully", createdUser);
    }

    public ApiResponse<User> updateUser(String id, UpdateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        if (request.getRole() != null) {
            user.setRoles(List.of(request.getRole().toString()));
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        // save the user to the database
        User updatedUser = userRepository.save(user);

        return ApiResponse.success("User updated successfully", updatedUser);
    }

    public ApiResponse<List<User>> getUsers() {
        List<User> users = userRepository.findAll();
        return ApiResponse.success("Users found successfully", users);
    }

    public ApiResponse<User> getUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return ApiResponse.success("User found successfully", user);
    }

    @Transactional
    public ApiResponse<Void> deteteUser(String id) {
        String email = authService.getCurrentUserDetails().getUsername();
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        // Check if the admin is deleting itself
        if (user.getEmail().equals(email)) {
            throw new BadRequestException("You cannot delete yourself");
        }

        // Check and delete all journals created by the user
        journalRepository.deleteAllByUserId(user.getId());
        userRepository.delete(user);

        return ApiResponse.success("User deleted successfully");
    }
}