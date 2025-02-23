package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import java.util.List;
import java.util.ArrayList;

class StudentTest {

    private Student student;
    private User user;
    private List<CatalogStudentMaterie> catalog;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1);
        student.setCod("S12345");
        student.setNume("Popescu");
        student.setPrenume("Ion");
        student.setAn(2);
        student.setGrupa("A3");

        user = Mockito.mock(User.class);
        student.setUser(user);

        catalog = new ArrayList<>();
        student.setCatalogStudentMaterie(catalog);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, student.getId());
        assertEquals("S12345", student.getCod());
        assertEquals("Popescu", student.getNume());
        assertEquals("Ion", student.getPrenume());
        assertEquals(2, student.getAn());
        assertEquals("A3", student.getGrupa());
        assertEquals(user, student.getUser());
        assertEquals(catalog, student.getCatalogStudentMaterie());
    }

    @Test
    void testSetCod() {
        student.setCod("S67890");
        assertEquals("S67890", student.getCod());
    }

    @Test
    void testSetNume() {
        student.setNume("Ionescu");
        assertEquals("Ionescu", student.getNume());
    }

    @Test
    void testSetPrenume() {
        student.setPrenume("Mihai");
        assertEquals("Mihai", student.getPrenume());
    }

    @Test
    void testSetAn() {
        student.setAn(3);
        assertEquals(3, student.getAn());
    }

    @Test
    void testSetGrupa() {
        student.setGrupa("B4");
        assertEquals("B4", student.getGrupa());
    }

    @Test
    void testSetUser() {
        User newUser = Mockito.mock(User.class);
        student.setUser(newUser);
        assertEquals(newUser, student.getUser());
    }

    @Test
    void testSetCatalogStudentMaterie() {
        List<CatalogStudentMaterie> newCatalog = new ArrayList<>();
        student.setCatalogStudentMaterie(newCatalog);
        assertEquals(newCatalog, student.getCatalogStudentMaterie());
    }
}
