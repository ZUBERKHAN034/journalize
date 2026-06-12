package com.journalize.journalize.services;

import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.journalize.journalize.enums.Role;
import com.journalize.journalize.Utils.Weatherstack;
import com.journalize.journalize.cache.AppCache;
import com.journalize.journalize.dto.ApiResponse;
import com.journalize.journalize.dto.user.CreateUserRequest;
import com.journalize.journalize.dto.user.UpdateUserRequest;
import com.journalize.journalize.dto.weather.WeatherResponse;
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
    private final Weatherstack weatherstack;
    private final AppCache appCache;

    public ApiResponse<User> createUser(CreateUserRequest request) {
        // Check if user with the same email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        // Create user payload
        User user = User.builder().email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .roles(Set.of(request.getRole()))
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        // Setting the sentiment analysis
        if (request.getSentimentAnalysis() != null) {
            user.setSentimentAnalysis(request.getSentimentAnalysis());
        }

        // save the user to the database
        User createdUser = userRepository.save(user);

        return ApiResponse.success("User created successfully", createdUser);
    }

    public ApiResponse<User> updateUser(String id, UpdateUserRequest request) {
        String email = authService.getCurrentUserDetails().getUsername();
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if (request.getEmail() != null) {

            if (user.getEmail().equals(request.getEmail())) {
                throw new BadRequestException("Email is same as current email");
            }

            if (userRepository.existsByEmail(request.getEmail())) {
                throw new UserAlreadyExistsException(request.getEmail());
            }

            user.setEmail(request.getEmail());
        }

        if (request.getRole() != null) {
            if (user.getEmail().equals(email) && request.getRole() == Role.USER) {
                throw new BadRequestException("You cannot update your role to 'USER'");
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

    public ApiResponse<List<User>> getUsers() {
        List<User> users = userRepository.findAll();
        return ApiResponse.success("Users found successfully", users);
    }

    public ApiResponse<User> getUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return ApiResponse.success("User found successfully", user);
    }

    @Transactional
    public ApiResponse<Void> deleteUser(String id) {
        String email = authService.getCurrentUserDetails().getUsername();
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        // Check if the admin is deleting itself
        if (user.getEmail().equals(email)) {
            throw new BadRequestException("You cannot delete yourself");
        }

        // Check and delete all journals created by the user
        journalRepository.deleteAllByUserId(user.getId());

        // Delete the user
        userRepository.delete(user);

        return ApiResponse.success("User deleted successfully");
    }

    public ApiResponse<WeatherResponse.Current> weather(String city) {
        var weather = weatherstack.getWeather(city);
        if (weather == null) {
            throw new BadRequestException("Weather not fetched, try again later");
        }

        return ApiResponse.success("Weather fetched successfully", weather);
    }

    public ApiResponse<Void> refreshAppCache() {
        appCache.init();
        return ApiResponse.success("App cache refreshed successfully");
    }
}