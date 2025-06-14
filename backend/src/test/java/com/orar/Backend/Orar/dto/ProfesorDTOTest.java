package com.orar.Backend.Orar.dto;

import com.orar.Backend.Orar.dto.ProfesorDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProfesorDTOTest {

    @Test
    void testProfesorDTOFields() {
        ProfesorDTO profesor = new ProfesorDTO();

        profesor.setNume("Pop");
        profesor.setPrenume("Ana");
        profesor.setUsername("apop");
        profesor.setPassword("parola123");
        profesor.setEmail("apop@universitate.ro");

        assertEquals("Pop", profesor.getNume());
        assertEquals("Ana", profesor.getPrenume());
        assertEquals("apop", profesor.getUsername());
        assertEquals("parola123", profesor.getPassword());
        assertEquals("apop@universitate.ro", profesor.getEmail());
    }
}
