package com.juanlopezaranzazu.springboot_oauth2.service;

import com.juanlopezaranzazu.springboot_oauth2.entity.Role;
import com.juanlopezaranzazu.springboot_oauth2.entity.User;
import com.juanlopezaranzazu.springboot_oauth2.exception.ResourceNotFoundException;
import com.juanlopezaranzazu.springboot_oauth2.repository.RoleRepository;
import com.juanlopezaranzazu.springboot_oauth2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauthUser = super.loadUser(userRequest);
        // obtener el email y el nombre del usuario
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        // verificar el rol
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Rol USER no encontrado"));

        // validar si el usuario existe
        User user = userRepository.findByUsername(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(email);
                    newUser.setName(name);
                    newUser.setRoles(Set.of(userRole));
                    // guardar el usuario
                    return userRepository.save(newUser);
                });

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oauthUser.getAttributes(),
                "email"
        );
    }
}
