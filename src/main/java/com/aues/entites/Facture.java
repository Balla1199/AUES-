package com.aues.entites;

import com.aues.StatutFacture;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Facture d'eau générée automatiquement depuis un relevé de compteur :
 * montant = unite_consomme × tarif du relevé.
 */
@Entity
@Table(name = "Facture")
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** Numéro de facture lisible, ex. FACT-CPT001-42. */
    @Column(unique = true, nullable = false)
    private String numero;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date date_emission;

    /** Émission + 30 jours. */
    @Temporal(TemporalType.TIMESTAMP)
    private Date date_echeance;

    @Column(nullable = false)
    private Double montant = 0.0;

    @Column(nullable = false)
    private Double montant_paye = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutFacture statut = StatutFacture.Impayee;

    /** Copie du numéro de compteur, pour lecture directe de la facture. */
    private String numero_compteur;

    public Facture() {
        this.date_emission = new Date();
    }

    // ------------------------------------------------------------------
    // Règles métier
    // ------------------------------------------------------------------

    /** Reste à payer sur la facture. */
    public Double montantRestant() {
        return Math.max(0.0, montant - montant_paye);
    }

    /** Met à jour le statut à partir du montant payé. */
    public void recalculerStatut() {
        this.statut = montant_paye >= montant ? StatutFacture.Payee : StatutFacture.Impayee;
    }

    // ------------------------------------------------------------------
    // Getters / setters
    // ------------------------------------------------------------------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getDate_emission() {
        return date_emission;
    }

    public void setDate_emission(Date date_emission) {
        this.date_emission = date_emission;
    }

    public Date getDate_echeance() {
        return date_echeance;
    }

    /** Échéance = émission + 30 jours si non renseignée. */
    public void setDate_echeance(Date date_echeance) {
        this.date_echeance = date_echeance;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Double getMontant_paye() {
        return montant_paye;
    }

    public void setMontant_paye(Double montant_paye) {
        this.montant_paye = montant_paye;
    }

    public StatutFacture getStatut() {
        return statut;
    }

    public void setStatut(StatutFacture statut) {
        this.statut = statut;
    }

    public String getNumero_compteur() {
        return numero_compteur;
    }

    public void setNumero_compteur(String numero_compteur) {
        this.numero_compteur = numero_compteur;
    }

    @ManyToOne
    @JoinColumn(name = "releve_id", unique = true, nullable = false)
    @JsonBackReference("facture_releve")
    private Releve releve;

    public Releve getReleve() {
        return releve;
    }

    public void setReleve(Releve releve) {
        this.releve = releve;
    }
}
