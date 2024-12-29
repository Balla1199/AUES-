package com.aues.entites;

public class JwtResponse {
    private String token;
    private String message;

    // Constructeurs
    public JwtResponse(String message) {
        this.message = message;
    }

    public JwtResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    // Getters et Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
