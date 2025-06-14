package com.orar.Backend.Orar.dto;

import com.orar.Backend.Orar.dto.OrarDetailsDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrarDetailsDTOTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        OrarDetailsDTO dto = new OrarDetailsDTO(
                "Marti", "302AC", 8, 10,
                "302AC", "Sala 101", "Curs",
                "Structuri de Date", "Popescu Ana", "Saptamanal"
        );

        assertEquals("Marti", dto.getZi());
        assertEquals("302AC", dto.getFormatia());
        assertEquals(8, dto.getOraInceput());
        assertEquals(10, dto.getOraSfarsit());
        assertEquals("302AC", dto.getGrupa());
        assertEquals("Sala 101", dto.getSala());
        assertEquals("Curs", dto.getTipul());
        assertEquals("Structuri de Date", dto.getDisciplina());
        assertEquals("Popescu Ana", dto.getCadruDidactic());
        assertEquals("Saptamanal", dto.getFrecventa());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        OrarDetailsDTO dto = new OrarDetailsDTO();

        dto.setZi("Joi");
        dto.setFormatia("304B");
        dto.setOraInceput(14);
        dto.setOraSfarsit(16);
        dto.setGrupa("304B");
        dto.setSala("Sala 202");
        dto.setTipul("Laborator");
        dto.setDisciplina("Baze de Date");
        dto.setCadruDidactic("Ionescu Mihai");
        dto.setFrecventa("Bilunar");

        assertEquals("Joi", dto.getZi());
        assertEquals("304B", dto.getFormatia());
        assertEquals(14, dto.getOraInceput());
        assertEquals(16, dto.getOraSfarsit());
        assertEquals("304B", dto.getGrupa());
        assertEquals("Sala 202", dto.getSala());
        assertEquals("Laborator", dto.getTipul());
        assertEquals("Baze de Date", dto.getDisciplina());
        assertEquals("Ionescu Mihai", dto.getCadruDidactic());
        assertEquals("Bilunar", dto.getFrecventa());
    }

    @Test
    void testBuilder() {
        OrarDetailsDTO dto = OrarDetailsDTO.builder()
                .zi("Vineri")
                .formatia("305C")
                .oraInceput(12)
                .oraSfarsit(14)
                .grupa("305C")
                .sala("Sala 303")
                .tipul("Seminar")
                .disciplina("Retele de Calculatoare")
                .cadruDidactic("Georgescu Elena")
                .frecventa("Saptamanal")
                .build();

        assertEquals("Vineri", dto.getZi());
        assertEquals("305C", dto.getFormatia());
        assertEquals(12, dto.getOraInceput());
        assertEquals(14, dto.getOraSfarsit());
        assertEquals("305C", dto.getGrupa());
        assertEquals("Sala 303", dto.getSala());
        assertEquals("Seminar", dto.getTipul());
        assertEquals("Retele de Calculatoare", dto.getDisciplina());
        assertEquals("Georgescu Elena", dto.getCadruDidactic());
        assertEquals("Saptamanal", dto.getFrecventa());
    }
}
