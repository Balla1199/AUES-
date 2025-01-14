package com.aues.DTO;

import com.aues.entites.Facture;
import com.aues.entites.Releve;
import com.aues.entites.StatutFact;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


public class FactureDto {

    private Integer id;
    private String dateTrs;
    private StatutFact statut;
    private Long montantTotal;
    @JsonIgnore
    private Releve releve;

    public  FactureDto(){}

    public static FactureDto Collects(Facture fact){
        LocalDateTime localDateTime;
        localDateTime = LocalDateTime.ofInstant(fact.getCreateDate(), ZoneId.systemDefault()).withNano(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatedDate = localDateTime.format(formatter);
        if (fact!=null){
            FactureDto factureDto = new FactureDto();
            factureDto.setId(fact.getId());
            factureDto.setDateTrs(formatedDate);
            factureDto.setMontantTotal(fact.getMontantTotal());
            factureDto.setStatut(fact.getStatut());

            return factureDto;
        } return null;
    }

    public static Facture Register(FactureDto dto){

        Facture facture = new Facture();
        if (dto!=null){
            facture.setId(dto.getId());
            facture.setStatut(dto.getStatut());
            facture.setMontantTotal(dto.getMontantTotal());
            facture.setReleve(dto.getReleve());
            return facture;
        } return null;
    }


    public Integer getId() {
        return id;
    }

    public String getDateTrs() {
        return dateTrs;
    }

    public StatutFact getStatut() {
        return statut;
    }

    public Long getMontantTotal() {
        return montantTotal;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public void setDateTrs(String dateTrs) {
        this.dateTrs = dateTrs;
    }

    public void setStatut(StatutFact statut) {
        this.statut = statut;
    }

    public void setMontantTotal(Long montantTotal) {
        this.montantTotal = montantTotal;
    }


    public Releve getReleve() {
        return releve;
    }

    public void setReleve(Releve releve) {
        this.releve = releve;
    }



}
