package com.orar.Backend.Orar.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StudentDTOTest {

    @Test
    void testStudentDTOFields() {
        StudentDTO student = new StudentDTO();

        student.setCod("123");
        student.setNume("Popescu");
        student.setPrenume("Ion");
        student.setGrupa("302AC");
        student.setAn(2);
        student.setUsername("ipopescu");
        student.setPassword("securePass");
        student.setSpecializare("Informatica");
        student.setEmail("ipopescu@example.com");

        assertEquals("123", student.getCod());
        assertEquals("Popescu", student.getNume());
        assertEquals("Ion", student.getPrenume());
        assertEquals("302AC", student.getGrupa());
        assertEquals(2, student.getAn());
        assertEquals("ipopescu", student.getUsername());
        assertEquals("securePass", student.getPassword());
        assertEquals("Informatica", student.getSpecializare());
        assertEquals("ipopescu@example.com", student.getEmail());
    }
}
