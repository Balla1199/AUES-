package com.aues.repositories;

import com.aues.StatutFacture;
import com.aues.entites.Facture;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FactureRepository extends CrudRepository<Facture, Integer> {

    Optional<Facture> findByNumero(String numero);

    List<Facture> findByStatut(StatutFacture statut);

    /** Une même facture ne peut pas être générée deux fois pour un relevé. */
    boolean existsByReleveId(Integer releveId);
}
