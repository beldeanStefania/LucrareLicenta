package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MaterieTest {

    @Test
    void testEqualsAndHashCode() {
        Materie m1 = new Materie();
        m1.setId(1);
        m1.setCod("M001");

        Materie m2 = new Materie();
        m2.setId(1);
        m2.setCod("M002"); // cod diferit, dar id egal

        Materie m3 = new Materie();
        m3.setId(2);
        m3.setCod("M001");

        // Compară după id → m1 și m2 sunt egale
        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());

        // id diferit → nu sunt egale
        assertNotEquals(m1, m3);
        assertNotEquals(m1.hashCode(), m3.hashCode());
    }
}
