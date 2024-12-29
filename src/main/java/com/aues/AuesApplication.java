package com.aues;



import com.aues.entites.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.aues.entites.Utilisateur;
import com.aues.repositories.UtilisateurRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AuesApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuesApplication.class, args);
	}
@Autowired
private PasswordEncoder passwordEncoder;

@Bean
CommandLineRunner initDatabase(UtilisateurRepository utilisateurRepository) {
    return args -> {
        if (utilisateurRepository.findByRole(Role.ADMINISTRATEUR).isEmpty()) {
            Utilisateur admin = new Utilisateur();
            admin.setNom("Admin");
            admin.setAdresse("Adresse par défaut");
            admin.setTelephone("70700000");
            admin.setMotDePasse(passwordEncoder.encode("sidiki123")); // Hachage du mot de passe
            admin.setRole(Role.ADMINISTRATEUR);

            utilisateurRepository.save(admin);
            System.out.println("Administrateur initial ajouté avec succès !");
        } else {
            System.out.println("Administrateur déjà présent !");
        }
    };
}

}
