package com.orar.Backend.Orar.dto;

import com.orar.Backend.Orar.dto.RepartizareProfDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RepartizareProfDTOTest {

    @Test
    void testRepartizareProfDTOFields() {
        RepartizareProfDTO dto = new RepartizareProfDTO();

        dto.setId(1);
        dto.setTip("Curs");
        dto.setNumeProfesor("Ionescu");
        dto.setPrenumeProfesor("Maria");
        dto.setMaterie("Programare Java");

        assertEquals(1, dto.getId());
        assertEquals("Curs", dto.getTip());
        assertEquals("Ionescu", dto.getNumeProfesor());
        assertEquals("Maria", dto.getPrenumeProfesor());
        assertEquals("Programare Java", dto.getMaterie());
    }
}
