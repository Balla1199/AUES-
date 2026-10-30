package com.aues.repositories;

import com.aues.entites.Payement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayementRepository extends JpaRepository<Payement, Integer> {

    /** Versements rattachés à une facture (trace des annulations incluse). */
    List<Payement> findByFactureId(Integer factureId);
}
