package com.aues;

/** Corps de la requête d'enregistrement d'un paiement. */
public class PayementRequest {
    private Integer factureId;
    private Double montant;
    private String mode;    // Especes, MobileMoney, Virement, Cheque
    private String payeur;

    public Integer getFactureId() {
        return factureId;
    }

    public void setFactureId(Integer factureId) {
        this.factureId = factureId;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPayeur() {
        return payeur;
    }

    public void setPayeur(String payeur) {
        this.payeur = payeur;
    }
}
