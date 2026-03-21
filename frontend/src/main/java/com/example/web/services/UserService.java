package com.example.web.services;

import com.example.web.models.User;
import com.example.web.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder;
    //Ici passwordHash temporairement contient le mot de passe
    // en clair depuis le formulaire ➝ avant d’être encodé.
    public UserService(UserRepository repo, BCryptPasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public List<User> findAll() {
        return repo.findAll();
    }

    public User create(User user) {
        user.setPasswordHash(encoder.encode(user.getPasswordHash()));
        return repo.save(user);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}