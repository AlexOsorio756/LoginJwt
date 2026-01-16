package com.login.app.Controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.login.app.Entity.User;
import com.login.app.Service.userService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(path = "api/users")
public class userController {

    @Autowired
    private  userService userService ;

    @GetMapping
    public String home() {
        return "Welcome to User Management API";
    }

    // 1. Recibe Nombre y Correo
    @PostMapping("/signin")
    public User signIn(@RequestBody User user) {
        userService.initialRegistration(user);
        return user;
    }

    // 2. Recibe el Token y la Password desde la nueva página HTML
    @PostMapping("/confirm")
    public String confirm(@RequestBody Map<String, String> data) {
        String token = data.get("token");
        String password = data.get("password");
        
        boolean exito = userService.activeAccount(token, password);
        return exito ? "Cuenta activada con éxito" : "Token inválido";
    }

    @GetMapping("/{userId}")
    public Optional<User> getUserById(@PathVariable("userId") Long userId) {
        return userService.getUserById(userId);
    }

    @Autowired
    private PasswordEncoder passwordEncoder; // Inyectamos el encriptador

    @PostMapping
    public void saveUpdateUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
    }
    
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
    }

}
