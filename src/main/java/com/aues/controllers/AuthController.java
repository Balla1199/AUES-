package com.aues.controllers;

import com.aues.DTO.LoginRequest;
import com.aues.entites.JwtResponse;
import com.aues.entites.Utilisateur;
import com.aues.repositories.JwtUtil;
import com.aues.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder; // Importer PasswordEncoder
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Injecter PasswordEncoder

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        String telephone = loginRequest.getTelephone();
        String motDePasse = loginRequest.getMotDePasse();

        // Vérifier si l'utilisateur existe avec ce numéro de téléphone
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findByTelephone(telephone);
        if (utilisateurOptional.isEmpty()) {
            return ResponseEntity.status(401).body(new JwtResponse("Identifiants incorrects"));
        }

        Utilisateur utilisateur = utilisateurOptional.get();

        // Vérifier le mot de passe de manière sécurisée
        if (!passwordEncoder.matches(motDePasse, utilisateur.getMotDePasse())) {
            return ResponseEntity.status(401).body(new JwtResponse("Identifiants incorrects"));
        }

        // Authentification
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(telephone, motDePasse)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Génération du jeton JWT
        String jwt = jwtUtil.generateToken(telephone);

        // Retourner la réponse avec le token
        return ResponseEntity.ok(new JwtResponse(jwt, "Connexion réussie"));
    }
}
