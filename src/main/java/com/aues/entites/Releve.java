package com.aues.entites;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Releve")
public class Releve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    public Instant date = Instant.now();

    @Column(nullable = false)
    public Double unite_consomme;


    public Double ValLorsRelev;

    private Double tarif;;


    @ManyToOne
    @JoinColumn(name = "compteur_id", nullable = false) // Crée une clé étrangère vers la table Compteur
    @JsonBackReference
    private Compteur compteur;

    @OneToOne(mappedBy = "releve")
    @JoinColumn(name = "Facture")
    private Facture facture;


    public Double getValLorsRelev() {
        return ValLorsRelev;
    }

    public void setValLorsRelev(Double valActu) {
        this.ValLorsRelev = valActu;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Double getUnite_consomme() {
        return unite_consomme;
    }

    public void setUnite_consomme(Double unite_consomme) {
        this.unite_consomme = unite_consomme;
    }

    public Double getTarif() {
        return tarif;
    }

    public void setTarif(Double tarif) {
        this.tarif = tarif;
    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    public Compteur getCompteur() {
        return compteur;
    }

    public void setCompteur(Compteur compteur) {
        this.compteur = compteur;
    }


}
