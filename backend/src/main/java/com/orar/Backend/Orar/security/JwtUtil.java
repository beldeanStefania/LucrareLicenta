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

    // Multiple possible paths for finding the key file
    private static final String[] KEY_FILE_PATHS = {
        "/app/jwtSecretKey.key",
        "jwtSecretKey.key",
        "../jwtSecretKey.key",
        System.getProperty("user.dir") + "/jwtSecretKey.key"
    };
    private static SecretKey SECRET_KEY;

    private final UserDetailsServiceImpl userDetailsService;

    public JwtUtil(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.SECRET_KEY = loadOrGenerateKey();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    private SecretKey loadOrGenerateKey() {
        // Try to find the key file in any of the potential locations
        for (String path : KEY_FILE_PATHS) {
            try {
                File keyFile = new File(path);
                if (keyFile.exists()) {
                    System.out.println("Loading JWT key from: " + keyFile.getAbsolutePath());
                    byte[] keyBytes = Files.readAllBytes(keyFile.toPath());
                    return Keys.hmacShaKeyFor(keyBytes);
                }
            } catch (IOException e) {
                System.out.println("Could not load key from: " + path);
                // Continue to the next path
            }
        }
        
        // If we couldn't find a key file, generate a new one
        try {
            System.out.println("Generating new JWT key");
            SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
            
            // Try to save the key to one of the paths
            for (String path : KEY_FILE_PATHS) {
                try {
                    File keyFile = new File(path);
                    File parentDir = keyFile.getParentFile();
                    if (parentDir != null && !parentDir.exists()) {
                        parentDir.mkdirs();
                    }
                    Files.write(keyFile.toPath(), key.getEncoded());
                    System.out.println("Saved JWT key to: " + keyFile.getAbsolutePath());
                    break; // Successfully saved the key
                } catch (IOException e) {
                    System.out.println("Could not save key to: " + path);
                    // Try the next path
                }
            }
            
            return key;
        } catch (Exception e) {
            throw new RuntimeException("Could not generate JWT secret key", e);
        }
    }

    public String generateToken(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Role not found"))
                .getAuthority(); 

        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        System.out.println("Generating token for user: " + username + " with role: " + role);
        
        String token = Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setId(java.util.UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours 
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
                
        System.out.println("Generated token: " + token);
        return token;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY) 
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false; 
        }
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
