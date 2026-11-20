package com.andreas.bookingsystem.dto;

import com.andreas.bookingsystem.model.UserRole;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        UserRole role) {
}
