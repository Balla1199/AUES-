package com.aues.services;

import com.aues.entites.Role;
import com.aues.entites.Utilisateur;
import com.aues.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    // Ajouter un utilisateur
    public Utilisateur ajouterUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    // Modifier un utilisateur
    public Utilisateur modifierUtilisateur(Integer id, Utilisateur utilisateurDetails) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        utilisateur.setNom(utilisateurDetails.getNom());
        utilisateur.setAdresse(utilisateurDetails.getAdresse());
        utilisateur.setTelephone(utilisateurDetails.getTelephone());
        utilisateur.setMotDePasse(utilisateurDetails.getMotDePasse());
        utilisateur.setRole(utilisateurDetails.getRole());

        return utilisateurRepository.save(utilisateur);
    }

    // Supprimer un utilisateur
    public void supprimerUtilisateur(Integer id) {
        utilisateurRepository.deleteById(id);
    }

    // Lister tous les utilisateurs
    public List<Utilisateur> listerUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    // Lister les utilisateurs par rôle
    public List<Utilisateur> listerUtilisateursParRole(Role role) {
        return utilisateurRepository.findByRole(role);
    }
}

