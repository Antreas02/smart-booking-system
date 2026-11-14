package com.andreas.bookingsystem.controller;

import org.springframework.web.bind.annotation.*;

import com.andreas.bookingsystem.model.User;
import com.andreas.bookingsystem.dto.LoginRequest;
import com.andreas.bookingsystem.dto.LoginResponse;
import com.andreas.bookingsystem.dto.UserResponse;
import com.andreas.bookingsystem.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponse registerUser(@RequestBody User newUser) {

        User savedUser = userService.registerUser(newUser);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getRole());
    }

    @PostMapping("/login")
    public LoginResponse loginUser(@RequestBody LoginRequest loginRequest) {

        String token = userService.loginUser(loginRequest);

        return new LoginResponse(token);
    }
}
