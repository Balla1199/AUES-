package com.aues.services.Interface;

import com.aues.DTO.PayementDto;
import com.aues.entites.Payement;

import java.util.List;

public interface PayementInterf {

    PayementDto save(PayementDto payementDto);

    PayementDto findByid(Integer id);

    /** Annule un versement : trace conservée (StatutPay.Annule), reste recalculé. */
    PayementDto AnnulerByid(Integer id);

    List<PayementDto> findAll();

    void delete(Integer id);

    // Accès « entités brutes » pour les vues web (Thymeleaf)
    List<Payement> listerEntites();

    List<Payement> listerEntitesParFacture(Integer factureId);
}
