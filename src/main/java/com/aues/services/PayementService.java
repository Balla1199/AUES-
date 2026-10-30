package com.aues.services;

import com.aues.DTO.PayementDto;
import com.aues.Verify.PayementVerify;
import com.aues.entites.Facture;
import com.aues.entites.ModePayement;
import com.aues.entites.Payement;
import com.aues.entites.StatutPay;
import com.aues.repositories.FactureRepository;
import com.aues.repositories.PayementRepository;
import com.aues.services.Interface.PayementInterf;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Paiements (conception fusion) :
 * - les règles s'appliquent quel que soit le mode de paiement (bug
 *   « physique seulement » de la branche précédente corrigé) ;
 * - montant nul / facture payée / paiement excédentaire refusés ;
 * - annulation : trace conservée (StatutPay.Annule) et montantPaye recalculé.
 */
@Service
public class PayementService implements PayementInterf {

    private final PayementRepository payementRepository;
    private final FactureRepository factureRepository;

    public PayementService(PayementRepository payementRepository,
                           FactureRepository factureRepository) {
        this.payementRepository = payementRepository;
        this.factureRepository = factureRepository;
    }

    @Override
    @Transactional
    public PayementDto save(PayementDto payementDto) {
        List<String> errors = PayementVerify.verify(payementDto);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Erreur de validation : " + String.join(", ", errors));
        }

        Facture facture = factureRepository.findById(payementDto.getIdFact())
                .orElseThrow(() -> new RuntimeException("Facture non trouvée pour l'ID spécifié."));

        BigDecimal montant = payementDto.getMontant();
        if (facture.getStatut() == com.aues.entites.StatutFact.Payee) {
            throw new RuntimeException("La facture " + facture.getNumero() + " est déjà entièrement payée.");
        }
        if (montant.compareTo(facture.montantRestant()) > 0) {
            throw new RuntimeException("Le montant dépasse le reste à payer ("
                    + facture.montantRestant() + " FCFA) pour la facture " + facture.getNumero() + ".");
        }

        Payement payement = new Payement();
        payement.setFacture(facture);
        payement.setMontant(montant);
        payement.setMode(ModePayement.valueOf(payementDto.getMode()));
        payement.setStatutPay(StatutPay.Effectuer);
        if (payementDto.getPayeur() != null && !payementDto.getPayeur().isBlank()) {
            payement.setPayeur(payementDto.getPayeur());
        }
        payement = payementRepository.save(payement);

        // Le cumul augmente et le statut est recalculé — quel que soit le mode.
        facture.setMontantPaye(facture.getMontantPaye().add(montant));
        facture.recalculerStatut();
        factureRepository.save(facture);

        return PayementDto.Collects(payement);
    }

    @Override
    public PayementDto findByid(Integer id) {
        if (id == null) {
            throw new RuntimeException("Paiement non trouvé pour l'ID spécifié.");
        }
        Payement payement = payementRepository.findById(id).orElse(null);
        if (payement == null) {
            throw new RuntimeException("Ce paiement n'est pas dans la base.");
        }
        return PayementDto.Collects(payement);
    }

    /** Annule un versement : la trace reste (statut Annule), le reste est recalculé. */
    @Override
    @Transactional
    public PayementDto AnnulerByid(Integer id) {
        if (id == null) {
            throw new RuntimeException("Paiement non trouvé pour l'ID spécifié.");
        }
        Payement payement = payementRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Ce paiement n'est pas dans la base : " + id));
        if (payement.getStatutPay() == StatutPay.Annule) {
            throw new RuntimeException("Ce paiement est déjà annulé.");
        }

        Facture facture = payement.getFacture();
        if (payement.getStatutPay() == StatutPay.Effectuer) {
            // Le versement avait été compté : on le retire du cumul payé.
           BigDecimal resteApres = facture.getMontantPaye().subtract(payement.getMontant());
            facture.setMontantPaye(resteApres.max(BigDecimal.ZERO));
            facture.recalculerStatut();
            factureRepository.save(facture);
        }

        payement.setStatutPay(StatutPay.Annule);
        return PayementDto.Collects(payementRepository.save(payement));
    }

    @Override
    public List<PayementDto> findAll() {
        return ((List<Payement>) payementRepository.findAll()).stream()
                .map(PayementDto::Collects)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id != null) {
            payementRepository.deleteById(id);
        }
    }

    // ------------------------------------------------------------------
    // Accès bruts pour les vues web
    // ------------------------------------------------------------------

    @Override
    public List<Payement> listerEntites() {
        return (List<Payement>) payementRepository.findAll();
    }

    @Override
    public List<Payement> listerEntitesParFacture(Integer factureId) {
        return payementRepository.findByFactureId(factureId);
    }
}
