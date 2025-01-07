package com.aues.services;

import com.aues.entites.Utilisateur;
import com.aues.repositories.UtilisateurRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UtilisateurService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public Utilisateur loadUserByUsername(String username) throws UsernameNotFoundException {
        return utilisateurRepository.findByTelephone(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec le numéro de téléphone : " + username));
    }

    // Méthode pour ajouter un utilisateur
    public Utilisateur ajouterUtilisateur(Utilisateur utilisateur) {
        Optional<Utilisateur> optional = utilisateurRepository.findByTelephone(utilisateur.getTelephone());

        if (optional.isPresent()) {
            throw new IllegalArgumentException("L'utilisateur avec le téléphone " + utilisateur.getTelephone() + " existe déjà.");
        }

        return utilisateurRepository.save(utilisateur);
    }


    // Autres méthodes comme la mise à jour et la suppression peuvent être ajoutées


}
