package com.andreas.bookingsystem.dto;

public record LoginRequest(
        String email,
        String password) {
}
