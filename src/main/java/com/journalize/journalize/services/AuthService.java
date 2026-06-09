package com.journalize.journalize.services;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.journalize.journalize.dto.ApiResponse;
import com.journalize.journalize.dto.auth.LoginRequest;
import com.journalize.journalize.dto.auth.TokenResponse;
import com.journalize.journalize.dto.auth.RegisterRequest;
import com.journalize.journalize.entities.User;
import com.journalize.journalize.exceptions.user.UserAlreadyExistsException;
import com.journalize.journalize.repositories.UserRepository;
import com.journalize.journalize.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public ApiResponse<User> register(RegisterRequest request) {
        // Check if user with the same email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        // Create user payload
        User user = User.builder().email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        // save the user to the database
        User registeredUser = userRepository.save(user);

        return ApiResponse.success("User registered successfully", registeredUser);
    }

    public ApiResponse<TokenResponse> login(LoginRequest request) {
        // Authenticate the user
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        // Get the authenticated user details
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Generate the JWT token
        String tokenValue = jwtService.generateToken(userDetails.getUsername());
        TokenResponse token = TokenResponse.builder().token(tokenValue).build();

        return ApiResponse.success("User logged in successfully", token);
    }

    protected UserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails;
    }
}