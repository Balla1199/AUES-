package com.aues.repositories;

import com.aues.entites.Releve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReleveRepository extends JpaRepository<Releve, Integer> {
}
