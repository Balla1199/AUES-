package com.aues.repositories;

import com.aues.entites.Payement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayementRepository extends JpaRepository<Payement, Integer> {
}
