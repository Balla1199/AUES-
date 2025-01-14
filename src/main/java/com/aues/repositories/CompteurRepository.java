package com.aues.repositories;

import com.aues.entites.Compteur;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompteurRepository extends CrudRepository<Compteur, Integer> {
      Compteur findCompteurByCode(String numero);
}
