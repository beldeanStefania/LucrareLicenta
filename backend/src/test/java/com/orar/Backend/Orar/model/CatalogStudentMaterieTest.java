package com.orar.Backend.Orar.model;

import com.orar.Backend.Orar.enums.MaterieStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CatalogStudentMaterieTest {

    @Test
    void testSettersAndGetters() {
        CatalogStudentMaterie entry = new CatalogStudentMaterie();
        Student student = new Student();
        Materie materie = new Materie();

        entry.setId(1);
        entry.setNota(9.75);
        entry.setSemestru(2);
        entry.setStatus(MaterieStatus.FINALIZATA);
        entry.setStudent(student);
        entry.setMaterie(materie);

        assertEquals(1, entry.getId());
        assertEquals(9.75, entry.getNota());
        assertEquals(2, entry.getSemestru());
        assertEquals(MaterieStatus.FINALIZATA, entry.getStatus());
        assertSame(student, entry.getStudent());
        assertSame(materie, entry.getMaterie());
    }

    @Test
    void testEqualsAndHashCode() {
        CatalogStudentMaterie c1 = new CatalogStudentMaterie();
        c1.setId(100);

        CatalogStudentMaterie c2 = new CatalogStudentMaterie();
        c2.setId(100);

        CatalogStudentMaterie c3 = new CatalogStudentMaterie();
        c3.setId(200);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotEquals(c1, c3);
    }
}
