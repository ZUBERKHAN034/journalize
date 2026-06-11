package com.journalize.journalize.dto.user;

import com.journalize.journalize.enums.Role;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {

    private Role role;

    private String firstName;

    private String lastName;

    @Email(message = "Email is invalid")
    private String email;

    private String password;

    private Boolean sentimentAnalysis;
}