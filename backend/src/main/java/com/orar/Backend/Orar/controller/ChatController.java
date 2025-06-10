package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;
// …

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public Map<String,String> chatGeneric(@RequestBody Map<String,String> payload, Principal principal) {
        String message = payload.get("message");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(r -> r.equals("ROLE_ADMIN"));
        boolean isProf  = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(r -> r.equals("ROLE_PROFESOR"));

        String reply;
        if (isAdmin) {
            String username = principal.getName();
            reply = chatService.chatForAdmin(message, username);
        } else if (isProf) {
            String username = principal.getName();
            reply = chatService.chatForProfessor(message, username);
        } else {
            String username = principal.getName();
            reply = chatService.chat(message, username);
        }
        return Map.of("reply", reply);
    }

}
