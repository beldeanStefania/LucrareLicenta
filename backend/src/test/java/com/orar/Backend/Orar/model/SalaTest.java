package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SalaTest {

    private Sala sala;
    private Cladire cladire;
    private List<Orar> orar;

    @BeforeEach
    void setUp() {
        sala = new Sala();
        sala.setId(1);
        sala.setNume("Sala 101");
        sala.setCapacitate(50);

        cladire = Mockito.mock(Cladire.class);
        sala.setCladire(cladire);

        orar = new ArrayList<>();
        sala.setOrar(orar);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, sala.getId());
        assertEquals("Sala 101", sala.getNume());
        assertEquals(50, sala.getCapacitate());
        assertEquals(cladire, sala.getCladire());
        assertEquals(orar, sala.getOrar());
    }

    @Test
    void testSetNume() {
        sala.setNume("Sala 202");
        assertEquals("Sala 202", sala.getNume());
    }

    @Test
    void testSetCapacitate() {
        sala.setCapacitate(100);
        assertEquals(100, sala.getCapacitate());
    }

    @Test
    void testSetCladire() {
        Cladire newCladire = Mockito.mock(Cladire.class);
        sala.setCladire(newCladire);
        assertEquals(newCladire, sala.getCladire());
    }

    @Test
    void testSetOrar() {
        List<Orar> newOrar = new ArrayList<>();
        sala.setOrar(newOrar);
        assertEquals(newOrar, sala.getOrar());
    }
}