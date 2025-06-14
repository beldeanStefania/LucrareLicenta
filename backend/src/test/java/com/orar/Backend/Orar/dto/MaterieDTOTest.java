package com.orar.Backend.Orar.dto;

import com.orar.Backend.Orar.dto.MaterieDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MaterieDTOTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        MaterieDTO materie = new MaterieDTO("Programare Java", 2, "INF204", 5);

        assertEquals("Programare Java", materie.getNume());
        assertEquals(2, materie.getSemestru());
        assertEquals("INF204", materie.getCod());
        assertEquals(5, materie.getCredite());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        MaterieDTO materie = new MaterieDTO();

        materie.setNume("Algoritmi");
        materie.setSemestru(1);
        materie.setCod("INF101");
        materie.setCredite(6);

        assertEquals("Algoritmi", materie.getNume());
        assertEquals(1, materie.getSemestru());
        assertEquals("INF101", materie.getCod());
        assertEquals(6, materie.getCredite());
    }
}
