package com.aues.controllers;

import com.aues.entites.Compteur;
import com.aues.services.CompteurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/compteur")
public class CompteurController {
    @Autowired
    public CompteurService compteurService;

    public CompteurController() {
    }

    public CompteurController(CompteurService compteurService) {
        this.compteurService = compteurService;
    }
    @PostMapping
    public ResponseEntity<String> ajouter(@RequestBody Compteur compteur){

        this.compteurService.ajouter(compteur);
        return ResponseEntity.status(HttpStatus.CREATED).body("Le compteur a été ajouté avec succès.");
    }
    @GetMapping
    public ResponseEntity<List<Compteur>> afficher() {
        List<Compteur> compteurs = this.compteurService.afficher();
        return ResponseEntity.ok(compteurs);
    }
    @GetMapping(path = "{id}")
    public ResponseEntity<?> chercher(@PathVariable int id) {
        Compteur compteur = this.compteurService.chercher(id);
        if (compteur != null) {
            return ResponseEntity.ok(compteur);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun compteur trouvé avec l'ID " + id + ".");
        }
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<String> modifier(@PathVariable int id, @RequestBody Compteur compteur) {
        Compteur compteurModifie = this.compteurService.modiffier(id, compteur);
        if (compteurModifie != null) {
            return ResponseEntity.ok("Le compteur avec l'ID " + id + " a été modifié avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun compteur trouvé avec l'ID " + id + " pour modification.");
        }
    }
    @DeleteMapping(path = "{id}")
    public ResponseEntity<String> supprimer(@PathVariable int id){
        Compteur compteur= this.compteurService.supprimer(id);
        if(compteur!=null){
            return ResponseEntity.ok("Le compteur avec l'ID " + id + " a été supprimé avec succès.");
        }else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun compteur trouvé avec l'ID " + id + " pour suppression.");
    }
}
