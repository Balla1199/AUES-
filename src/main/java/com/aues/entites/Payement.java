package com.aues.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;


@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payement extends EntiteAbstrait {

    @Column(name = "Date")
    private Instant date = Instant.now();

    @Column(name = "Montant")
    private Integer montant;

    @Column(name = "Mode")
    @Enumerated(EnumType.STRING)
    protected ModePayement mode;

    @Column(name = "Statuts")
    @Enumerated(EnumType.STRING)
    protected StatutPay statutPay;

    @ManyToOne
    @JoinColumn(name = "idFacture")
    private Facture Facture;


    public Instant getDate() {
        return date;
    }

    public Integer getMontant() {
        return montant;
    }

    public ModePayement getMode() {
        return mode;
    }

    public Facture getFacture() {
        return Facture;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public void setMontant(Integer montant) {
        this.montant = montant;
    }

    public void setMode(ModePayement mode) {
        this.mode = mode;
    }

    public void setFacture(Facture facture) {
        Facture = facture;
    }

    public StatutPay getStatutPay() {
        return statutPay;
    }

    public void setStatutPay(StatutPay statutPay) {
        this.statutPay = statutPay;
    }
}
