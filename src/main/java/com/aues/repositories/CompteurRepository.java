package com.aues.repositories;

import com.aues.TypeStatut;
import com.aues.entites.Compteur;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompteurRepository extends CrudRepository<Compteur, Integer> {
      Compteur findCompteurByCode(String numero);
      Optional<Compteur> findByCodeAndStatut(String numero, TypeStatut statut);
}
