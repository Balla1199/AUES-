package com.aues.DTO;



public class LoginRequest {
    private String telephone;
    private String motDePasse;

    // Constructeur par défaut
    public LoginRequest() {}

    // Constructeur avec paramètres
    public LoginRequest(String telephone, String motDePasse) {
        this.telephone = telephone;
        this.motDePasse = motDePasse;
    }

    // Getters et setters
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}

