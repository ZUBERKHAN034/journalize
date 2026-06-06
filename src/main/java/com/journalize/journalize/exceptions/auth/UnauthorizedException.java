package com.journalize.journalize.exceptions.auth;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("You are not authorized");
    }
}