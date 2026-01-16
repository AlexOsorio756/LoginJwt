package com.login.app.Service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.login.app.Entity.User;
import com.login.app.Repository.userRepository;

@Service
public class authService {

    @Autowired
    private userRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. Generar token y guardar usuario inactivo
    public void registrarProvisoriamente(User user) {
        user.setToken(UUID.randomUUID().toString());
        user.setActive(false);
        userRepository.save(user);
        // AQUÍ enviarías el correo con: "http://localhost:8080/auth/confirmar.html?token=" + user.getToken()
    }

    // 2. Validar token y asignar contraseña
    public boolean confirmarCuenta(String token, String password) {
        Optional<User> userOpt = userRepository.findByToken(token);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(passwordEncoder.encode(password));
            user.setToken(null); // Borramos el token para que no se use 2 veces
            user.setActive(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
