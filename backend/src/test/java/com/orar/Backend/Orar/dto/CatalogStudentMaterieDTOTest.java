package com.orar.Backend.Orar.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.orar.Backend.Orar.dto.CatalogStudentMaterieDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CatalogStudentMaterieDTOTest {

    @Test
    void testGetterAndSetter() {
        CatalogStudentMaterieDTO dto = new CatalogStudentMaterieDTO();

        dto.setNota(9.5);
        dto.setSemestru(2);
        dto.setStudentCod("S12345");
        dto.setCodMaterie("INF301");
        dto.setNumeMaterie("Sisteme Distribuite");
        dto.setCredite(6);

        assertEquals(9.5, dto.getNota());
        assertEquals(2, dto.getSemestru());
        assertEquals("S12345", dto.getStudentCod());
        assertEquals("INF301", dto.getCodMaterie());
        assertEquals("Sisteme Distribuite", dto.getNumeMaterie());
        assertEquals(6, dto.getCredite());
    }
}
