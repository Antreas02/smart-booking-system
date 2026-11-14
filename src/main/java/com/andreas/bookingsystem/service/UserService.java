package com.andreas.bookingsystem.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.andreas.bookingsystem.dto.LoginRequest;
import com.andreas.bookingsystem.model.User;
import com.andreas.bookingsystem.model.UserRole;
import com.andreas.bookingsystem.repository.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User registerUser(User newUser) {

        newUser.setId(null);

        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already registered.");
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRole(UserRole.USER);

        return userRepository.save(newUser);
    }

    public String loginUser(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new RuntimeException("Invalid email or password."));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password.");
        }

        return jwtService.generateToken(user.getEmail());
    }
}
