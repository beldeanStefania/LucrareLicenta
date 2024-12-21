package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.LoginDTO;
import com.orar.Backend.Orar.model.Rol;
import com.orar.Backend.Orar.model.Student;
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

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByUsername(loginDTO.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Map<String, Object> claims = new HashMap<>();
            claims.put("role", user.getRole().getName());

            if ("STUDENT".equals(user.getRole().getName())) {
                Student student = user.getStudent(); // Relația directă cu Student
                claims.put("studentCod", student.getCod());
            }

            String token = jwtUtil.generateToken(claims, user.getUsername());
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
