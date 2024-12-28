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
    private Integer montantTotal;

    @OneToMany(mappedBy = "Facture",
            cascade = CascadeType.ALL)
    private List<Payement> payementlist;

    public StatutFact getStatut() {
        return statut;
    }

    public Integer getMontantTotal() {
        return montantTotal;
    }

    public List<Payement> getPayementlist() {
        return payementlist;
    }

    public void setStatut(StatutFact statut) {
        this.statut = statut;
    }

    public void setMontantTotal(Integer montantTotal) {
        this.montantTotal = montantTotal;
    }

    public void setPayementlist(List<Payement> payementlist) {
        this.payementlist = payementlist;
    }
}
