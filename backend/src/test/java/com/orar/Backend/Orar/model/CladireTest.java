package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class CladireTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        Cladire cladire = new Cladire();
        cladire.setId(1);
        cladire.setNume("FSEGA");
        cladire.setAdresa("Str. Teodor Mihali");

        assertEquals(1, cladire.getId());
        assertEquals("FSEGA", cladire.getNume());
        assertEquals("Str. Teodor Mihali", cladire.getAdresa());
    }

    @Test
    void testAllArgsConstructor() {
        Sala sala = new Sala();
        Cladire cladire = new Cladire(2, "UTCN", "Str. Barițiu", Collections.singletonList(sala));

        assertEquals(2, cladire.getId());
        assertEquals("UTCN", cladire.getNume());
        assertEquals("Str. Barițiu", cladire.getAdresa());
        assertEquals(1, cladire.getSala().size());
    }

    @Test
    void testEqualsAndHashCode() {
        Cladire c1 = new Cladire();
        c1.setId(100);

        Cladire c2 = new Cladire();
        c2.setId(100);

        Cladire c3 = new Cladire();
        c3.setId(200);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotEquals(c1, c3);
    }
}
