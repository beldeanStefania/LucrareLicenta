package com.orar.Backend.Orar.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.orar.Backend.Orar.dto.CladireDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CladireDTOTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        CladireDTO cladire = new CladireDTO("Clădire A", "Str. Universității 1");

        assertEquals("Clădire A", cladire.getNume());
        assertEquals("Str. Universității 1", cladire.getAdresa());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        CladireDTO cladire = new CladireDTO();

        cladire.setNume("Clădire B");
        cladire.setAdresa("Bd. Eroilor 25");

        assertEquals("Clădire B", cladire.getNume());
        assertEquals("Bd. Eroilor 25", cladire.getAdresa());
    }
}
