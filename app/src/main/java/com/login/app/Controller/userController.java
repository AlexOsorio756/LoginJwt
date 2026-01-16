package com.login.app.Controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.login.app.Entity.User;
import com.login.app.Service.userService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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

    @Operation(
    summary = "Registrarse", description = "Recibe username(nombre completo) y email del usuario para enviarle un correo de confirmacion.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Se registro el usuario correctamente"),
    @ApiResponse(responseCode = "404", description = "Algun dato es incorrecto"),
    @ApiResponse(responseCode = "500", description = "Error interno al enviar el correo")
})
    @PostMapping("/signin")
    public User signIn(@RequestBody User user) {
        userService.initialRegistration(user);
        return user;
    }

    @Operation(
    summary = "Confirmar la creacion de usuario", description = "Recibe la contraseña y el token enviado por correo para activar la cuenta.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Se creo el usuario correctamente en la base de datos"),
    @ApiResponse(responseCode = "404", description = "Algun dato es incorrecto para creear el usuario o ya se creo"),
    @ApiResponse(responseCode = "500", description = "Error interno al enviar el correo")
})
    @PostMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String password = request.get("password");

        System.out.println(token); 
        userService.activeAccount(token, password); 

        return ResponseEntity.ok(Map.of("message", "Usuario activado"));
    }
    @Operation(
    summary = "Ver usuario por id", description = "Muestra el usuario con el id dado si existe")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Devolvio el usuario correctamente"),
    @ApiResponse(responseCode = "404", description = "El usuario con ese id no existe"),
    @ApiResponse(responseCode = "500", description = "Error interno al enviar el correo")
})
    @GetMapping("/{userId}")
    public Optional<User> getUserById(@PathVariable("userId") Long userId) {
        return userService.getUserById(userId);
    }

    @Autowired
    private PasswordEncoder passwordEncoder; 

    @Operation(
    summary = "Guardar/Actualizar", description = "Guarda o actualiza el usuario en la base de datos, en caso de nueva creación cifra la contraseña.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Se guardo/actualizo el usuario correctamente"),
    @ApiResponse(responseCode = "404", description = "El usuario no se pudo guardar/actualizar"),
    @ApiResponse(responseCode = "500", description = "Error interno al enviar el correo")
})
    @PostMapping
    public void saveUpdateUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
    }
    
    @Operation(
    summary = "Eliminar", description = "Elimina el usuario de la base de datos.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Se elimino el usuario correctamente"),
    @ApiResponse(responseCode = "404", description = "El usuario no se pudo borrar porque no existe"),
    @ApiResponse(responseCode = "500", description = "Error interno al enviar el correo")
})
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
    }

}
