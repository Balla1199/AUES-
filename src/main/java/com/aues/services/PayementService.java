package com.aues.services;

import com.aues.ModePayement;
import com.aues.entites.Facture;
import com.aues.entites.Payement;
import com.aues.repositories.FactureRepository;
import com.aues.repositories.PayementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Enregistrement des paiements : partiels ou complets.
 * Le statut de la facture est recalculé à chaque versement.
 */
@Service
public class PayementService {

    @Autowired
    private PayementRepository payementRepository;

    @Autowired
    private FactureRepository factureRepository;

    /** Enregistre un versement et met la facture à jour (aucun versement nul). */
    @Transactional
    public Payement enregistrer(int factureId, Double montant, ModePayement mode, String payeur) {
        Facture facture = factureRepository.findById(factureId).orElse(null);
        if (facture == null) {
            throw new RuntimeException("Aucune facture trouvée avec l'ID " + factureId + ".");
        }
        if (montant == null || montant <= 0) {
            throw new RuntimeException("Le montant du paiement doit être strictement positif.");
        }
        if (facture.getStatut() == com.aues.StatutFacture.Payee) {
            throw new RuntimeException("La facture " + facture.getNumero() + " est déjà entièrement payée.");
        }
        if (montant > facture.montantRestant()) {
            throw new RuntimeException("Le montant dépasse le reste à payer ("
                    + facture.montantRestant() + " FCFA) pour la facture " + facture.getNumero() + ".");
        }

        Payement payement = new Payement();
        payement.setFacture(facture);
        payement.setMontant(montant);
        if (mode != null) {
            payement.setMode(mode);
        }
        payement.setPayeur(payeur);

        facture.setMontant_paye(facture.getMontant_paye() + montant);
        facture.recalculerStatut();
        factureRepository.save(facture);

        return payementRepository.save(payement);
    }

    public List<Payement> afficherPayements() {
        return (List<Payement>) payementRepository.findAll();
    }

    public List<Payement> afficherParFacture(int factureId) {
        return payementRepository.findByFactureId(factureId);
    }

    public Payement chercher(int id) {
        return payementRepository.findById(id).orElse(null);
    }

    public Payement supprimer(int id) {
        Payement payement = chercher(id);
        if (payement != null) {
            payementRepository.delete(payement);
        }
        return payement;
    }
}
