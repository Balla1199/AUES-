package com.aues.services;

import com.aues.entites.Role;
import com.aues.entites.Utilisateur;
import com.aues.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminInitializationService {

    private final UtilisateurRepository utilisateurRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminInitializationService(UtilisateurRepository utilisateurRepository, BCryptPasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void initializeAdmin() {
        // Vérifiez si un administrateur existe déjà
        if (!utilisateurRepository.existsByTelephone("0123456789")) {
            // Si non, créez un administrateur par défaut
            Utilisateur admin = new Utilisateur();
            admin.setNom("ADMIN");
            admin.setAdresse("Same");
            admin.setTelephone("0123456789");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMINISTRATEUR);
            //admin.setCompteur(null);

            utilisateurRepository.save(admin);
            System.out.println("Administrateur par défaut créé !");
        } else {
            System.out.println("L'administrateur par défaut existe déjà.");
        }
    }
}
