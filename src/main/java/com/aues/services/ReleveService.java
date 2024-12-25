package com.aues.services;

import com.aues.entites.Compteur;
import com.aues.entites.Releve;
import com.aues.repositories.CompteurRepository;
import com.aues.repositories.ReleveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReleveService {
    @Autowired
    private ReleveRepository releveRepository;


    @Autowired
    private CompteurRepository compteurRepository;

    public ReleveService() {
    }

    public ReleveService(ReleveRepository releveRepository, CompteurRepository compteurRepository) {
        this.releveRepository = releveRepository;
        this.compteurRepository = compteurRepository;
    }

    public void ajouterReleve(String numero_compteur, Double uniteConsomme) {
        Compteur compteur = compteurRepository.findByNumero(numero_compteur);
        if (compteur != null) {
            Releve releve = new Releve();
            releve.setDate(new Date());  // Date actuelle
            releve.setUnite_consomme(uniteConsomme);  // Unité consommée
            releve.setTarif(250.5);  // Tarif fixe
            releve.setNumero_compteur(compteur.getNumero());
            releve.setCompteur(compteur);  // Associer le compteur

            releveRepository.save(releve);  // Sauvegarder le relevé
        } else {
            throw new RuntimeException("Compteur non trouvé avec le numéro : " + numero_compteur);
        }
    }

    public List<Releve> afficherReleve(){
        return (List<Releve>) this.releveRepository.findAll();
    }

    public Releve chercher(int id){
        Optional<Releve> relevebd = this.releveRepository.findById(id);
        return relevebd.orElse(null);
    }

    public Releve modiffier(int id, Double uniteConsomme) {
        Releve releve1 = this.chercher(id);
        if (releve1 != null) {
            releve1.setUnite_consomme(uniteConsomme);
            releve1.setDate(new Date());

            this.releveRepository.save(releve1);
        }
        return releve1;
    }



    public Releve supprimer(int id){
        Releve releve1=this.chercher(id);
        if (releve1!=null){
            this.releveRepository.delete(releve1);
        }
        return releve1;
    }
}
