package com.aues.securite;



import com.aues.entites.Utilisateur;
import com.aues.services.UtilisateurService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Service
public class JwtFilter extends OncePerRequestFilter {
    private UtilisateurService utilisateurService;
    private JwtService jwtService;

    public JwtFilter(UtilisateurService utilisateurService, JwtService jwtService) {
        this.utilisateurService = utilisateurService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        String username=null;
        boolean isTokenExpired=true;
//Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRyYWJvdWJrYXJpYmFAZ21haWwuY29tIiwibm9tIjoiQm91YmFjYXIgdHJhb3JlIn0.CGQAKseW1i18prhaHRpqxb9P1N6SHopcG2KwgXw7FGY
        String authorization = request.getHeader("Authorization");
        if(authorization !=null && authorization.startsWith("Bearer ")){
            token = authorization.substring(7);
            isTokenExpired=jwtService.isTokenExpired(token);
            username=jwtService.lireUsername(token);

        }
        if(!isTokenExpired && username != null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = utilisateurService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request,response);

    }


    public UtilisateurService getUtilisateurService() {
        return utilisateurService;
    }

    public void setUtilisateurService(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    public JwtService getJwtService() {
        return jwtService;
    }

    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }
}
