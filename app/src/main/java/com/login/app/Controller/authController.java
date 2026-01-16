package com.login.app.Controller;
import com.login.app.Config.jwtUtil;
import com.login.app.Dto.forgotPasswordRequest;
import com.login.app.Entity.User;
import com.login.app.Repository.userRepository;
import com.login.app.Service.emailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
public class authController {

    @Autowired
    private userRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private jwtUtil jwtUtil;

    @Autowired
    private emailService emailService;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isActive()) {
            throw new RuntimeException("The account is not activated. Check your email.");
        }

        if (passwordEncoder.matches(password, user.getPassword())) {
            //Si todo es correcto generamos el token
            String token = jwtUtil.generateToken(username);
            
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "Login successful");
            return response;
        } else {
            throw new RuntimeException("Incorrect password");
        }
    }

    @PostMapping("/api/auth/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody forgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new RuntimeException("No existe un usuario con ese correo"));

        String token = UUID.randomUUID().toString();
        user.setValidationToken(token);
        userRepository.save(user);
        emailService.sendEmailForgotPassword(user.getEmail(), token);
        

        return ResponseEntity.ok(Map.of("message", "Reset password email sent"));
    }
    
}