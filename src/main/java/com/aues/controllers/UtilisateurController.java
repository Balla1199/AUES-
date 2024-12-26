package com.aues.controllers;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    // Créer un nouveau client (accessible uniquement par l'administrateur)
    @PostMapping("/clients")
    public ResponseEntity<Utilisateur> creerClient(@RequestBody Utilisateur utilisateur, @RequestHeader("Role") Role role) {
        if (role != Role.ADMINISTRATEUR) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Seul l'administrateur peut créer un client
        }

        utilisateur.setRole(Role.CLIENT); // Forcer le rôle CLIENT
        Utilisateur nouveauClient = utilisateurService.ajouterUtilisateur(utilisateur);
        return new ResponseEntity<>(nouveauClient, HttpStatus.CREATED);
    }

    // Modifier un client (accessible uniquement par l'administrateur)
    @PutMapping("/clients/{id}")
    public ResponseEntity<Utilisateur> modifierClient(@PathVariable Integer id, @RequestBody Utilisateur utilisateurDetails, @RequestHeader("Role") Role role) {
        if (role != Role.ADMINISTRATEUR) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Seul l'administrateur peut modifier un client
        }

        Utilisateur utilisateurModifie = utilisateurService.modifierUtilisateur(id, utilisateurDetails);
        return new ResponseEntity<>(utilisateurModifie, HttpStatus.OK);
    }

    // Supprimer un client (accessible uniquement par l'administrateur)
    @DeleteMapping("/clients/{id}")
    public ResponseEntity<Void> supprimerClient(@PathVariable Integer id, @RequestHeader("Role") Role role) {
        if (role != Role.ADMINISTRATEUR) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Seul l'administrateur peut supprimer un client
        }

        utilisateurService.supprimerUtilisateur(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Lister tous les clients (accessible uniquement par l'administrateur)
    @GetMapping("/clients")
    public ResponseEntity<List<Utilisateur>> listerClients(@RequestHeader("Role") Role role) {
        if (role != Role.ADMINISTRATEUR) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Seul l'administrateur peut voir les clients
        }

        List<Utilisateur> clients = utilisateurService.listerUtilisateursParRole(Role.CLIENT);
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }
}
