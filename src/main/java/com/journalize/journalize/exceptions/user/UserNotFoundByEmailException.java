package com.journalize.journalize.exceptions.user;

public class UserNotFoundByEmailException extends RuntimeException {
    public UserNotFoundByEmailException(String email) {
        super("User not found with email: " + email);
    }
}