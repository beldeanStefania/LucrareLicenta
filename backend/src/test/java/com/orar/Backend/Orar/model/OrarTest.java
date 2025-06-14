package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrarTest {

    private Orar orar;
    private Sala sala;
    private RepartizareProf repartizareProf;

    @BeforeEach
    void setUp() {
        sala = mock(Sala.class);
        repartizareProf = mock(RepartizareProf.class);

        orar = new Orar();
        orar.setId(1);
        orar.setSala(sala);
        orar.setRepartizareProf(repartizareProf);
        orar.setFormatia("FAF2022");
        orar.setOraInceput(10);
        orar.setOraSfarsit(12);
        orar.setGrupa("231");
        orar.setZi("Luni");
        orar.setFrecventa("saptamanal");
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, orar.getId());
        assertEquals(sala, orar.getSala());
        assertEquals(repartizareProf, orar.getRepartizareProf());
        assertEquals("FAF2022", orar.getFormatia());
        assertEquals(10, orar.getOraInceput());
        assertEquals(12, orar.getOraSfarsit());
        assertEquals("231", orar.getGrupa());
        assertEquals("Luni", orar.getZi());
        assertEquals("saptamanal", orar.getFrecventa());
    }

    @Test
    void testSetSala() {
        Sala newSala = mock(Sala.class);
        orar.setSala(newSala);
        assertEquals(newSala, orar.getSala());
    }

    @Test
    void testSetRepartizareProf() {
        RepartizareProf newProf = mock(RepartizareProf.class);
        orar.setRepartizareProf(newProf);
        assertEquals(newProf, orar.getRepartizareProf());
    }

    @Test
    void testEqualsAndHashCode() {
        Orar o1 = new Orar();
        o1.setId(10);
        Orar o2 = new Orar();
        o2.setId(10);

        assertEquals(o1, o2);
        assertEquals(o1.hashCode(), o2.hashCode());
    }
}
