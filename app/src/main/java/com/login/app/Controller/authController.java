package com.login.app.Controller;
import com.login.app.Config.jwtUtil;
import com.login.app.Dto.forgotPasswordRequest;
import com.login.app.Entity.User;
import com.login.app.Repository.userRepository;
import com.login.app.Service.emailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;

@Tag(name = "Autenticación", description = "Operaciones relacionadas con el acceso, registro y recuperación de cuenta")
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

    @Operation(
    summary = "Iniciar sesión", description = "Recibe las credenciales del usuario y devuelve un token JWT si son correctas.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Se inicio sesión correctamente"),
    @ApiResponse(responseCode = "404", description = "No existe el usuario con ese nombre"),
    @ApiResponse(responseCode = "500", description = "Error interno al enviar el correo")
})
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

    @Operation(
    summary = "Recuperar contraseña", description = "Envía un correo electrónico con un enlace para restablecer la contraseña y recibe un request que es el correo del usuario.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Correo enviado con éxito"),
    @ApiResponse(responseCode = "404", description = "No se encontró el usuario con ese email"),
    @ApiResponse(responseCode = "500", description = "Error interno al enviar el correo")
})
    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody forgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new RuntimeException("No existe un usuario con ese correo"));

        String token = UUID.randomUUID().toString();
        user.setValidationToken(token);
        userRepository.save(user);
        emailService.sendEmailForgotPassword(user.getEmail(), token);
        

        return ResponseEntity.ok(Map.of("message", "Reset password email sent"));
    }
    
}