package com.aues.controllers;

import com.aues.AuthenticationDTO;
import com.aues.entites.Utilisateur;
import com.aues.securite.JwtService;
import com.aues.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController

public class UtilisateurController {
    private UtilisateurService utilisateurService;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;

    public UtilisateurController() {
    }
    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.utilisateurService = utilisateurService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }



    @PostMapping("/inscription")
    public ResponseEntity<String> ajouter(@RequestBody Utilisateur utilisateur) {
        this.utilisateurService.ajouterUtilisateur(utilisateur);
        return ResponseEntity.status(HttpStatus.CREATED).body("Utilisateur a été ajouté avec succès.");
    }
    @PostMapping("/connexion")
    public Map<String,String> connexion(@RequestBody AuthenticationDTO authenticationDTO){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationDTO.username(), authenticationDTO.password())
        );
        if (authenticate.isAuthenticated()){
            return this.jwtService.generate(authenticationDTO.username());
        }

        return null;
    }
}
