
package com.aues.services;

import com.aues.entites.Utilisateur;
import com.aues.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String telephone) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByTelephone(telephone)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec le téléphone : " + telephone));
    
        System.out.println("Utilisateur trouvé : " + utilisateur.getNom());
        System.out.println("Mot de passe haché : " + utilisateur.getMotDePasse());
        System.out.println("Rôle : " + utilisateur.getRole());
    
        return User.builder()
                .username(utilisateur.getTelephone())
                .password(utilisateur.getMotDePasse())
                .roles(utilisateur.getRole().name())
                .build();
    }
    
    
}
