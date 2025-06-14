package com.orar.Backend.Orar.dto;

import com.orar.Backend.Orar.dto.OrarDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrarDTOTest {

    @Test
    void testOrarDTOFields() {
        OrarDTO orar = new OrarDTO();

        orar.setGrupa("302AC");
        orar.setSemigrupa("A");
        orar.setOraInceput(10);
        orar.setOraSfarsit(12);
        orar.setZi("Luni");
        orar.setRepartizareProfId(5);
        orar.setSalaId(101);
        orar.setTip("Curs");
        orar.setMaterie("Algoritmi");
        orar.setProfesorId(3);
        orar.setFrecventa("Saptamanal");

        assertEquals("302AC", orar.getGrupa());
        assertEquals("A", orar.getSemigrupa());
        assertEquals(10, orar.getOraInceput());
        assertEquals(12, orar.getOraSfarsit());
        assertEquals("Luni", orar.getZi());
        assertEquals(5, orar.getRepartizareProfId());
        assertEquals(101, orar.getSalaId());
        assertEquals("Curs", orar.getTip());
        assertEquals("Algoritmi", orar.getMaterie());
        assertEquals(3, orar.getProfesorId());
        assertEquals("Saptamanal", orar.getFrecventa());
    }
}
