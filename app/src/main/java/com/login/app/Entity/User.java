package com.login.app.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    private String token; 
    private boolean active = false; 
    private String validationToken;
}
