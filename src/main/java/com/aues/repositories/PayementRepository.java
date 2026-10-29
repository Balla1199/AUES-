package com.aues.repositories;

import com.aues.entites.Payement;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PayementRepository extends CrudRepository<Payement, Integer> {

    List<Payement> findByFactureId(Integer factureId);
}
