package com.aues.services;

import com.aues.entites.Compteur;
import com.aues.entites.Releve;
import com.aues.repositories.CompteurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompteurService {
    @Autowired
    public CompteurRepository compteurRepository;

    public CompteurService() {
    }

    public CompteurService(CompteurRepository compteurRepository) {
        this.compteurRepository = compteurRepository;
    }

    public void ajouter(Compteur compteur){

        // compteur.setReleve(compteur.getReleve());
        this.compteurRepository.save(compteur);
    }

    public List<Compteur> afficher(){
        return (List<Compteur>) this.compteurRepository.findAll();
    }

    public Compteur chercher(int id){
        Optional<Compteur> optional= this.compteurRepository.findById(id);
        return optional.orElse(null);
    }

    public Compteur modiffier(int id, Compteur compteur){
        Compteur compteurbd= this.chercher(id);
        if(compteurbd!=null){
            compteurbd.setNumero(compteur.getNumero());
            compteurbd.setStatut(compteur.getStatut());
            this.compteurRepository.save(compteurbd);
        }
        return compteurbd;
    }
    public Compteur supprimer(int id ){
        Compteur compteurbd =this.chercher(id);
        if (compteurbd!=null){
            this.compteurRepository.delete(compteurbd);
        }
        return compteurbd;
    }
}
