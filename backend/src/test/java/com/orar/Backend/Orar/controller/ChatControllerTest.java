package com.orar.Backend.Orar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orar.Backend.Orar.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

    @Mock
    private ChatService chatService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Principal principal;

    @InjectMocks
    private ChatController chatController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(chatController).build();
        objectMapper = new ObjectMapper();

        // Setup SecurityContextHolder mock
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void chatGeneric_WithAdminRole_ShouldCallChatForAdmin() throws Exception {
        // Given
        String username = "admin";
        String message = "Hello admin";
        String expectedReply = "Admin response";

        Map<String, String> payload = Map.of("message", message);

        when(principal.getName()).thenReturn(username);
        when(chatService.chatForAdmin(message, username)).thenReturn(expectedReply);

        // Setup admin authorities
        List<GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_USER")
        );
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        // When & Then
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply").value(expectedReply));

        verify(chatService, times(1)).chatForAdmin(message, username);
        verify(chatService, never()).chatForProfessor(anyString(), anyString());
        verify(chatService, never()).chat(anyString(), anyString());
    }

    @Test
    void chatGeneric_WithProfesorRole_ShouldCallChatForProfessor() throws Exception {
        // Given
        String username = "profesor";
        String message = "Hello profesor";
        String expectedReply = "Professor response";

        Map<String, String> payload = Map.of("message", message);

        when(principal.getName()).thenReturn(username);
        when(chatService.chatForProfessor(message, username)).thenReturn(expectedReply);

        // Setup profesor authorities (not admin)
        List<GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_PROFESOR"),
                new SimpleGrantedAuthority("ROLE_USER")
        );
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        // When & Then
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply").value(expectedReply));

        verify(chatService, times(1)).chatForProfessor(message, username);
        verify(chatService, never()).chatForAdmin(anyString(), anyString());
        verify(chatService, never()).chat(anyString(), anyString());
    }

    @Test
    void chatGeneric_WithUserRole_ShouldCallRegularChat() throws Exception {
        // Given
        String username = "student";
        String message = "Hello student";
        String expectedReply = "Student response";

        Map<String, String> payload = Map.of("message", message);

        when(principal.getName()).thenReturn(username);
        when(chatService.chat(message, username)).thenReturn(expectedReply);

        // Setup regular user authorities (no admin or profesor)
        List<GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER")
        );
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        // When & Then
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply").value(expectedReply));

        verify(chatService, times(1)).chat(message, username);
        verify(chatService, never()).chatForAdmin(anyString(), anyString());
        verify(chatService, never()).chatForProfessor(anyString(), anyString());
    }

    @Test
    void chatGeneric_WithNullPrincipal_ShouldReturnUnauthorized() throws Exception {
        // Given
        String message = "Hello";
        Map<String, String> payload = Map.of("message", message);

        // When & Then
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isUnauthorized());

        verify(chatService, never()).chat(anyString(), anyString());
        verify(chatService, never()).chatForAdmin(anyString(), anyString());
        verify(chatService, never()).chatForProfessor(anyString(), anyString());
    }

    @Test
    void chatGeneric_WithEmptyMessage_ShouldPassToService() throws Exception {
        // Given
        String username = "user";
        String message = "";
        String expectedReply = "Empty message response";

        Map<String, String> payload = Map.of("message", message);

        when(principal.getName()).thenReturn(username);
        when(chatService.chat(message, username)).thenReturn(expectedReply);

        List<GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER")
        );
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        // When & Then
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply").value(expectedReply));

        verify(chatService, times(1)).chat(message, username);
    }

    @Test
    void chatGeneric_WithNullMessage_ShouldPassNullToService() throws Exception {
        // Given
        String username = "user";
        String expectedReply = "Null message response";

        Map<String, String> payload = new HashMap<>();
        payload.put("message", null);

        when(principal.getName()).thenReturn(username);
        when(chatService.chat(null, username)).thenReturn(expectedReply);

        List<GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER")
        );
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        // When & Then
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply").value(expectedReply));

        verify(chatService, times(1)).chat(null, username);
    }

    @Test
    void chatGeneric_WithMissingMessageKey_ShouldPassNullToService() throws Exception {
        // Given
        String username = "user";
        String expectedReply = "No message key response";

        Map<String, String> payload = Map.of("other", "value");

        when(principal.getName()).thenReturn(username);
        when(chatService.chat(null, username)).thenReturn(expectedReply);

        List<GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER")
        );
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        // When & Then
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply").value(expectedReply));

        verify(chatService, times(1)).chat(null, username);
    }

    @Test
    void chatGeneric_WithMultipleRoles_AdminShouldTakePrecedence() throws Exception {
        // Given
        String username = "superuser";
        String message = "Hello superuser";
        String expectedReply = "Admin response";

        Map<String, String> payload = Map.of("message", message);

        when(principal.getName()).thenReturn(username);
        when(chatService.chatForAdmin(message, username)).thenReturn(expectedReply);

        // Setup multiple authorities with both admin and profesor
        List<GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_PROFESOR"),
                new SimpleGrantedAuthority("ROLE_USER")
        );
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        // When & Then
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply").value(expectedReply));

        // Should call admin method, not profesor
        verify(chatService, times(1)).chatForAdmin(message, username);
        verify(chatService, never()).chatForProfessor(anyString(), anyString());
        verify(chatService, never()).chat(anyString(), anyString());
    }

    @Test
    void chatGeneric_WithEmptyPayload_ShouldWork() throws Exception {
        // Given
        String username = "user";
        String expectedReply = "Empty payload response";

        Map<String, String> payload = new HashMap<>();

        when(principal.getName()).thenReturn(username);
        when(chatService.chat(null, username)).thenReturn(expectedReply);

        List<GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER")
        );
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        // When & Then
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply").value(expectedReply));

        verify(chatService, times(1)).chat(null, username);
    }

    @Test
    void chatGeneric_WithLongMessage_ShouldWork() throws Exception {
        // Given
        String username = "user";
        String longMessage = "A".repeat(1000);
        String expectedReply = "Long message response";

        Map<String, String> payload = Map.of("message", longMessage);

        when(principal.getName()).thenReturn(username);
        when(chatService.chat(longMessage, username)).thenReturn(expectedReply);

        List<GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER")
        );
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        // When & Then
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply").value(expectedReply));

        verify(chatService, times(1)).chat(longMessage, username);
    }

    @Test
    void chatGeneric_WithSpecialCharactersInMessage_ShouldWork() throws Exception {
        // Given
        String username = "user";
        String message = "Hello! @#$%^&*()_+ 🚀 中文 ñáéíóú";
        String expectedReply = "Special chars response";

        Map<String, String> payload = Map.of("message", message);

        when(principal.getName()).thenReturn(username);
        when(chatService.chat(message, username)).thenReturn(expectedReply);

        List<GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER")
        );
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        // When & Then
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply").value(expectedReply));

        verify(chatService, times(1)).chat(message, username);
    }
}