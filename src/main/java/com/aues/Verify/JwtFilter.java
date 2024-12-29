package com.aues.Verify;

import com.aues.repositories.JwtUtil;
import com.aues.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Récupère l'en-tête Authorization
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Vérifie si l'en-tête contient un JWT au format Bearer
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Extrait le jeton après "Bearer "
            username = jwtUtil.extractUsername(jwt); // Récupère le nom d'utilisateur à partir du JWT
        }

        // Si le nom d'utilisateur est valide et qu'il n'y a pas encore d'authentification dans le contexte
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Charge les détails de l'utilisateur

            // Valide le JWT
            if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                // Crée une authentification
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Ajoute l'authentification au contexte de sécurité
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Passe la requête au filtre suivant
        chain.doFilter(request, response);
    }
}
