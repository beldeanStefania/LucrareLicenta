package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.LoginDTO;
import com.orar.Backend.Orar.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );

            // Generează token-ul JWT pentru utilizatorul autentificat
            String token = jwtUtil.generateToken(loginDTO.getUsername());
            return token;
        } catch (AuthenticationException e) {
            return "Error: " + e.getMessage();
        }
    }
}
