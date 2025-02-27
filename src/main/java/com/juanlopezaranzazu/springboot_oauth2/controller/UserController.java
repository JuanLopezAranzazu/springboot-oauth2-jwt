package com.juanlopezaranzazu.springboot_oauth2.controller;

import com.juanlopezaranzazu.springboot_oauth2.dto.UserResponse;
import com.juanlopezaranzazu.springboot_oauth2.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    // obtener el usuario autenticado
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponse> getUser() {
        return ResponseEntity.ok(userService.getUser());
    }

    // solo rol ROLE_ADMIN
    @GetMapping("/message1")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getMessage1() {
        return "Usuario con rol ROLE_ADMIN";
    }

    // solo rol ROLE_USER
    @GetMapping("/message2")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getMessage2() {
        return "Usuario con rol ROLE_USER";
    }
}
