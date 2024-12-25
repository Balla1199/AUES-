package com.aues.entites;

import com.aues.TypeStatut;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Compteur")
public class Compteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String numero;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public TypeStatut statut=TypeStatut.Inactive;

    @OneToMany(mappedBy = "compteur", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Releve> releve = new ArrayList<>();

    public Compteur() {

    }

    public Compteur(int id, String numero, TypeStatut statut, List<Releve> releve) {
        this.id = id;
        this.numero = numero;
        this.statut = statut;
        this.releve = releve;
    }

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

    public TypeStatut getStatut() {
        return statut;
    }

    public void setStatut(TypeStatut statut) {
        this.statut = statut;
    }


    public List<Releve> getReleve() {
        return releve;
    }

    public void setReleve(List<Releve> releve) {
        this.releve = releve;
    }
}

