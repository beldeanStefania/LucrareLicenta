package com.orar.Backend.Orar.config;

import com.orar.Backend.Orar.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults()) // Enable CORS
                .csrf((csrf) -> csrf.disable()) // Disable CSRF
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**",
                                "/swagger-ui.html", "/webjars/**", "/v3/api-docs/swagger-config").permitAll()
                        .requestMatchers("/api/student/getAllStudents").hasRole("ADMIN")
                        .requestMatchers("/api/repartizareProf/**").hasAnyRole("ADMIN", "PROFESOR")
                        .requestMatchers("/api/student/**").hasAnyRole("ADMIN", "STUDENT", "PROFESOR")
                        .requestMatchers("/api/auth/userInfo").hasAnyRole("ADMIN", "STUDENT", "PROFESOR")
                        .requestMatchers("/api/orare/getAll/{grupa}").hasAnyRole("STUDENT")
                        .requestMatchers("/api/orare/getAllProfesor/{profesorId}").hasAnyRole("PROFESOR")
                        .requestMatchers("/getNote/{studentCod}").hasRole("STUDENT")
                        .requestMatchers("/api/orare/**").hasAnyRole("ADMIN", "PROFESOR")
                        .requestMatchers("/api/profesor/**").hasAnyRole("ADMIN", "PROFESOR")
                        .requestMatchers("/api/catalogStudentMaterie/**").permitAll()
                        .requestMatchers("/api/sala/**").hasAnyRole("ADMIN", "PROFESOR")
                        .requestMatchers("/api/cladire/**").hasAnyRole("ADMIN", "PROFESOR")
                        .requestMatchers("/api/materie/**").hasAnyRole("ADMIN", "PROFESOR", "STUDENT")
                        .requestMatchers("/api/studentContract/**").hasRole("STUDENT")
                        .requestMatchers("/api/curriculum/**").hasAnyRole("STUDENT")
                        .requestMatchers("/api/specializare/**").hasAnyRole("ADMIN", "PROFESOR")
                        .requestMatchers("/api/todo/**").hasAnyRole("STUDENT", "PROFESOR", "ADMIN")
                        .requestMatchers("/api/test-email/**").permitAll()
                        .requestMatchers("/api/chat/**").hasAnyRole("STUDENT", "PROFESOR", "ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling((exceptions) -> exceptions
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            System.out.println("Access Denied: " + accessDeniedException.getMessage());
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                        })
                )
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
