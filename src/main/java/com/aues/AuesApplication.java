package com.aues;

import javax.management.relation.Role;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.aues.entites.Utilisateur;
import com.aues.repositories.UtilisateurRepository;

@SpringBootApplication
public class AuesApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuesApplication.class, args);
	}
 @Bean
    CommandLineRunner initDatabase(UtilisateurRepository utilisateurRepository) {
        return args -> {
            // Vérifiez si un administrateur existe déjà
            if (utilisateurRepository.findByRole(Role.ADMINISTRATEUR).isEmpty()) {
                Utilisateur admin = new Utilisateur();
                admin.setNom("Admin");
                admin.setAdresse("Adresse par défaut");
                admin.setTelephone("0123456789");
                admin.setMotDePasse("admin123"); // Vous pouvez le hacher avec BCryptEncoder
                admin.setRole(Role.ADMINISTRATEUR);
                
                utilisateurRepository.save(admin);
                System.out.println("Administrateur initial ajouté avec succès !");
            } else {
                System.out.println("Administrateur déjà présent !");
            }
        };
    }
}
