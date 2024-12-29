package com.aues.Verify;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class SecurityConfig {

    // Bean pour encoder les mots de passe
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean pour le gestionnaire d'authentification
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Configuration personnalisée du firewall pour autoriser certains caractères
    @Bean
    public HttpFirewall customHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true); // Autorise les slashs encodés
        firewall.setAllowUrlEncodedPercent(true); // Autorise les pourcentages encodés
        firewall.setAllowBackSlash(true); // Autorise les backslashes
        firewall.setAllowSemicolon(true); // Autorise les points-virgules
        firewall.setAllowUrlEncodedDoubleSlash(true); // Autorise les doubles slash encodés
        return firewall;
    }

    // Configuration de la chaîne de sécurité
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Désactiver CSRF (à réactiver si nécessaire pour protéger les formulaires)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll() // Autorise les points de terminaison d'authentification
                .anyRequest().authenticated() // Toutes les autres requêtes nécessitent une authentification
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> 
                    response.sendError(401, "Unauthorized")) // Gestion des erreurs 401
            );

        return http.build();
    }
}
