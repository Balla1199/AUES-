package com.aues.controllers;

import com.aues.ReleveRequest;
import com.aues.entites.Releve;
import com.aues.services.ReleveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/releves")
public class ReleveController {
    @Autowired
    private ReleveService releveService;

    // Endpoint pour ajouter un relevé
    @PostMapping
    public ResponseEntity<String> ajouterReleve(@RequestBody ReleveRequest releveRequest) {
        try {
            // Appeler le service pour ajouter le relevé
            releveService.ajouterReleve(releveRequest.getNumero(), releveRequest.getUnite_consomme());
            return ResponseEntity.ok("Relevé ajouté avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Releve>> afficheReleve(){
        List<Releve> releves= this.releveService.afficherReleve();
        return ResponseEntity.ok(releves);
    }
    @GetMapping(path = "{id}")
    public ResponseEntity<?> chercher(@PathVariable int id){
        Releve relevebd = this.releveService.chercher(id);
        if (relevebd!=null){
            return ResponseEntity.ok(relevebd);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun relever trouvé avec l'ID " + id + ".");
        }
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<String> modiffier(@PathVariable int id,@RequestBody ReleveRequest releveRequest){
        Releve releve1 = this.releveService.modiffier(id,releveRequest.getUnite_consomme());
        if(releve1!=null){
            return ResponseEntity.ok("Le relever avec l'ID " + id + " a été modifié avec succès.");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun relever trouvé avec l'ID " + id + " pour modification.");
        }
    }
    @DeleteMapping(path = "{id}")
    public ResponseEntity<String> supprimer(@PathVariable int id){
        Releve releve= this.releveService.supprimer(id);
        if (releve!=null){
            return ResponseEntity.ok("Le relever avec l'ID " + id + " a été supprimé avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun relever trouvé avec l'ID " + id + " pour suppression.");
        }
    }
}
