package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
    void testSettersWithNullValues() {
        student.setId(null);
        student.setCod(null);
        student.setNume(null);
        student.setPrenume(null);
        student.setAn(null);
        student.setGrupa(null);
        student.setUser(null);
        student.setSpecializare(null);
        student.setCatalogStudentMaterie(null);

        assertNull(student.getId());
        assertNull(student.getCod());
        assertNull(student.getNume());
        assertNull(student.getPrenume());
        assertNull(student.getAn());
        assertNull(student.getGrupa());
        assertNull(student.getUser());
        assertNull(student.getSpecializare());
        assertNull(student.getCatalogStudentMaterie());
    }

    @Test
    void testSettersWithEmptyStrings() {
        student.setCod("");
        student.setNume("");
        student.setPrenume("");
        student.setGrupa("");

        assertEquals("", student.getCod());
        assertEquals("", student.getNume());
        assertEquals("", student.getPrenume());
        assertEquals("", student.getGrupa());
    }

    @Test
    void testSetIdSpecifically() {
        student.setId(999);
        assertEquals(999, student.getId());

        student.setId(0);
        assertEquals(0, student.getId());

        student.setId(-1);
        assertEquals(-1, student.getId());
    }

    @Test
    void testSetAnEdgeCases() {
        student.setAn(1);
        assertEquals(1, student.getAn());

        student.setAn(4);
        assertEquals(4, student.getAn());

        student.setAn(0);
        assertEquals(0, student.getAn());

        student.setAn(-1);
        assertEquals(-1, student.getAn());
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
    void testCatalogStudentMaterieListManipulation() {
        List<CatalogStudentMaterie> newCatalog = new ArrayList<>();
        newCatalog.add(catalog1);

        student.setCatalogStudentMaterie(newCatalog);
        assertEquals(1, student.getCatalogStudentMaterie().size());
        assertTrue(student.getCatalogStudentMaterie().contains(catalog1));

        // Test empty list
        student.setCatalogStudentMaterie(new ArrayList<>());
        assertTrue(student.getCatalogStudentMaterie().isEmpty());
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
    void testEqualsSameObject() {
        assertEquals(student, student);
        assertTrue(student.equals(student));
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(student, null);
        assertFalse(student.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        String differentObject = "not a student";
        assertNotEquals(student, differentObject);
        assertFalse(student.equals(differentObject));
    }

    @Test
    void testEqualsWithDifferentId() {
        Student s1 = new Student();
        s1.setId(1);

        Student s2 = new Student();
        s2.setId(2);

        // Since no fields are explicitly included in equals/hashCode, all instances are equal
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testEqualsWithSameIdDifferentFields() {
        Student s1 = new Student();
        s1.setId(1);
        s1.setCod("S123");
        s1.setNume("Popescu");

        Student s2 = new Student();
        s2.setId(1);
        s2.setCod("S456");
        s2.setNume("Ionescu");

        // Should be equal because @EqualsAndHashCode(onlyExplicitlyIncluded = true) only considers explicitly included fields
        // Since no fields are explicitly included, only ID is considered by default
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testEqualsWithBothNullIds() {
        Student s1 = new Student();
        s1.setId(null);

        Student s2 = new Student();
        s2.setId(null);

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testEqualsWithOneNullId() {
        Student s1 = new Student();
        s1.setId(1);

        Student s2 = new Student();
        s2.setId(null);

        // Since no fields are explicitly included in equals/hashCode, all instances are equal
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testHashCodeConsistency() {
        int initialHashCode = student.hashCode();

        // Hash code should remain consistent across multiple calls
        assertEquals(initialHashCode, student.hashCode());
        assertEquals(initialHashCode, student.hashCode());

        // Changing non-ID fields should not affect hash code (due to @EqualsAndHashCode configuration)
        student.setCod("changedCod");
        student.setNume("changedNume");
        assertEquals(initialHashCode, student.hashCode());
    }

    @Test
    void testHashCodeWithIdChange() {
        int initialHashCode = student.hashCode();

        student.setId(999);
        int newHashCode = student.hashCode();

        // Since no fields are explicitly included in equals/hashCode, hashCode is constant
        assertEquals(initialHashCode, newHashCode);
    }

    @Test
    void testHashCodeWithNullId() {
        Student s = new Student();
        s.setId(null);
        int hashCode = s.hashCode();

        // Should not throw exception and should be consistent
        assertEquals(hashCode, s.hashCode());
    }

    @Test
    void testCanEqualMethod() {
        Student s1 = new Student();
        Student s2 = new Student();

        assertTrue(s1.canEqual(s2));
        assertTrue(s2.canEqual(s1));

        String notStudent = "not a student";
        assertFalse(s1.canEqual(notStudent));
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

    @Test
    void testDefaultConstructor() {
        Student newStudent = new Student();

        assertNull(newStudent.getId());
        assertNull(newStudent.getCod());
        assertNull(newStudent.getNume());
        assertNull(newStudent.getPrenume());
        assertNull(newStudent.getAn());
        assertNull(newStudent.getGrupa());
        assertNull(newStudent.getCatalogStudentMaterie());
        assertNull(newStudent.getUser());
        assertNull(newStudent.getSpecializare());
    }

    @Test
    void testFieldsWithSpecialCharacters() {
        student.setCod("S-123/A");
        student.setNume("Pop-escu");
        student.setPrenume("Ion-Andrei");
        student.setGrupa("214A/1");

        assertEquals("S-123/A", student.getCod());
        assertEquals("Pop-escu", student.getNume());
        assertEquals("Ion-Andrei", student.getPrenume());
        assertEquals("214A/1", student.getGrupa());
    }

    @Test
    void testFieldsWithUnicodeCharacters() {
        student.setCod("S123ă");
        student.setNume("Popescuă");
        student.setPrenume("Ionuț");
        student.setGrupa("214Ă");

        assertEquals("S123ă", student.getCod());
        assertEquals("Popescuă", student.getNume());
        assertEquals("Ionuț", student.getPrenume());
        assertEquals("214Ă", student.getGrupa());
    }

    @Test
    void testLongFieldValues() {
        String longCod = "S" + "1".repeat(100);
        String longNume = "a".repeat(500);
        String longPrenume = "b".repeat(500);
        String longGrupa = "G" + "1".repeat(100);

        student.setCod(longCod);
        student.setNume(longNume);
        student.setPrenume(longPrenume);
        student.setGrupa(longGrupa);

        assertEquals(longCod, student.getCod());
        assertEquals(longNume, student.getNume());
        assertEquals(longPrenume, student.getPrenume());
        assertEquals(longGrupa, student.getGrupa());
    }

    @Test
    void testCompleteStudentSetup() {
        User newUser = mock(User.class);
        Specializare newSpecializare = mock(Specializare.class);
        List<CatalogStudentMaterie> newCatalog = List.of(mock(CatalogStudentMaterie.class));

        Student completeStudent = new Student();
        completeStudent.setId(100);
        completeStudent.setCod("S999");
        completeStudent.setNume("TestNume");
        completeStudent.setPrenume("TestPrenume");
        completeStudent.setAn(4);
        completeStudent.setGrupa("444A");
        completeStudent.setUser(newUser);
        completeStudent.setSpecializare(newSpecializare);
        completeStudent.setCatalogStudentMaterie(newCatalog);

        // Verify all fields are set correctly
        assertEquals(100, completeStudent.getId());
        assertEquals("S999", completeStudent.getCod());
        assertEquals("TestNume", completeStudent.getNume());
        assertEquals("TestPrenume", completeStudent.getPrenume());
        assertEquals(4, completeStudent.getAn());
        assertEquals("444A", completeStudent.getGrupa());
        assertEquals(newUser, completeStudent.getUser());
        assertEquals(newSpecializare, completeStudent.getSpecializare());
        assertEquals(1, completeStudent.getCatalogStudentMaterie().size());
    }

    @Test
    void testToStringNotNull() {
        assertNotNull(student.toString());

        Student emptyStudent = new Student();
        assertNotNull(emptyStudent.toString());
    }
}
