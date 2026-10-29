package com.aues.entites;

import com.aues.ModePayement;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Date;

/** Versement enregistré sur une facture (partiel ou total). */
@Entity
@Table(name = "Payement")
public class Payement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date date;

    @Column(nullable = false)
    private Double montant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModePayement mode = ModePayement.Especes;

    /** Nom du payeur (client, agent…), information libre. */
    private String payeur;

    public Payement() {
        this.date = new Date();
        this.montant = 0.0;
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

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public ModePayement getMode() {
        return mode;
    }

    public void setMode(ModePayement mode) {
        this.mode = mode;
    }

    public String getPayeur() {
        return payeur;
    }

    public void setPayeur(String payeur) {
        this.payeur = payeur;
    }

    @ManyToOne
    @JoinColumn(name = "facture_id", nullable = false)
    @JsonBackReference("payement_facture")
    private Facture facture;

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }
}
