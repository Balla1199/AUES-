package com.aues.entites;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.math.BigDecimal;

/** Versement sur une facture : validé (Effectuer) ou annulé (Annule). */
@Entity
@Table(name = "Payement")
public class Payement extends EntiteAbstrait {

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montant = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModePayement mode = ModePayement.Especes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPay statutPay = StatutPay.Effectuer;

    /** Nom du payeur (client, agent…), information libre. */
    private String payeur;

    @ManyToOne
    @JoinColumn(name = "facture_id", nullable = false)
    @JsonBackReference
    private Facture facture;

    public Payement() {
        // createDate/modifieDate initialisées par défaut dans EntiteAbstrait.
    }

    // ------------------------------------------------------------------
    // Getters / setters
    // ------------------------------------------------------------------

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public ModePayement getMode() {
        return mode;
    }

    public void setMode(ModePayement mode) {
        this.mode = mode;
    }

    public StatutPay getStatutPay() {
        return statutPay;
    }

    public void setStatutPay(StatutPay statutPay) {
        this.statutPay = statutPay;
    }

    public String getPayeur() {
        return payeur;
    }

    public void setPayeur(String payeur) {
        this.payeur = payeur;
    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }
}
