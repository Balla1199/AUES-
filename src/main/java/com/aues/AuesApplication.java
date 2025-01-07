package com.aues;

import com.aues.services.AdminInitializationService;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuesApplication {
	private final AdminInitializationService adminInitializationService;


	public AuesApplication(AdminInitializationService adminInitializationService) {
		this.adminInitializationService = adminInitializationService;
	}


	public static void main(String[] args) {
		SpringApplication.run(AuesApplication.class, args);
	}
	@PostConstruct
	public void init() {
		adminInitializationService.initializeAdmin();
	}

}



//package com.aues;
//
//import com.aues.entites.Role;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//import com.aues.entites.Utilisateur;
//import com.aues.repositories.UtilisateurRepository;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//@SpringBootApplication
//public class AuesApplication {
//
////    private final BCryptPasswordEncoder bCryptPasswordEncoder;
////
//////    @Autowired
////    public AuesApplication(BCryptPasswordEncoder bCryptPasswordEncoder) {
////        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
////    }
//
//	public static void main(String[] args) {
//		SpringApplication.run(AuesApplication.class, args);
//	}
//
//    @Bean
//    CommandLineRunner initDatabase(UtilisateurRepository utilisateurRepository) {
//        return args -> {
//            // Vérifiez si un administrateur existe déjà
//            if (utilisateurRepository.findByRole(Role.ADMINISTRATEUR).isEmpty()) {
//                Utilisateur admin = new Utilisateur();
//                admin.setNom("Admin");
//                admin.setAdresse("Adresse par défaut");
//                admin.setTelephone("0123456789");
//                admin.setMotDePasse(new BCryptPasswordEncoder().encode("admin123")); // Mot de passe brut
//
////                // Encoder le mot de passe
////                String crypt = bCryptPasswordEncoder.encode(admin.getMotDePasse());
////                admin.setMotDePasse(crypt);
//
//                admin.setRole(Role.ADMINISTRATEUR);
//                utilisateurRepository.save(admin);
//
//                System.out.println("Administrateur initial ajouté avec succès !");
//            } else {
//                System.out.println("Administrateur déjà présent !");
//            }
//        };
//    }
//}

