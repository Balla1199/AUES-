package com.aues.services;

import com.aues.DTO.FactureDto;
import com.aues.DTO.ReleveDto;
import com.aues.TypeStatut;
import com.aues.entites.Compteur;
import com.aues.entites.Releve;
import com.aues.entites.StatutFact;
import com.aues.repositories.CompteurRepository;
import com.aues.repositories.ReleveRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ReleveService {

    private ReleveRepository releveRepository;
    private CompteurRepository compteurRepository;

    @Autowired
    private  CompteurService compteurService;
    @Autowired
    private FactureService factureService;

    public ReleveService(ReleveRepository releveRepository) {
        this.releveRepository = releveRepository;
    }

    @Transactional
    public ReleveDto ajouterReleve(ReleveDto dto) {
        if (dto != null ) {
            Compteur compteur = compteurService.ChercherCode(dto.getNumero_compteur());
            if (compteur.getStatut()==TypeStatut.Inactive){
                throw new RuntimeException("le statut du compteur est : " + compteur.getStatut());
            }
            if (compteur == null) {
                throw new RuntimeException("Compteur non trouvé avec le numéro : " + dto.getNumero_compteur());
            }
            if (compteur.getCpteurChiffre() >= dto.getValLorsRelev()) {
                throw new RuntimeException("Enregistrement impossible : valeur inférieure au compteur actuel");
            }
            Releve releve = ReleveDto.Register(dto);
            releve.setCompteur(compteur);
            releve.setUnite_consomme((dto.getValLorsRelev() - compteur.getCpteurChiffre()));
            releve = releveRepository.save(releve);
            FactureDto factureDto = new FactureDto();
            factureDto.setStatut(StatutFact.nopayer);
            factureDto.setMontantTotal(Math.round(releve.getUnite_consomme() * releve.getTarif()));
            factureDto.setReleve(releve);
            factureService.save(factureDto);

            compteur.setCpteurChiffre(dto.getValLorsRelev());
            compteur.getReleve().add(releve);
            compteurService.ajouter(compteur);

            return ReleveDto.Collects(releve);
        }
        throw new RuntimeException("La valeur de relevé est null");
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
            releve1.setDate(Instant.now());

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
