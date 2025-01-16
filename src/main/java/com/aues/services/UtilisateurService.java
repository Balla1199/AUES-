package com.aues.services;

import com.aues.DTO.UtilisateurDTO;
import com.aues.TypeStatut;
import com.aues.entites.Compteur;
import com.aues.entites.Utilisateur;
import com.aues.repositories.CompteurRepository;
import com.aues.repositories.UtilisateurRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;
    private final CompteurRepository compteurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository, CompteurRepository compteurRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.compteurRepository = compteurRepository;
    }

    @Override
    public Utilisateur loadUserByUsername(String username) throws UsernameNotFoundException {
        return utilisateurRepository.findByTelephone(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec le numéro de téléphone : " + username));
    }

    // Méthode pour ajouter un utilisateur
    public Utilisateur ajouterUtilisateur(UtilisateurDTO utilisateurDTO) {
        Optional<Utilisateur> optional = utilisateurRepository.findByTelephone(utilisateurDTO.getTelephone());

        if (optional.isPresent()) {
            throw new IllegalArgumentException("L'utilisateur avec le téléphone " + utilisateurDTO.getTelephone() + " existe déjà.");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(utilisateurDTO.getNom());
        utilisateur.setAdresse(utilisateurDTO.getAdresse());
        utilisateur.setTelephone(utilisateurDTO.getTelephone());
        utilisateur.setPassword(utilisateurDTO.getPassword());
        utilisateur.setRole(utilisateurDTO.getRole());
//
//        Compteur optional1= compteurRepository.findCompteurByCode(utilisateurDTO.getNumeroCompteur());
//        utilisateur.setCompteurs(optional1.getUtilisateur().getCompteurs());
        if (utilisateur.getCompteurs() == null) {
            utilisateur.setCompteurs(new ArrayList<>());
        }

        if (utilisateurDTO.getNumeroCompteur() != null && !utilisateurDTO.getNumeroCompteur().isEmpty()) {
            Compteur compteur = compteurRepository.findByCodeAndStatut(utilisateurDTO.getNumeroCompteur(), TypeStatut.Inactive)
                    .orElseThrow(() -> new IllegalArgumentException("Aucun compteur inactif trouvé avec le numéro : " + utilisateurDTO.getNumeroCompteur()));

            compteur.setStatut(TypeStatut.Active); // Mettre le compteur en actif
            compteur.setUtilisateur(utilisateur); // Associer le compteur à l'utilisateur
            utilisateur.getCompteurs().add(compteur); // Ajouter le compteur à l'utilisateur
        }

        return utilisateurRepository.save(utilisateur);
    }


    // Autres méthodes comme la mise à jour et la suppression peuvent être ajoutées
    public List<Utilisateur> afficher(){
        return  this.utilisateurRepository.findAll();
    }

}
