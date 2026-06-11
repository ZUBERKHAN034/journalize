package com.journalize.journalize.services;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.journalize.journalize.dto.ApiResponse;
import com.journalize.journalize.dto.user.UpdateUserRequest;
import com.journalize.journalize.entities.User;
import com.journalize.journalize.enums.Role;
import com.journalize.journalize.exceptions.BadRequestException;
import com.journalize.journalize.exceptions.user.UserAlreadyExistsException;
import com.journalize.journalize.exceptions.user.UserNotFoundException;
import com.journalize.journalize.repositories.JournalRepository;
import com.journalize.journalize.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JournalRepository journalRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse<User> updateUser(String id, UpdateUserRequest request) {
        String email = authService.getCurrentUserDetails().getUsername();
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        // Check if the user is not same
        if (!user.getEmail().equals(email)) {
            throw new BadRequestException("You cannot update another user");
        }

        if (request.getEmail() != null) {

            if (user.getEmail().equals(request.getEmail())) {
                throw new BadRequestException("Email is same as current email");
            }

            if (userRepository.existsByEmail(request.getEmail())) {
                throw new UserAlreadyExistsException(email);
            }

            user.setEmail(request.getEmail());
        }

        if (request.getRole() != null) {
            if (request.getRole() == Role.ADMIN) {
                throw new BadRequestException("You cannot update your role to 'ADMIN'");
            }

            user.setRoles(Set.of(request.getRole()));
        }

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getSentimentAnalysis() != null) {
            user.setSentimentAnalysis(request.getSentimentAnalysis());
        }

        // save the user to the database
        User updatedUser = userRepository.save(user);

        return ApiResponse.success("User updated successfully", updatedUser);
    }

    public ApiResponse<User> getUser(String id) {
        String email = authService.getCurrentUserDetails().getUsername();
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        // Check if the user is not same
        if (!user.getEmail().equals(email)) {
            throw new BadRequestException("You cannot get another user");
        }

        return ApiResponse.success("User found successfully", user);
    }

    @Transactional
    public ApiResponse<Void> deleteUser(String id) {
        String email = authService.getCurrentUserDetails().getUsername();
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        // Check if the user is not same
        if (!user.getEmail().equals(email)) {
            throw new BadRequestException("You cannot delete another user");
        }

        // Check and delete all journals created by the user
        journalRepository.deleteAllByUserId(user.getId());

        // Delete the user
        userRepository.delete(user);

        return ApiResponse.success("User deleted successfully");
    }
}