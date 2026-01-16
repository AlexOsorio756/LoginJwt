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

    //Generamos el token y guardamos el usuario inactivo por el momento
    public void registUserFirstTime(User user) {
        user.setToken(UUID.randomUUID().toString());
        user.setActive(false);
        userRepository.save(user);
    }

    // Verficiamos que el token es válido y activamos la cuenta
    public boolean confirmAccount(String token, String password) {
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
