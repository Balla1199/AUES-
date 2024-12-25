package com.aues.entites;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Releve")
public class Releve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    public Date date;

    @Column(nullable = false)
    public Double unite_consomme;

    public Double tarif=250.5;

    private String numero_compteur;

    @ManyToOne
    @JoinColumn(name = "compteur_id", nullable = false) // Crée une clé étrangère vers la table Compteur
    @JsonBackReference
    private Compteur compteur;


    public Releve() {
        this.date = new Date();
    }

    public Releve(int id, Date date, Double unite_consomme, Double tarif, Compteur compteur, String numero_compteur) {
        this.id = id;
        this.date = date;
        this.unite_consomme = unite_consomme;
        this.tarif = tarif;
        this.compteur = compteur;
        this.numero_compteur = numero_compteur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public Compteur getCompteur() {
        return compteur;
    }

    public void setCompteur(Compteur compteur) {
        this.compteur = compteur;
    }

    public String getNumero_compteur() {
        return numero_compteur;
    }

    public void setNumero_compteur(String numero_compteur) {
        this.numero_compteur = numero_compteur;
    }
}
