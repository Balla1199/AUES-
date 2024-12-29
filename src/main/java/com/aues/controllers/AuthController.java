package com.aues.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.aues.DTO.LoginRequest;
import com.aues.entites.JwtResponse;
import com.aues.repositories.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil; // Injection correcte de JwtUtil

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        String telephone = loginRequest.getTelephone();
        String motDePasse = loginRequest.getMotDePasse();

        // Authentification
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(telephone, motDePasse)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Génération du token JWT
        String jwt = jwtUtil.generateToken(telephone);

        // Retourne la réponse avec le token
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}
