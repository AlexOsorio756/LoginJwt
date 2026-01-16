package com.login.app.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.login.app.Entity.User;
import com.login.app.Repository.userRepository;

@Service
public class userService {
    
    @Autowired
    private userRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;    

    public void initialRegistration(User user) {
        // Generamos un código aleatorio como "a1b2-c3d4"
        String secretCode = UUID.randomUUID().toString();
        user.setToken(secretCode);
        user.setActive(false); // No puede entrar todavía
        userRepository.save(user);
        
        // TIP: Aquí imprimirás en consola el enlace para que lo pruebes:
        System.out.println("Enlace de confirmación: http://localhost:8080/auth/confirmar.html?token=" + secretCode);
    }

    // PASO B: El usuario usa el token para poner su contraseña
    public boolean activeAccount(String token, String userPassword) {
        Optional<User> optionalUser = userRepository.findByToken(token);
        
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Encriptamos la contraseña
            user.setPassword(passwordEncoder.encode(userPassword));
            user.setActive(true); // ¡Ya puede entrar!
            user.setToken(null);  // Borramos el token para que no se use dos veces
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    // Este es el método que usará Spring Security
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

