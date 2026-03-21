package com.example.web;

import com.example.web.models.User;
import com.example.web.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = {
        "com.example.web" // Fuerza la lectura del repertorio al arranque
})
public class FrontendApplication {
    public static void main(String[] args) {
        SpringApplication.run(FrontendApplication.class, args);
    }
    /* Bean creado inicialmente para tener un primer admin con su contraseña generad automáticamente
    @Bean
    CommandLineRunner init(UserRepository repo, BCryptPasswordEncoder encoder) {
        return args -> {
            if (repo.findByUsername("admin").isEmpty()) {
                User u = new User();
                u.setUsername("admin");
                u.setPasswordHash(encoder.encode("admin"));
                u.setEmail("admin@example.com");
                u.setRole("ADMIN");
                repo.save(u);
            }
        };
    }

     */
    @Bean
    RestTemplate restTemplate() { return new RestTemplate(); }
}
