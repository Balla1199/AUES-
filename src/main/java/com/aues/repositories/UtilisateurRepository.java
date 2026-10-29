package com.aues.repositories;

import com.aues.entites.Utilisateur;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UtilisateurRepository extends CrudRepository<Utilisateur,Integer> {
    Optional<Utilisateur> findByEmail(String email);
    boolean existsByEmail(String email);

}
