package com.juanlopezaranzazu.springboot_oauth2.service;

import com.juanlopezaranzazu.springboot_oauth2.entity.UserPrincipal;
import com.juanlopezaranzazu.springboot_oauth2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // verificar si el usuario existe
        return userRepository.findByUsername(username)
                .map(UserPrincipal::new)
                .orElseThrow(() -> {
                    System.out.println("Usuario no encontrado");
                    return new UsernameNotFoundException("Usuario no encontrado");
                });
    }
}
