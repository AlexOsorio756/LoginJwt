package com.login.app.Config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class jwtFilter extends OncePerRequestFilter {

    @Autowired
    private jwtUtil jwtUtil;

    @Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    String path = request.getRequestURI();
    // Si la ruta es de autenticación dejamos pasar para evitar errores en el frontend
    if (path.startsWith("/api/auth/")) {
        filterChain.doFilter(request, response);
        return;
    }

    // Si no hay Header en el token o no empieza con Bearer seguimos al sigiuiente filtro
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        filterChain.doFilter(request, response);
        return;
    }

    String token = authHeader.substring(7);
    
    try {
        String username = jwtUtil.extractUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Creamos el token de autenticacion 
            UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    } catch (Exception e) {
        //Si el token es incorrecto o ha habido un error limpiamos
        SecurityContextHolder.clearContext();
    }

    filterChain.doFilter(request, response);
}
}