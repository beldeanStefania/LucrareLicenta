package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.LoginDTO;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; 

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginDTO loginDTO) {
        Map<String, String> response = new HashMap<>();

        System.out.println("Request Body:");
        System.out.println("Username: " + loginDTO.getUsername());
        System.out.println("Password: " + loginDTO.getPassword());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtil.generateToken(loginDTO.getUsername());
            User user = userRepository.findByUsername(loginDTO.getUsername()).orElseThrow();

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
        request.getSession().invalidate();
        return ResponseEntity.ok("Logged out successfully.");
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String oldPassword = payload.get("oldPassword");
        String newPassword = payload.get("newPassword");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (passwordEncoder.matches(oldPassword, user.getPassword())) { 
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return ResponseEntity.ok("Password updated successfully");
        } else {
            return ResponseEntity.status(FORBIDDEN).body("Old password is incorrect");
        }
    }

    @GetMapping("/userInfo")
    public ResponseEntity<Map<String, String>> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return ResponseEntity.status(UNAUTHORIZED).build();
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Map<String, String> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("role", user.getRole().getName());

        if (user.getStudent() != null) {
            response.put("cod", user.getStudent().getCod());
            response.put("grupa", user.getStudent().getGrupa());
            response.put("an", user.getStudent().getAn().toString());
        } else if (user.getProfesor() != null) {
            response.put("profesorId", user.getProfesor().getId().toString());
            response.put("nume", user.getProfesor().getNume());
            response.put("prenume", user.getProfesor().getPrenume());
        } else {
            response.put("cod", "Not available");
            response.put("grupa", "Not available");
        }

        System.out.println("UserInfo response built:");
        System.out.println(response);

        return ResponseEntity.ok(response);
    }


}

