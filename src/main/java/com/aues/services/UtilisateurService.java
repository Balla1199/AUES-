package com.aues.services;

import com.aues.entites.Utilisateur;
import com.aues.entites.Role;
import com.aues.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class UtilisateurService implements UserDetailsService {
    public UtilisateurRepository utilisateurRepository;
    public BCryptPasswordEncoder bCryptPasswordEncoder;
    public UtilisateurService() {
    }
    @Autowired
    public UtilisateurService(UtilisateurRepository utilisateurRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }



    public void ajouterUtilisateur(Utilisateur utilisateur){
        if (!utilisateur.getEmail().contains("@") || !utilisateur.getEmail().contains(".")) {
            throw new RuntimeException("Votre mail est invalide");
        }
        Optional<Utilisateur> utilisateurOptional = this.utilisateurRepository.findByEmail(utilisateur.getEmail());
        if (utilisateurOptional.isPresent()) {
            throw new RuntimeException("Votre mail est déjà utilisé");
        }
        utilisateur.setRole(Role.ADMINISTRATEUR);
        String crypt= bCryptPasswordEncoder.encode(utilisateur.getMdp());
        utilisateur.setMdp(crypt);
        this.utilisateurRepository.save(utilisateur);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email));
        return utilisateur;
    }
}
