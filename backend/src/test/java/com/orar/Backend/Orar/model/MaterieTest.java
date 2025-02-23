package com.orar.Backend.Orar.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

class MaterieTest {

    private Materie materie;
    private List<CatalogStudentMaterie> catalogStudentMaterie;
    private List<RepartizareProf> repartizareProfs;

    @BeforeEach
    void setUp() {
        materie = new Materie();
        materie.setId(1);
        materie.setNume("Matematica");
        materie.setSemestru(1);
        materie.setCod("MATH101");

        catalogStudentMaterie = new ArrayList<>();
        materie.setCatalogStudentMaterie(catalogStudentMaterie);

        repartizareProfs = new ArrayList<>();
        materie.setRepartizareProfs(repartizareProfs);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, materie.getId());
        assertEquals("Matematica", materie.getNume());
        assertEquals(1, materie.getSemestru());
        assertEquals("MATH101", materie.getCod());
        assertEquals(catalogStudentMaterie, materie.getCatalogStudentMaterie());
        assertEquals(repartizareProfs, materie.getRepartizareProfs());
    }
}
