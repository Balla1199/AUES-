package com.aues.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Facture extends EntiteAbstrait {

    @Column(name = "Statuts")
    @Enumerated(EnumType.STRING)
    protected StatutFact statut;

    @Column(name = "Montant")
    private Long montantTotal;

    @OneToMany(mappedBy = "Facture",
            cascade = CascadeType.ALL)
    private List<Payement> payementlist;

    @OneToOne(cascade = CascadeType.ALL)
    private Releve releve;

    public StatutFact getStatut() {
        return statut;
    }

    public Long getMontantTotal() {
        return montantTotal;
    }

    public List<Payement> getPayementlist() {
        return payementlist;
    }

    public Releve getReleve() {
        return releve;
    }

    public void setReleve(Releve releve) {
        this.releve = releve;
    }

    public void setStatut(StatutFact statut) {
        this.statut = statut;
    }

    public void setMontantTotal(Long montantTotal) {
        this.montantTotal = montantTotal;
    }

    public void setPayementlist(List<Payement> payementlist) {
        this.payementlist = payementlist;
    }
}
