package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpecializareTest {

    private Specializare specializare;
    private CurriculumEntry entry1;
    private CurriculumEntry entry2;
    private Student student1;
    private Student student2;

    @BeforeEach
    void setUp() {
        specializare = new Specializare();
        specializare.setId(100);
        specializare.setSpecializare("Informatica");

        entry1 = mock(CurriculumEntry.class);
        entry2 = mock(CurriculumEntry.class);

        student1 = mock(Student.class);
        student2 = mock(Student.class);

        specializare.setCurriculum(List.of(entry1, entry2));
        specializare.setStudenti(List.of(student1, student2));
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(100, specializare.getId());
        assertEquals("Informatica", specializare.getSpecializare());
        assertEquals(2, specializare.getCurriculum().size());
        assertTrue(specializare.getCurriculum().contains(entry1));
        assertEquals(2, specializare.getStudenti().size());
        assertTrue(specializare.getStudenti().contains(student2));
    }

    @Test
    void testChangeSpecializareName() {
        specializare.setSpecializare("Calculatoare");
        assertEquals("Calculatoare", specializare.getSpecializare());
    }

    @Test
    void testEqualsAndHashCode() {
        Specializare s1 = new Specializare();
        s1.setId(5);

        Specializare s2 = new Specializare();
        s2.setId(5);

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testNullListsAndDefaults() {
        Specializare s = new Specializare();
        assertNull(s.getCurriculum());
        assertNull(s.getStudenti());
        assertNull(s.getSpecializare());
    }
}
