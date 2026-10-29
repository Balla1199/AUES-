package com.aues.controllers;

import com.aues.StatutFacture;
import com.aues.entites.Facture;
import com.aues.services.FactureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/factures")
public class FactureController {

    @Autowired
    private FactureService factureService;

    // Générer une facture depuis un relevé (une seule fois par relevé)
    @PostMapping("/generer/{releveId}")
    public ResponseEntity<?> genererFacture(@PathVariable int releveId) {
        try {
            Facture facture = factureService.generer(releveId);
            return ResponseEntity.status(HttpStatus.CREATED).body(facture);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Facture>> afficheFactures() {
        return ResponseEntity.ok(factureService.afficherFactures());
    }

    // Factures payées ou impayées : /factures/statut/Payee ou /factures/statut/Impayee
    @GetMapping("/statut/{statut}")
    public ResponseEntity<?> afficherParStatut(@PathVariable String statut) {
        try {
            StatutFacture statutFacture = StatutFacture.valueOf(statut);
            return ResponseEntity.ok(factureService.afficherParStatut(statutFacture));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(
                    "Statut invalide : " + statut + ". Valeurs acceptées : Payee, Impayee.");
        }
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<?> chercher(@PathVariable int id) {
        Facture facture = factureService.chercher(id);
        if (facture != null) {
            return ResponseEntity.ok(facture);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Aucune facture trouvée avec l'ID " + id + ".");
    }

    @GetMapping("/numero/{numero}")
    public ResponseEntity<?> chercherParNumero(@PathVariable String numero) {
        Facture facture = factureService.chercherParNumero(numero);
        if (facture != null) {
            return ResponseEntity.ok(facture);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Aucune facture trouvée avec le numéro " + numero + ".");
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<String> supprimer(@PathVariable int id) {
        Facture facture = factureService.supprimer(id);
        if (facture != null) {
            return ResponseEntity.ok("La facture avec l'ID " + id + " a été supprimée avec succès.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Aucune facture trouvée avec l'ID " + id + " pour suppression.");
    }
}
