package com.orar.Backend.Orar.security;

import com.orar.Backend.Orar.service.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String KEY_FILE_PATH = "jwtSecretKey.key";
    private static SecretKey SECRET_KEY;

    private final UserDetailsServiceImpl userDetailsService;

    public JwtUtil(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.SECRET_KEY = loadOrGenerateKey();
    }

    private SecretKey loadOrGenerateKey() {
        try {
            File keyFile = new File(KEY_FILE_PATH);
            if (keyFile.exists()) {
                byte[] keyBytes = Files.readAllBytes(keyFile.toPath());
                return Keys.hmacShaKeyFor(keyBytes);
            } else {
                SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
                Files.write(keyFile.toPath(), key.getEncoded());
                return key;
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not load or generate secret key", e);
        }
    }

    // Generarea token-ului JWT
    public String generateToken(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Role not found"))
                .getAuthority(); // Obține rolul utilizatorului

        // Adaugă prefixul "ROLE_" dacă lipsește
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role) // Include rolul în token
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Valabil 10 ore
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }


    // Validarea token-ului
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY) // Utilizează cheia generată dinamic
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false; // Token-ul nu este valid
        }
    }

    // Extrage username-ul din token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Extrage toate informațiile (claims) din token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // Utilizează cheia generată dinamic
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Verifică dacă token-ul a expirat
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
