package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class MateriiOptionaleTest {

    private MateriiOptionale optionale;

    @BeforeEach
    void setUp() {
        optionale = new MateriiOptionale();
        optionale.setId(1);
        optionale.setNume("AI Avansat");
        optionale.setCurriculumEntries(Collections.emptyList());
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, optionale.getId());
        assertEquals("AI Avansat", optionale.getNume());
        assertNotNull(optionale.getCurriculumEntries());
        assertTrue(optionale.getCurriculumEntries().isEmpty());
    }

    @Test
    void testSetCurriculumEntries() {
        CurriculumEntry entry = new CurriculumEntry();
        optionale.setCurriculumEntries(Collections.singletonList(entry));
        assertEquals(1, optionale.getCurriculumEntries().size());
    }

    @Test
    void testEqualsAndHashCode() {
        MateriiOptionale m1 = new MateriiOptionale();
        m1.setId(1);

        MateriiOptionale m2 = new MateriiOptionale();
        m2.setId(1);

        MateriiOptionale m3 = new MateriiOptionale();
        m3.setId(2);

        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());

        assertNotEquals(m1, m3);
    }
}
