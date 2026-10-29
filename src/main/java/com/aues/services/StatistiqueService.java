package com.aues.services;

import com.aues.entites.Compteur;
import com.aues.entites.Facture;
import com.aues.entites.Payement;
import com.aues.entites.Releve;
import com.aues.repositories.CompteurRepository;
import com.aues.repositories.FactureRepository;
import com.aues.repositories.PayementRepository;
import com.aues.repositories.ReleveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Statistiques pour le tableau de bord : recettes, impayés,
 * consommation, recouvrement.
 */
@Service
public class StatistiqueService {

    private static final SimpleDateFormat FORMAT_MOIS = new SimpleDateFormat("yyyy-MM");

    @Autowired
    private FactureRepository factureRepository;

    @Autowired
    private PayementRepository payementRepository;

    @Autowired
    private ReleveRepository releveRepository;

    @Autowired
    private CompteurRepository compteurRepository;

    /** Bloc complet consommé par le tableau de bord (un seul aller-retour). */
    public Map<String, Object> statistiques() {
        List<Facture> factures = (List<Facture>) factureRepository.findAll();
        List<Payement> payements = (List<Payement>) payementRepository.findAll();
        List<Releve> releves = (List<Releve>) releveRepository.findAll();

        double totalFacture = factures.stream().mapToDouble(Facture::getMontant).sum();
        double totalPaye = factures.stream().mapToDouble(Facture::getMontant_paye).sum();
        long nbImpayees = factures.stream().filter(f -> f.getStatut() == com.aues.StatutFacture.Impayee).count();
        double uniteConsommees = releves.stream().mapToDouble(Releve::getUnite_consomme).sum();

        Map<String, Object> resultats = new LinkedHashMap<>();
        resultats.put("nbCompteurs", compteurRepository.count());
        resultats.put("nbFactures", factures.size());
        resultats.put("nbFacturesImpayees", nbImpayees);
        resultats.put("recettesTotales", Math.round(totalPaye * 100.0) / 100.0);
        resultats.put("montantFacture", Math.round(totalFacture * 100.0) / 100.0);
        resultats.put("montantImpaye", Math.round((totalFacture - totalPaye) * 100.0) / 100.0);
        resultats.put("tauxRecouvrement",
                totalFacture > 0 ? Math.round(totalPaye / totalFacture * 1000.0) / 10.0 : 0.0);
        resultats.put("uniteConsommees", Math.round(uniteConsommees * 100.0) / 100.0);
        resultats.put("recettesParMois", recettesParMois(payements));
        resultats.put("consommationParMois", consommationParMois(releves));
        return resultats;
    }

    /** Recettes agrégées par mois (2026-10, 2026-11…). */
    public Map<String, Double> recettesParMois(List<Payement> payements) {
        return payements.stream()
                .collect(Collectors.groupingBy(
                        p -> FORMAT_MOIS.format(p.getDate()),
                        LinkedHashMap::new,
                        Collectors.summingDouble(Payement::getMontant)));
    }

    /** Consommation agrégée par mois (m³ facturés relevés sur les compteurs). */
    public Map<String, Double> consommationParMois(List<Releve> releves) {
        return releves.stream()
                .collect(Collectors.groupingBy(
                        r -> FORMAT_MOIS.format(r.getDate()),
                        LinkedHashMap::new,
                        Collectors.summingDouble(Releve::getUnite_consomme)));
    }

    /** Compteurs les plus gros consommateurs (à la dernière facture). */
    public Map<String, Double> consommationParCompteur() {
        List<Releve> releves = (List<Releve>) releveRepository.findAll();
        return releves.stream()
                .collect(Collectors.groupingBy(
                        Releve::getNumero_compteur,
                        Collectors.summingDouble(Releve::getUnite_consomme)));
    }
}
