package com.login.app.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.login.app.Entity.User;
import com.login.app.Repository.userRepository;

import jakarta.transaction.Transactional;

@Service
public class userService {
    
    @Autowired
    private userRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;  
    @Autowired
    private emailService emailService; 

    public void initialRegistration(User user) {

        String secretCode = UUID.randomUUID().toString();
        user.setValidationToken(secretCode);
        user.setToken(secretCode);
        user.setActive(false);
        userRepository.save(user);

        //Enviamos correo a mailtrap
        emailService.sendMailConfirm(user.getEmail(), secretCode);
    
    }

    @Transactional
    public void activeAccount(String token, String passwordPlano) {
        User user = userRepository.findByValidationToken(token)
            .orElseThrow(() -> new RuntimeException("The link has expired or already been used."));

        user.setPassword(passwordEncoder.encode(passwordPlano));
        user.setActive(true);
        
        // Se elimina el token para que no se pueda usar de nuevo
        user.setValidationToken(null); 
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    public Optional<User> getUserByName(String username) {
        return userRepository.findByUsername(username);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

