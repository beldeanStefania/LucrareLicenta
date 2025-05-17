//package com.orar.Backend.Orar.model;
//
//import static org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//class CatalogStudentMaterieTest {
//
//    private CatalogStudentMaterie catalogStudentMaterie;
//    private Student student;
//    private Materie materie;
//
//    @BeforeEach
//    void setUp() {
//        catalogStudentMaterie = new CatalogStudentMaterie();
//        catalogStudentMaterie.setId(1);
//        catalogStudentMaterie.setNota(9.5);
//        catalogStudentMaterie.setSemestru(1);
//
//        student = Mockito.mock(Student.class);
//        catalogStudentMaterie.setStudent(student);
//
//        materie = Mockito.mock(Materie.class);
//        catalogStudentMaterie.setMaterie(materie);
//    }
//
//    @Test
//    void testGettersAndSetters() {
//        assertEquals(1, catalogStudentMaterie.getId());
//        assertEquals(9.5, catalogStudentMaterie.getNota());
//        assertEquals(1, catalogStudentMaterie.getSemestru());
//        assertEquals(student, catalogStudentMaterie.getStudent());
//        assertEquals(materie, catalogStudentMaterie.getMaterie());
//    }
//
//    @Test
//    void testSetNota() {
//        catalogStudentMaterie.setNota(8.0);
//        assertEquals(8.0, catalogStudentMaterie.getNota());
//    }
//
//    @Test
//    void testSetSemestru() {
//        catalogStudentMaterie.setSemestru(2);
//        assertEquals(2, catalogStudentMaterie.getSemestru());
//    }
//
//    @Test
//    void testSetStudent() {
//        Student newStudent = Mockito.mock(Student.class);
//        catalogStudentMaterie.setStudent(newStudent);
//        assertEquals(newStudent, catalogStudentMaterie.getStudent());
//    }
//
//    @Test
//    void testSetMaterie() {
//        Materie newMaterie = Mockito.mock(Materie.class);
//        catalogStudentMaterie.setMaterie(newMaterie);
//        assertEquals(newMaterie, catalogStudentMaterie.getMaterie());
//    }
//}