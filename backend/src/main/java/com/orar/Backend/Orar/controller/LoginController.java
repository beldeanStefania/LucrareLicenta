package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.LoginDTO;
import com.orar.Backend.Orar.model.Rol;
import com.orar.Backend.Orar.security.JwtUtil;
import com.orar.Backend.Orar.model.User;
import com.orar.Backend.Orar.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginDTO loginDTO) {
        Map<String, String> response = new HashMap<>();

        System.out.println("Request Body:");
        System.out.println("Username: " + loginDTO.getUsername());
        System.out.println("Password: " + loginDTO.getPassword());

        try {
            // Autentifică utilizatorul
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );

            // Setează autentificarea în contextul de securitate
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generează token-ul JWT pentru utilizatorul autentificat
            String token = jwtUtil.generateToken(loginDTO.getUsername());

            // Obține rolul utilizatorului din UserRepository
            User user = userRepository.findByUsername(loginDTO.getUsername()).orElseThrow();

            // Adaugă token-ul și rolul în răspuns
            response.put("token", token);
            response.put("role", user.getRole().getName());
            return response;
        } catch (AuthenticationException e) {
            response.put("error", "Error: " + e.getMessage());
            return response;
        }
    }

    @PostMapping("/api/auth/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // Invalidează sesiunea curentă
        request.getSession().invalidate();
        return ResponseEntity.ok("Logged out successfully.");
    }

}
