package com.orar.Backend.Orar.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

class CladireTest {

    private Cladire cladire;
    private List<Sala> sali;

    @BeforeEach
    void setUp() {
        cladire = new Cladire();
        cladire.setId(1);
        cladire.setNume("Cladirea A");
        cladire.setAdresa("Strada Universitatii 10");

        sali = new ArrayList<>();
        cladire.setSala(sali);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, cladire.getId());
        assertEquals("Cladirea A", cladire.getNume());
        assertEquals("Strada Universitatii 10", cladire.getAdresa());
        assertEquals(sali, cladire.getSala());
    }

    @Test
    void testSetNume() {
        cladire.setNume("Cladirea B");
        assertEquals("Cladirea B", cladire.getNume());
    }

    @Test
    void testSetAdresa() {
        cladire.setAdresa("Bulevardul Central 20");
        assertEquals("Bulevardul Central 20", cladire.getAdresa());
    }

    @Test
    void testSetSala() {
        List<Sala> newSali = new ArrayList<>();
        cladire.setSala(newSali);
        assertEquals(newSali, cladire.getSala());
    }
}
