package com.juanlopezaranzazu.springboot_oauth2.controller;

import com.juanlopezaranzazu.springboot_oauth2.dto.AuthRequest;
import com.juanlopezaranzazu.springboot_oauth2.dto.AuthResponse;
import com.juanlopezaranzazu.springboot_oauth2.dto.UserRequest;
import com.juanlopezaranzazu.springboot_oauth2.dto.UserResponse;
import com.juanlopezaranzazu.springboot_oauth2.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    // login de usuarios
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.login(authRequest));
    }

    // registro de usuarios
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(authService.register(userRequest));
    }
}
