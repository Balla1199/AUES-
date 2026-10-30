package com.aues.services;

import com.aues.DTO.FactureDto;
import com.aues.Verify.Factureverify;
import com.aues.entites.Facture;
import com.aues.entites.Payement;
import com.aues.entites.Releve;
import com.aues.entites.StatutFact;
import com.aues.repositories.FactureRepository;
import com.aues.services.Interface.FactureInterf;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Cycle de vie des factures (conception fusion) :
 * - génération automatique depuis un relevé (montant = unités × tarif) ;
 * - montant d'origine immuable, montantPaye cumulatif ;
 * - consultation par DTO + accès brut pour les vues web.
 */
@Service
public class FactureService implements FactureInterf {

    private final FactureRepository factureRepository;
    private final com.aues.repositories.ReleveRepository releveRepository;
    private final com.aues.repositories.PayementRepository payementRepository;

    public FactureService(FactureRepository factureRepository,
                          com.aues.repositories.ReleveRepository releveRepository,
                          com.aues.repositories.PayementRepository payementRepository) {
        this.factureRepository = factureRepository;
        this.releveRepository = releveRepository;
        this.payementRepository = payementRepository;
    }

    @Override
    public FactureDto save(FactureDto factureDto) {
        List<String> errors = Factureverify.verify(factureDto);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Erreur de validation : " + String.join(", ", errors));
        }
        Facture factureSavee = factureRepository.save(FactureDto.Register(factureDto));
        return FactureDto.Collects(factureSavee);
    }

    /** Génère une facture depuis un relevé : une seule facture par relevé. */
    @Override
    public FactureDto generer(Integer releveId) {
        if (releveId == null) {
            throw new IllegalArgumentException("L'identifiant du relevé est requis.");
        }
        Releve releve = releveRepository.findById(releveId)
                .orElseThrow(() -> new RuntimeException("Aucun relevé trouvé avec l'ID " + releveId + "."));
        if (factureRepository.existsByReleveId(releveId)) {
            throw new RuntimeException("Une facture existe déjà pour le relevé " + releveId + ".");
        }
        if (releve.getUnite_consomme() == null || releve.getTarif() == null) {
            throw new RuntimeException("Le relevé " + releveId + " n'est pas exploitable (unités ou tarif manquant).");
        }

        Facture facture = new Facture();
        facture.setNumero("FACT-" + releve.getCompteur().getCode() + "-" + releve.getId());
        facture.setMontant(BigDecimal.valueOf(releve.getUnite_consomme())
                .multiply(BigDecimal.valueOf(releve.getTarif())));
        facture.setMontantPaye(BigDecimal.ZERO);
        facture.setStatut(StatutFact.Impayee);
        facture.setReleve(releve);
        facture.appliquerEcheanceParDefaut();

        return FactureDto.Collects(factureRepository.save(facture));
    }

    @Override
    public FactureDto findByid(Integer id) {
        if (id == null) {
            throw new RuntimeException("Identifiant de facture requis.");
        }
        Facture facture = factureRepository.findById(id).orElse(null);
        if (facture == null) {
            throw new RuntimeException("Cette facture n'est pas dans la base.");
        }
        return FactureDto.Collects(facture);
    }

    @Override
    public List<FactureDto> findAll() {
        return ((List<Facture>) factureRepository.findAll()).stream()
                .map(FactureDto::Collects)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id != null) {
            factureRepository.deleteById(id);
        }
    }

    // ------------------------------------------------------------------
    // Accès bruts pour les vues web
    // ------------------------------------------------------------------

    @Override
    public List<Facture> listerEntites() {
        return (List<Facture>) factureRepository.findAll();
    }

    @Override
    public Facture chercherEntite(Integer id) {
        return id == null ? null : factureRepository.findById(id).orElse(null);
    }

    @Override
    public List<Payement> listerPayementsParFacture(Integer factureId) {
        return payementRepository.findByFactureId(factureId);
    }
}
