package com.aues.services.Interface;

import com.aues.DTO.FactureDto;
import com.aues.entites.Facture;
import com.aues.entites.Payement;

import java.util.List;

public interface FactureInterf {

    FactureDto save(FactureDto factureDto);

    /** Génère une facture depuis un relevé (une seule facture par relevé). */
    FactureDto generer(Integer releveId);

    FactureDto findByid(Integer id);

    List<FactureDto> findAll();

    void delete(Integer id);

    // Accès « entités brutes » pour les vues web (Thymeleaf)
    List<Facture> listerEntites();

    Facture chercherEntite(Integer id);

    List<Payement> listerPayementsParFacture(Integer factureId);
}
