package com.aues.repositories;

import com.aues.entites.Compteur;
import org.springframework.data.repository.CrudRepository;

public interface CompteurRepository extends CrudRepository<Compteur, Integer> {
    Compteur findByNumero(String numeroCompteur);
}
