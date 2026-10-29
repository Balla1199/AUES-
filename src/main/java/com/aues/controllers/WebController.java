package com.aues.controllers;

import com.aues.ModePayement;
import com.aues.entites.Facture;
import com.aues.entites.Payement;
import com.aues.services.FactureService;
import com.aues.services.PayementService;
import com.aues.services.StatistiqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Vues web (Thymeleaf) pour les agents de l'AUES : tableau de bord,
 * factures et paiements. Les URLs sont préfixées par /web pour ne pas
 * empiéter sur l'API REST (protégée par JWT).
 */
@Controller
public class WebController {

    @Autowired
    private FactureService factureService;

    @Autowired
    private PayementService payementService;

    @Autowired
    private StatistiqueService statistiqueService;

    /** Raccourci : "/" -> tableau de bord. */
    @GetMapping("/")
    public String racine() {
        return "redirect:/web/tableau-de-bord";
    }

    @GetMapping("/web/tableau-de-bord")
    public String tableauDeBord(Model model) {
        Map<String, Object> stats = statistiqueService.statistiques();
        model.addAttribute("stats", stats);
        return "tableau-de-bord";
    }

    @GetMapping("/web/factures")
    public String factures(Model model) {
        model.addAttribute("factures", factureService.afficherFactures());
        return "factures";
    }

    @GetMapping("/web/factures/{id}")
    public String facture(@PathVariable int id, Model model) {
        Facture facture = factureService.chercher(id);
        if (facture == null) {
            return "redirect:/web/factures";
        }
        model.addAttribute("facture", facture);
        model.addAttribute("payements", payementService.afficherParFacture(id));
        model.addAttribute("modes", ModePayement.values());
        return "facture";
    }

    /** Post du formulaire de paiement d'une facture depuis la vue web. */
    @PostMapping("/web/factures/{id}/payer")
    public String payer(@PathVariable int id,
                        @RequestParam Double montant,
                        @RequestParam("mode") String modeTexte,
                        @RequestParam(required = false) String payeur,
                        Model model) {
        try {
            ModePayement mode = ModePayement.valueOf(modeTexte);
            payementService.enregistrer(id, montant, mode, payeur);
            return "redirect:/web/factures/" + id;
        } catch (RuntimeException e) {
            model.addAttribute("erreur", e.getMessage());
            return "facture";
        }
    }

    @GetMapping("/web/payements")
    public String payements(Model model) {
        model.addAttribute("payements", payementService.afficherPayements());
        return "payements";
    }
}
