package com.orar.Backend.Orar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orar.Backend.Orar.dto.LoginDTO;
import com.orar.Backend.Orar.model.Rol;
import com.orar.Backend.Orar.model.User;
import com.orar.Backend.Orar.security.JwtUtil;
import com.orar.Backend.Orar.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtUtil jwtUtil;
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private LoginController loginController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private LoginDTO loginDTO;
    private User user;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
        objectMapper = new ObjectMapper();

        loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("testpass");

        Rol role = new Rol();
        role.setName("STUDENT");

        user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedpass");
        user.setRole(role);
    }

    @Test
    void login_ShouldReturnTokenAndRole() throws Exception {
        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
        when(jwtUtil.generateToken("testuser")).thenReturn("testtoken");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("testtoken"))
                .andExpect(jsonPath("$.role").value("STUDENT"));
    }

    @Test
    void login_WithAuthenticationException_ShouldReturnError() throws Exception {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("Bad credentials"));


        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void logout_ShouldInvalidateSession() throws Exception {
        mockMvc.perform(post("/api/auth/api/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("Logged out successfully."));
    }

    @Test
    void changePassword_ShouldUpdatePassword() throws Exception {
        Map<String, String> payload = Map.of(
                "username", "testuser",
                "oldPassword", "testpass",
                "newPassword", "newpass"
        );

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("testpass", "encodedpass")).thenReturn(true);
        when(passwordEncoder.encode("newpass")).thenReturn("encodedNewPass");

        mockMvc.perform(post("/api/auth/changePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password updated successfully"));

        verify(userRepository).save(any(User.class));
    }

    @Test
    void changePassword_WithWrongOldPassword_ShouldReturnForbidden() throws Exception {
        Map<String, String> payload = Map.of(
                "username", "testuser",
                "oldPassword", "wrongpass",
                "newPassword", "newpass"
        );

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpass", "encodedpass")).thenReturn(false);

        mockMvc.perform(post("/api/auth/changePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Old password is incorrect"));
    }

    @Test
    void getUserInfo_WithUserDetails_ShouldReturnInfo() throws Exception {
        Rol role = new Rol();
        role.setName("STUDENT");
        user.setRole(role);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("testuser")
                .password("pass")
                .roles("STUDENT")
                .build();

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/auth/userInfo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.role").value("STUDENT"));
    }
}