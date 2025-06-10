package com.orar.Backend.Orar.security;

import com.orar.Backend.Orar.service.UserDetailsServiceImpl;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            // Nu avem token: continuăm ca anon
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        try {
            String username = jwtUtil.extractUsername(token);
            String role     = jwtUtil.extractRole(token);

            if (username != null
                    && SecurityContextHolder.getContext().getAuthentication() == null
                    && jwtUtil.validateToken(token)) {

                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);

                // construieşte autoritatea corectă
                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority(role);
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, List.of(authority));

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            // Dacă validateToken aruncă, ajunge în catch de mai jos

        } catch (JwtException ex) {
            // Token expirat/invalid/etc → răspundem 401 și nu continuăm
            logger.warn("JWT validation failed: {}");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "JWT invalid: " + ex.getMessage());
            return;
        } catch (Exception ex) {
            // Alte erori neașteptate
            logger.error("Unexpected error in JWT filter", ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error processing JWT");
            return;
        }

        // Totul OK → continuăm
        chain.doFilter(request, response);
    }
}
