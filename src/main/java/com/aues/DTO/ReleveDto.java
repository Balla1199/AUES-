package com.aues.DTO;

import com.aues.entites.Compteur;
import com.aues.entites.Facture;
import com.aues.entites.Releve;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ReleveDto {


    private Integer id;
    private String date;
    private  Double unite_consomme;
    private Double valLorsRelev;
    private Double tarif = 250.5;
    private String numero_compteur;
    @JsonIgnore
    private Facture facture;

    public  ReleveDto(){}

    public static ReleveDto Collects(Releve relev){
        LocalDateTime localDateTime;
        localDateTime = LocalDateTime.ofInstant(relev.getDate(), ZoneId.systemDefault()).withNano(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatedDate = localDateTime.format(formatter);
        if (relev!=null){
            ReleveDto releveDto = new ReleveDto();
            releveDto.setId(relev.getId());
            releveDto.setDate(formatedDate);
            releveDto.setUnite_consomme(relev.getUnite_consomme());
            releveDto.setValLorsRelev(relev.getValLorsRelev());
            releveDto.setNumero_compteur(releveDto.getNumero_compteur());
            releveDto.setFacture(relev.getFacture());
            return releveDto;
        } return null;
    }



    public static Releve Register(ReleveDto dto){

        Releve releve = new Releve();
        if (dto!=null){
            releve.setId(dto.getId());
            releve.setValLorsRelev(dto.getValLorsRelev());
            releve.setUnite_consomme(dto.getUnite_consomme());
            releve.setTarif(dto.getTarif());
            releve.setFacture(dto.getFacture());
            return releve;
        } return null;
    }






    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getUnite_consomme() {
        return unite_consomme;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getTarif() {
        return tarif;
    }

    public void setTarif(Double tarif) {
        this.tarif = tarif;
    }

    public void setUnite_consomme(Double unite_consomme) {
        this.unite_consomme = unite_consomme;
    }

    public Double getValLorsRelev() {
        return valLorsRelev;
    }

    public void setValLorsRelev(Double valLorsRelev) {
        this.valLorsRelev = valLorsRelev;
    }

    public String getNumero_compteur() {
        return numero_compteur;
    }

    public void setNumero_compteur(String numero_compteur) {
        this.numero_compteur = numero_compteur;
    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }




}
