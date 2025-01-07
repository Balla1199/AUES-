package com.aues.controllers;

import com.aues.DTO.AuthenticationDTO;
import com.aues.entites.Role;
import com.aues.entites.Utilisateur;
import com.aues.repositories.UtilisateurRepository;
import com.aues.securite.JwtService;
import com.aues.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
//@RequestMapping("/api")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService, JwtService jwtService, AuthenticationManager authenticationManager, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.utilisateurService = utilisateurService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/connexion")
    public ResponseEntity<Map<String, String>> connexion(@RequestBody AuthenticationDTO authenticationDTO) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationDTO.username(), authenticationDTO.password())
        );

        if (authenticate.isAuthenticated()) {
            Map<String, String> jwt = jwtService.generate(authenticationDTO.username());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/inscrire")
    public ResponseEntity<String> inscrire(@RequestBody Utilisateur utilisateur) {

        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Vérifier si l'utilisateur connecté est un administrateur
        Utilisateur administrateur = utilisateurService.loadUserByUsername(userDetails.getUsername());
        if (administrateur.getRole() != Role.ADMINISTRATEUR) {
            return new ResponseEntity<>("Vous n'êtes pas autorisé à effectuer cette action.", HttpStatus.FORBIDDEN);
        }

        utilisateur.setPassword(bCryptPasswordEncoder.encode(utilisateur.getPassword()));
        utilisateurService.ajouterUtilisateur(utilisateur);
        return new ResponseEntity<>("Utilisateur inscrit avec succès.", HttpStatus.CREATED);
    }
}
