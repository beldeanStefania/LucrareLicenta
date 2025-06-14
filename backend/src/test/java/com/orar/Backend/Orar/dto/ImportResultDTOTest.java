package com.orar.Backend.Orar.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.orar.Backend.Orar.dto.ImportResultDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ImportResultDTOTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        ImportResultDTO result = new ImportResultDTO(3, true, "Import reușit");

        assertEquals(3, result.getRow());
        assertTrue(result.isSuccess());
        assertEquals("Import reușit", result.getMessage());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        ImportResultDTO result = new ImportResultDTO();

        result.setRow(5);
        result.setSuccess(false);
        result.setMessage("Eroare la linia 5");

        assertEquals(5, result.getRow());
        assertFalse(result.isSuccess());
        assertEquals("Eroare la linia 5", result.getMessage());
    }
}
