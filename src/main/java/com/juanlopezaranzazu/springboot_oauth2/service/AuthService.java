package com.juanlopezaranzazu.springboot_oauth2.service;

import com.juanlopezaranzazu.springboot_oauth2.dto.AuthRequest;
import com.juanlopezaranzazu.springboot_oauth2.dto.AuthResponse;
import com.juanlopezaranzazu.springboot_oauth2.dto.UserRequest;
import com.juanlopezaranzazu.springboot_oauth2.dto.UserResponse;
import com.juanlopezaranzazu.springboot_oauth2.entity.Role;
import com.juanlopezaranzazu.springboot_oauth2.exception.BadRequestException;
import com.juanlopezaranzazu.springboot_oauth2.exception.ResourceNotFoundException;
import com.juanlopezaranzazu.springboot_oauth2.repository.RoleRepository;
import com.juanlopezaranzazu.springboot_oauth2.repository.UserRepository;
import com.juanlopezaranzazu.springboot_oauth2.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Service
public class AuthService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    // login de usuarios con jwt
    public AuthResponse login(AuthRequest authRequest){
        // verificar las credenciales
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );
        if (authentication.isAuthenticated()) {
            // generar token
            String token = jwtService.generateToken(authRequest.getUsername());;
            return new AuthResponse(token);
        } else {
            throw new UsernameNotFoundException("Usuario incorrecto");
        }
    }

    // registro de usuario con jwt
    public UserResponse register(UserRequest userRequest){
        // verificar el username
        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            throw new BadRequestException("El usuario ya existe");
        }

        // verificar el rol
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Rol USER no encontrado"));

        // crear el usuario
        User newUser = new User();
        newUser.setName(userRequest.getName());
        newUser.setUsername(userRequest.getUsername());
        // encriptar contraseña
        newUser.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));
        // asignar rol
        newUser.setRoles(Set.of(userRole));
        // guardar usuario
        userRepository.save(newUser);

        return UserResponse.fromEntity(newUser);
    }
}
