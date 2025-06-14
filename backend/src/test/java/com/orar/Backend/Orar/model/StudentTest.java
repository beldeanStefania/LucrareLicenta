package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentTest {

    private Student student;
    private User mockUser;
    private Specializare mockSpecializare;
    private CatalogStudentMaterie catalog1;
    private CatalogStudentMaterie catalog2;

    @BeforeEach
    void setUp() {
        mockUser = mock(User.class);
        mockSpecializare = mock(Specializare.class);

        catalog1 = mock(CatalogStudentMaterie.class);
        catalog2 = mock(CatalogStudentMaterie.class);

        student = new Student();
        student.setId(1);
        student.setCod("S123");
        student.setNume("Popescu");
        student.setPrenume("Ion");
        student.setAn(2);
        student.setGrupa("214A");
        student.setUser(mockUser);
        student.setSpecializare(mockSpecializare);
        student.setCatalogStudentMaterie(List.of(catalog1, catalog2));
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, student.getId());
        assertEquals("S123", student.getCod());
        assertEquals("Popescu", student.getNume());
        assertEquals("Ion", student.getPrenume());
        assertEquals(2, student.getAn());
        assertEquals("214A", student.getGrupa());
        assertEquals(mockUser, student.getUser());
        assertEquals(mockSpecializare, student.getSpecializare());
        assertEquals(2, student.getCatalogStudentMaterie().size());
        assertTrue(student.getCatalogStudentMaterie().contains(catalog1));
    }

    @Test
    void testChangeFields() {
        student.setCod("S456");
        student.setNume("Ionescu");
        student.setPrenume("Andrei");
        student.setAn(3);
        student.setGrupa("215B");

        assertEquals("S456", student.getCod());
        assertEquals("Ionescu", student.getNume());
        assertEquals("Andrei", student.getPrenume());
        assertEquals(3, student.getAn());
        assertEquals("215B", student.getGrupa());
    }

    @Test
    void testEqualsAndHashCode() {
        Student s1 = new Student();
        s1.setId(10);

        Student s2 = new Student();
        s2.setId(10);

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testEmptyCatalogAndNullRelations() {
        Student s = new Student();
        s.setCatalogStudentMaterie(null);
        s.setUser(null);
        s.setSpecializare(null);

        assertNull(s.getUser());
        assertNull(s.getSpecializare());
        assertNull(s.getCatalogStudentMaterie());
    }
}
