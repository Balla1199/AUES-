package com.aues.DTO;

import com.aues.entites.Facture;
import com.aues.entites.StatutFact;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;


public class FactureDto {

    private Integer id;
    private String numero;
    private String dateTrs;
    private String dateEcheance;
    private StatutFact statut;
    private BigDecimal montant;        // montant d'origine (immuable)
    private BigDecimal montantPaye;    // cumul des versements
    private String numeroCompteur;
    @JsonIgnore
    private Integer releveId;

    public FactureDto() {
    }

    public static FactureDto Collects(Facture facture) {
        if (facture == null) {
            return null;
        }
        DateTimeFormatter formatteur = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        FactureDto dto = new FactureDto();
        dto.setId(facture.getId());
        dto.setNumero(facture.getNumero());
        dto.setStatut(facture.getStatut());
        dto.setMontant(facture.getMontant());
        dto.setMontantPaye(facture.getMontantPaye());
        dto.setNumeroCompteur(facture.getNumeroCompteur());
        dto.setReleveId(facture.getReleve() != null ? facture.getReleve().getId() : null);
        if (facture.getCreateDate() != null) {
            dto.setDateTrs(LocalDateTime.ofInstant(facture.getCreateDate(), ZoneId.systemDefault())
                    .withNano(0).format(formatteur));
        }
        if (facture.getDateEcheance() != null) {
            dto.setDateEcheance(LocalDateTime.ofInstant(facture.getDateEcheance(), ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        return dto;
    }

    public static Facture Register(FactureDto dto) {
        if (dto == null) {
            return null;
        }
        Facture facture = new Facture();
        facture.setId(dto.getId());
        facture.setNumero(dto.getNumero());
        facture.setStatut(dto.getStatut());
        facture.setMontant(dto.getMontant());
        facture.setMontantPaye(dto.getMontantPaye());
        facture.appliquerEcheanceParDefaut();
        return facture;
    }

    // Getters / setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDateTrs() {
        return dateTrs;
    }

    public void setDateTrs(String dateTrs) {
        this.dateTrs = dateTrs;
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public StatutFact getStatut() {
        return statut;
    }

    public void setStatut(StatutFact statut) {
        this.statut = statut;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public BigDecimal getMontantPaye() {
        return montantPaye;
    }

    public void setMontantPaye(BigDecimal montantPaye) {
        this.montantPaye = montantPaye;
    }

    public String getNumeroCompteur() {
        return numeroCompteur;
    }

    public void setNumeroCompteur(String numeroCompteur) {
        this.numeroCompteur = numeroCompteur;
    }

    public Integer getReleveId() {
        return releveId;
    }

    public void setReleveId(Integer releveId) {
        this.releveId = releveId;
    }
}
