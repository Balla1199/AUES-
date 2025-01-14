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

    @Column(name = "Numero", unique = true)
    private String code;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public TypeStatut statut=TypeStatut.Inactive;

    @Column(name = "Tableau", nullable = false)
    public Double CpteurChiffre;

    @OneToMany(mappedBy = "compteur", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Releve> releve = new ArrayList<>();

    public Compteur() {

    }

    public Compteur(int id, String code, TypeStatut statut, List<Releve> releve) {
        this.id = id;
        this.code = code;
        this.statut = statut;
        this.releve = releve;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Double getCpteurChiffre() {
        return CpteurChiffre;
    }

    public void setCpteurChiffre(Double cpteurChiffre) {
        this.CpteurChiffre = cpteurChiffre;
    }

    public void setCode(String code) {
        this.code = code;
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

