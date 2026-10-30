package com.aues.entites;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Facture d'eau générée automatiquement depuis un relevé.
 *
 * Conception retenue (fusion) :
 * - montant : immuable (= unités consommées × tarif du relevé) ;
 * - montantPaye : cumulatif, mis à jour par les paiements ;
 * - statut : recalculé (Payee / Impayee), jamais figé manuellement ;
 * - le montant d'origine est toujours conservé (contrairement à un
 *   décrément du total, qui masquerait le taux de recouvrement).
 */
@Entity
@Table(name = "Facture")
public class Facture extends EntiteAbstrait {

    /** Numéro de facture lisible, ex. FACT-CPT001-42. */
    @Column(unique = true, nullable = false)
    private String numero;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montant = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montantPaye = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutFact statut = StatutFact.Impayee;

    /** Échéance de paiement : createDate + 30 jours (par défaut). */
    private Instant dateEcheance;

    /** Relevé d'où la facture provient (propriétaire de la relation). */
    @OneToOne
    @JoinColumn(name = "releve_id", unique = true, nullable = false)
    @JsonBackReference
    private Releve releve;

    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Payement> payements = new ArrayList<>();

    public Facture() {
        // createDate/modifieDate : valeurs par défaut de EntiteAbstrait.
    }

    // ------------------------------------------------------------------
    // Règles métier
    // ------------------------------------------------------------------

    /** Fixe l'échéance à la date de création + 30 jours. */
    public void appliquerEcheanceParDefaut() {
        Instant base = getCreateDate() == null ? Instant.now() : getCreateDate();
        this.dateEcheance = base.plus(30, ChronoUnit.DAYS);
    }

    /** Reste à payer. */
    public BigDecimal montantRestant() {
        return montant.subtract(montantPaye).max(BigDecimal.ZERO);
    }

    /** Recalcule le statut à partir du montant payé. */
    public void recalculerStatut() {
        this.statut = montantPaye.compareTo(montant) >= 0
                ? StatutFact.Payee
                : StatutFact.Impayee;
    }

    /** Numéro du compteur concerné, lu depuis le relevé (affichage). */
    public String getNumeroCompteur() {
        return releve != null && releve.getCompteur() != null
                ? releve.getCompteur().getCode() : null;
    }

    // ------------------------------------------------------------------
    // Getters / setters
    // ------------------------------------------------------------------

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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

    public StatutFact getStatut() {
        return statut;
    }

    public void setStatut(StatutFact statut) {
        this.statut = statut;
    }

    public Instant getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(Instant dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public Releve getReleve() {
        return releve;
    }

    public void setReleve(Releve releve) {
        this.releve = releve;
    }

    public List<Payement> getPayements() {
        return payements;
    }

    public void setPayements(List<Payement> payements) {
        this.payements = payements;
    }
}
