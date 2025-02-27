package com.juanlopezaranzazu.springboot_oauth2.service;

import com.juanlopezaranzazu.springboot_oauth2.dto.UserResponse;
import com.juanlopezaranzazu.springboot_oauth2.entity.User;
import com.juanlopezaranzazu.springboot_oauth2.exception.ResourceNotFoundException;
import com.juanlopezaranzazu.springboot_oauth2.repository.UserRepository;
import com.juanlopezaranzazu.springboot_oauth2.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserResponse getUser() {
        // obtener el usuario autenticado
        String username = AuthUtil.getAuthenticatedUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return UserResponse.fromEntity(user);
    }
}
