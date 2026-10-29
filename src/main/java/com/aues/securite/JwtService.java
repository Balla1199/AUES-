package com.aues.securite;

import com.aues.entites.Utilisateur;
import com.aues.services.UtilisateurService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private  String ENCRYPTION_KEY = "76cc7470d2cc47c0f8f02b1f88d9944b8fde00d8755ec5891821936f0c37f80f";
    private  UtilisateurService utilisateurService;

    public JwtService(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    public String lireUsername(String token) {
        return this.getClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = this.getClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }
//    private  Date getExpirationDateFromToken(String token){
//        return this.getClaim(token,Claims::getExpiration);
//    }

    private <T> T getClaim(String token, Function<Claims, T> function) {
        Claims claims = getAllClaims(token);
        return function.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Map<String, String> generate(String username) {
        Utilisateur utilisateur = (Utilisateur) utilisateurService.loadUserByUsername(username);
        if (utilisateur == null) {
            throw new RuntimeException("Utilisateur introuvable avec le nom d'utilisateur : " + username);
        }
        return this.generateJwt(utilisateur);
    }
    long currentTime = System.currentTimeMillis();
    long currentExpire = currentTime + 30 * 60 * 1000;
    private Map<String, String> generateJwt(Utilisateur utilisateur) {
        Map<String, Object> claims = Map.of(
                "email", utilisateur.getEmail(),
                Claims.EXPIRATION,new Date(currentExpire),
                Claims.SUBJECT,utilisateur.getEmail()
        );

        String bearer = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentExpire))
                .setSubject(utilisateur.getEmail())
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        return Map.of("bearer", bearer);
    }

    private Key getKey() {
        byte[] decoder = Decoders.BASE64.decode(ENCRYPTION_KEY);
        return Keys.hmacShaKeyFor(decoder);
    }
}
