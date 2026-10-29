package com.aues.services;

import com.aues.StatutFacture;
import com.aues.entites.Facture;
import com.aues.entites.Releve;
import com.aues.repositories.FactureRepository;
import com.aues.repositories.ReleveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Cycle de vie des factures : génération automatique depuis un relevé,
 * consultation, consultation par statut, suppression.
 */
@Service
public class FactureService {

    @Autowired
    private FactureRepository factureRepository;

    @Autowired
    private ReleveRepository releveRepository;

    public FactureService() {
    }

    /**
     * Génère la facture d'un relevé : montant = unite_consomme × tarif,
     * échéance à émission + 30 jours. Rejetée si le relevé est déjà facturé.
     */
    public Facture generer(int releveId) {
        Optional<Releve> releveBd = releveRepository.findById(releveId);
        if (releveBd.isEmpty()) {
            throw new RuntimeException("Aucun relevé trouvé avec l'ID " + releveId + ".");
        }
        if (factureRepository.existsByReleveId(releveId)) {
            throw new RuntimeException("Une facture existe déjà pour le relevé " + releveId + ".");
        }
        Releve releve = releveBd.get();

        Facture facture = new Facture();
        facture.setNumero("FACT-" + releve.getNumero_compteur() + "-" + releve.getId());
        facture.setDate_emission(new Date());
        Calendar echeance = Calendar.getInstance();
        echeance.add(Calendar.DAY_OF_MONTH, 30);
        facture.setDate_echeance(echeance.getTime());
        facture.setMontant(releve.getUnite_consomme() * releve.getTarif());
        facture.setMontant_paye(0.0);
        facture.setStatut(StatutFacture.Impayee);
        facture.setNumero_compteur(releve.getNumero_compteur());
        facture.setReleve(releve);

        return factureRepository.save(facture);
    }

    public List<Facture> afficherFactures() {
        return (List<Facture>) factureRepository.findAll();
    }

    public List<Facture> afficherParStatut(StatutFacture statut) {
        return factureRepository.findByStatut(statut);
    }

    public Facture chercher(int id) {
        return factureRepository.findById(id).orElse(null);
    }

    public Facture chercherParNumero(String numero) {
        return factureRepository.findByNumero(numero).orElse(null);
    }

    public Facture supprimer(int id) {
        Facture facture = chercher(id);
        if (facture != null) {
            factureRepository.delete(facture);
        }
        return facture;
    }
}
