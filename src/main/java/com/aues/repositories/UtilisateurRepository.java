package com.aues.repositories;
import com.aues.entites.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aues.entites.Role;
import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    List<Utilisateur> findByRole(Role role); // Recherche par rôle
    Optional<Utilisateur> findByTelephone(String telephone);

}
