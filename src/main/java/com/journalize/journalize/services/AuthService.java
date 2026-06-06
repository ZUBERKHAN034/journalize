package com.journalize.journalize.services;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.journalize.journalize.dto.ApiResponse;
import com.journalize.journalize.dto.auth.LoginRequest;
import com.journalize.journalize.dto.auth.Token;
import com.journalize.journalize.dto.auth.RegisterRequest;
import com.journalize.journalize.entities.User;
import com.journalize.journalize.exceptions.user.UserAlreadyExistsException;
import com.journalize.journalize.exceptions.user.UserNotFoundByEmailException;
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
        try {
            // Check if user with the same email already exists
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new UserAlreadyExistsException(request.getEmail());
            }

            // Create and save the user
            final User user = User.builder().email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .roles(List.of("USER"))
                    .build();

            // save the user to the database
            final User registeredUser = userRepository.save(user);

            return ApiResponse.success("User registered successfully", registeredUser);
        } catch (Exception e) {
            throw e;
        }
    }

    public ApiResponse<Token> login(LoginRequest request) {
        try {
            // check if user exists
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UserNotFoundByEmailException(request.getEmail()));

            // authenticates email + password — throws if invalid
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));

            // generate token and build payload
            String generatedToken = jwtService.generateToken(user.getEmail());
            final Token token = Token.builder().token(generatedToken).build();

            return ApiResponse.success("User logged in successfully", token);
        } catch (Exception e) {
            throw e;

        }
    }
}