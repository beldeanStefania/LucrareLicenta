package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.exception.UserNotFoundException;
import com.orar.Backend.Orar.model.Rol;
import com.orar.Backend.Orar.model.User;
import com.orar.Backend.Orar.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService service;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setUsername("john");
        sampleUser.setPassword("secret");
        sampleUser.setRole(new Rol("ROLE_USER"));
    }

    @Test
    void loadUserByUsername_Success() {
        // Arrange
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(sampleUser));

        // Act
        UserDetails details = service.loadUserByUsername("john");

        // Assert
        assertNotNull(details);
        assertEquals("john", details.getUsername());
        assertEquals("secret", details.getPassword());
        assertTrue(details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        verify(userRepository).findByUsername("john");
    }

    @Test
    void loadUserByUsername_NotFound() {
        // Arrange
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                service.loadUserByUsername("unknown")
        );
        assertTrue(ex.getCause() instanceof UserNotFoundException);
        assertEquals("User not found with username: unknown", ex.getCause().getMessage());
        verify(userRepository).findByUsername("unknown");
    }
}
