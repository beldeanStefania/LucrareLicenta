package com.orar.Backend.Orar.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

class OrarTest {

    private Orar orar;
    private Sala sala;
    private RepartizareProf repartizareProf;

    @BeforeEach
    void setUp() {
        orar = new Orar();
        orar.setId(1);
        orar.setFormatia("Grupa A1");
        orar.setOraInceput(10);
        orar.setOraSfarsit(12);
        orar.setGrupa("A1");
        orar.setZi("Luni");
        orar.setFrecventa("Saptamanal");

        sala = Mockito.mock(Sala.class);
        orar.setSala(sala);

        repartizareProf = Mockito.mock(RepartizareProf.class);
        orar.setRepartizareProf(repartizareProf);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, orar.getId());
        assertEquals("Grupa A1", orar.getFormatia());
        assertEquals(10, orar.getOraInceput());
        assertEquals(12, orar.getOraSfarsit());
        assertEquals("A1", orar.getGrupa());
        assertEquals("Luni", orar.getZi());
        assertEquals("Saptamanal", orar.getFrecventa());
        assertEquals(sala, orar.getSala());
        assertEquals(repartizareProf, orar.getRepartizareProf());
    }
}