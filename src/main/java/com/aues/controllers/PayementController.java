package com.aues.controllers;

import com.aues.ModePayement;
import com.aues.PayementRequest;
import com.aues.entites.Payement;
import com.aues.services.PayementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payements")
public class PayementController {

    @Autowired
    private PayementService payementService;

    // Enregistrer un versement sur une facture (partiel ou total)
    @PostMapping
    public ResponseEntity<?> enregistrer(@RequestBody PayementRequest requete) {
        try {
            ModePayement mode = null;
            if (requete.getMode() != null) {
                try {
                    mode = ModePayement.valueOf(requete.getMode());
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.status(400).body(
                            "Mode invalide : " + requete.getMode()
                                    + ". Valeurs acceptées : Especes, MobileMoney, Virement, Cheque.");
                }
            }
            Payement payement = payementService.enregistrer(
                    requete.getFactureId(), requete.getMontant(), mode, requete.getPayeur());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Paiement enregistré avec succès (facture " + requete.getFactureId() + ").");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Payement>> affichePayements() {
        return ResponseEntity.ok(payementService.afficherPayements());
    }

    @GetMapping(path = "/facture/{factureId}")
    public ResponseEntity<List<Payement>> afficherParFacture(@PathVariable int factureId) {
        return ResponseEntity.ok(payementService.afficherParFacture(factureId));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<?> chercher(@PathVariable int id) {
        Payement payement = payementService.chercher(id);
        if (payement != null) {
            return ResponseEntity.ok(payement);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Aucun paiement trouvé avec l'ID " + id + ".");
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<String> supprimer(@PathVariable int id) {
        Payement payement = payementService.supprimer(id);
        if (payement != null) {
            return ResponseEntity.ok("Le paiement avec l'ID " + id + " a été supprimé avec succès.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Aucun paiement trouvé avec l'ID " + id + " pour suppression.");
    }
}
