package com.aues.entites;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class Utilisateur implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String  nom;

    private String adresse;

    @Column(unique = true)
    private String telephone;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;


    public Utilisateur() {}

    public Utilisateur(String telephone, String password, Role role) {
        this.telephone = telephone;
        this.password = password;
        this.role = role;
    }

    // Getters et setters

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convertir le rôle en une autorité que Spring Security peut comprendre
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name())); // Ajoute le préfixe ROLE_
    }

    @Override
    public String getUsername() {
        return telephone; // Utilise le téléphone comme nom d'utilisateur
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // L'utilisateur n'a pas d'expiration d'account
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // L'utilisateur n'est pas verrouillé
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Les identifiants de l'utilisateur ne sont pas expirés
    }

    @Override
    public boolean isEnabled() {
        return true; // L'utilisateur est activé par défaut
    }
}
