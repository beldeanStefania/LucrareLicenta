package com.orar.Backend.Orar.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.orar.Backend.Orar.dto.LoginDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginDTOTest {

    @Test
    void testLoginDTOFields() {
        LoginDTO login = new LoginDTO();

        login.setUsername("student01");
        login.setPassword("parolaSigura");

        assertEquals("student01", login.getUsername());
        assertEquals("parolaSigura", login.getPassword());
    }
}
