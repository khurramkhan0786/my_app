package com.example.notes_api.controller;

import com.example.notes_api.dto.*;
import com.example.notes_api.model.User;
import com.example.notes_api.repository.UserRepository;
import com.example.notes_api.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request) {
        String token = authService.signup(request);
        User user = userRepository.findByEmail(request.getEmail()).get();
        return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), user.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        User user = userRepository.findByEmail(request.getEmail()).get();
        return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), user.getUsername()));
    }
}

