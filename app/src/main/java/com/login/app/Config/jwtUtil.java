package com.login.app.Config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class jwtUtil {

    private static final SecretKey key = Jwts.SIG.HS256.key().build();
    private static final long EXPIRATION_TIME = 86400000; // 1 dia en milisegundos

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        //Sacamos la informacion que necesitamos del token
        return Jwts.parser()
                .verifyWith(key) 
                .build()
                .parseSignedClaims(token) 
                .getPayload()
                .getSubject();
    }
}