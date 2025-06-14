package com.orar.Backend.Orar.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentGradeDTOTest {

    @Test
    void testNoArgsConstructorAndSettersGetters() {
        // Using no-args constructor, fields should be null
        StudentGradeDTO dto = new StudentGradeDTO();
        assertNull(dto.getDisciplina(), "Disciplina should be null after no-args constructor");
        assertNull(dto.getNota(), "Nota should be null after no-args constructor");

        // Test setters
        dto.setDisciplina("Matematica");
        dto.setNota(9.5);

        // Test getters
        assertEquals("Matematica", dto.getDisciplina(), "Getter for disciplina returned unexpected value");
        assertEquals(9.5, dto.getNota(), "Getter for nota returned unexpected value");
    }

    @Test
    void testAllArgsConstructor() {
        // Using all-args constructor
        StudentGradeDTO dto = new StudentGradeDTO("Fizica", 8.0);

        // Fields should be set correctly
        assertEquals("Fizica", dto.getDisciplina(), "Disciplina should match the constructor argument");
        assertEquals(8.0, dto.getNota(), "Nota should match the constructor argument");
    }
}
