package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SalaTest {

    private Sala sala;
    private Cladire cladire;
    private Orar orar1;
    private Orar orar2;

    @BeforeEach
    void setUp() {
        sala = new Sala();
        sala.setId(1);
        sala.setNume("Sala 101");
        sala.setCapacitate(30);

        cladire = mock(Cladire.class);
        sala.setCladire(cladire);

        orar1 = mock(Orar.class);
        orar2 = mock(Orar.class);

        sala.setOrar(List.of(orar1, orar2));
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, sala.getId());
        assertEquals("Sala 101", sala.getNume());
        assertEquals(30, sala.getCapacitate());
        assertEquals(cladire, sala.getCladire());

        assertNotNull(sala.getOrar());
        assertEquals(2, sala.getOrar().size());
        assertTrue(sala.getOrar().contains(orar1));
    }

    @Test
    void testUpdateSalaFields() {
        sala.setNume("Sala 305");
        sala.setCapacitate(45);

        assertEquals("Sala 305", sala.getNume());
        assertEquals(45, sala.getCapacitate());
    }

    @Test
    void testEqualsAndHashCode() {
        Sala s1 = new Sala();
        s1.setId(10);

        Sala s2 = new Sala();
        s2.setId(10);

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testNullFields() {
        Sala s = new Sala();

        assertNull(s.getId());
        assertNull(s.getNume());
        assertNull(s.getCapacitate());
        assertNull(s.getCladire());
        assertNull(s.getOrar());
    }
}
