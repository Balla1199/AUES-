package com.aues.repositories;

import com.aues.entites.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactureRepository extends JpaRepository<Facture, Integer> {

    /** Une seule facture par relevé. */
    boolean existsByReleveId(Integer releveId);

    /** Recherche par numéro lisible (FACT-CPT001-42). */
    Facture findByNumero(String numero);
}
