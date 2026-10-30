package com.aues.services;

import com.aues.entites.Facture;
import com.aues.entites.Payement;
import com.aues.entites.Releve;
import com.aues.entites.StatutFact;
import com.aues.entites.StatutPay;
import com.aues.repositories.FactureRepository;
import com.aues.repositories.PayementRepository;
import com.aues.repositories.ReleveRepository;
import com.aues.repositories.CompteurRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Statistiques du tableau de bord : recettes, impayés, consommation,
 * taux de recouvrement. Calculs en mémoire (volumes d'une AUES maîtrisés).
 */
@Service
public class StatistiqueService {

    private static final DateTimeFormatter FORMAT_MOIS_INSTANT =
            DateTimeFormatter.ofPattern("yyyy-MM").withZone(ZoneId.systemDefault());

    private final FactureRepository factureRepository;
    private final PayementRepository payementRepository;
    private final ReleveRepository releveRepository;
    private final CompteurRepository compteurRepository;

    public StatistiqueService(FactureRepository factureRepository,
                              PayementRepository payementRepository,
                              ReleveRepository releveRepository,
                              CompteurRepository compteurRepository) {
        this.factureRepository = factureRepository;
        this.payementRepository = payementRepository;
        this.releveRepository = releveRepository;
        this.compteurRepository = compteurRepository;
    }

    /** Bloc complet consommé par le tableau de bord (un seul aller-retour). */
    public Map<String, Object> statistiques() {
        List<Facture> factures = (List<Facture>) factureRepository.findAll();
        List<Payement> payementsValides = ((List<Payement>) payementRepository.findAll()).stream()
                .filter(p -> p.getStatutPay() == StatutPay.Effectuer)
                .toList();
        List<Releve> releves = (List<Releve>) releveRepository.findAll();

        BigDecimal totalFacture = factures.stream()
                .map(Facture::getMontant).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPaye = factures.stream()
                .map(Facture::getMontantPaye).reduce(BigDecimal.ZERO, BigDecimal::add);
        long nbImpayees = factures.stream()
                .filter(f -> f.getStatut() == StatutFact.Impayee).count();
        double unitesConsommees = releves.stream()
                .filter(r -> r.getUnite_consomme() != null)
                .mapToDouble(Releve::getUnite_consomme).sum();

        Map<String, Object> resultats = new LinkedHashMap<>();
        resultats.put("nbCompteurs", compteurRepository.count());
        resultats.put("nbFactures", factures.size());
        resultats.put("nbFacturesImpayees", nbImpayees);
        resultats.put("recettesTotales", totalPaye);
        resultats.put("montantFacture", totalFacture);
        resultats.put("montantImpaye", totalFacture.subtract(totalPaye).max(BigDecimal.ZERO));
        resultats.put("tauxRecouvrement", taux(totalPaye, totalFacture));
        resultats.put("uniteConsommees", Math.round(unitesConsommees * 100.0) / 100.0);
        resultats.put("recettesParMois", recettesParMois(payementsValides));
        resultats.put("consommationParMois", consommationParMois(releves));
        return resultats;
    }

    private double taux(BigDecimal paye, BigDecimal facture) {
        if (facture.compareTo(BigDecimal.ZERO) <= 0) {
            return 0.0;
        }
        return Math.round(paye.multiply(BigDecimal.valueOf(100))
                .divide(facture, 2, java.math.RoundingMode.HALF_UP).doubleValue() * 10.0) / 10.0;
    }

    /** Recettes agrégées par mois (2026-10, 2026-11…). */
    public Map<String, Double> recettesParMois(List<Payement> payements) {
        Map<String, Double> aggregats = new LinkedHashMap<>();
        for (Payement p : payements) {
            String mois = FORMAT_MOIS_INSTANT.format(p.getCreateDate());
            aggregats.merge(mois, p.getMontant().doubleValue(), Double::sum);
        }
        return aggregats;
    }

    /** Consommation agrégée par mois (releves de compteurs). */
    public Map<String, Double> consommationParMois(List<Releve> releves) {
        Map<String, Double> aggregats = new LinkedHashMap<>();
        for (Releve r : releves) {
            String mois = FORMAT_MOIS_INSTANT.format(r.getDate());
            aggregats.merge(mois, r.getUnite_consomme(), Double::sum);
        }
        return aggregats;
    }
}
