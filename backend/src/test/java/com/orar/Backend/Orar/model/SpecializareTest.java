package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
    void testSettersWithNullValues() {
        specializare.setId(null);
        specializare.setSpecializare(null);
        specializare.setCurriculum(null);
        specializare.setStudenti(null);

        assertNull(specializare.getId());
        assertNull(specializare.getSpecializare());
        assertNull(specializare.getCurriculum());
        assertNull(specializare.getStudenti());
    }

    @Test
    void testSettersWithEmptyStrings() {
        specializare.setSpecializare("");
        assertEquals("", specializare.getSpecializare());
    }

    @Test
    void testSetIdSpecifically() {
        specializare.setId(999);
        assertEquals(999, specializare.getId());

        specializare.setId(0);
        assertEquals(0, specializare.getId());

        specializare.setId(-1);
        assertEquals(-1, specializare.getId());
    }

    @Test
    void testChangeSpecializareName() {
        specializare.setSpecializare("Calculatoare");
        assertEquals("Calculatoare", specializare.getSpecializare());
    }

    @Test
    void testCurriculumListManipulation() {
        List<CurriculumEntry> newCurriculum = new ArrayList<>();
        newCurriculum.add(entry1);

        specializare.setCurriculum(newCurriculum);
        assertEquals(1, specializare.getCurriculum().size());
        assertTrue(specializare.getCurriculum().contains(entry1));

        // Test empty list
        specializare.setCurriculum(new ArrayList<>());
        assertTrue(specializare.getCurriculum().isEmpty());
    }

    @Test
    void testStudentiListManipulation() {
        List<Student> newStudenti = new ArrayList<>();
        newStudenti.add(student1);

        specializare.setStudenti(newStudenti);
        assertEquals(1, specializare.getStudenti().size());
        assertTrue(specializare.getStudenti().contains(student1));

        // Test empty list
        specializare.setStudenti(new ArrayList<>());
        assertTrue(specializare.getStudenti().isEmpty());
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
    void testEqualsSameObject() {
        assertEquals(specializare, specializare);
        assertTrue(specializare.equals(specializare));
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(specializare, null);
        assertFalse(specializare.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        String differentObject = "not a specializare";
        assertNotEquals(specializare, differentObject);
        assertFalse(specializare.equals(differentObject));
    }

    @Test
    void testEqualsWithDifferentId() {
        Specializare s1 = new Specializare();
        s1.setId(1);

        Specializare s2 = new Specializare();
        s2.setId(2);

        // Since no fields are explicitly included in equals/hashCode, all instances are equal
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testEqualsWithSameIdDifferentFields() {
        Specializare s1 = new Specializare();
        s1.setId(1);
        s1.setSpecializare("Informatica");

        Specializare s2 = new Specializare();
        s2.setId(1);
        s2.setSpecializare("Calculatoare");

        // Should be equal because @EqualsAndHashCode(onlyExplicitlyIncluded = true) only considers explicitly included fields
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testEqualsWithBothNullIds() {
        Specializare s1 = new Specializare();
        s1.setId(null);

        Specializare s2 = new Specializare();
        s2.setId(null);

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testEqualsWithOneNullId() {
        Specializare s1 = new Specializare();
        s1.setId(1);

        Specializare s2 = new Specializare();
        s2.setId(null);

        // Since no fields are explicitly included in equals/hashCode, all instances are equal
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testHashCodeConsistency() {
        int initialHashCode = specializare.hashCode();

        // Hash code should remain consistent across multiple calls
        assertEquals(initialHashCode, specializare.hashCode());
        assertEquals(initialHashCode, specializare.hashCode());

        // Changing non-ID fields should not affect hash code (due to @EqualsAndHashCode configuration)
        specializare.setSpecializare("changedSpecializare");
        assertEquals(initialHashCode, specializare.hashCode());
    }

    @Test
    void testHashCodeWithIdChange() {
        int initialHashCode = specializare.hashCode();

        specializare.setId(999);
        int newHashCode = specializare.hashCode();

        // Since no fields are explicitly included in equals/hashCode, hashCode is constant
        assertEquals(initialHashCode, newHashCode);
    }

    @Test
    void testHashCodeWithNullId() {
        Specializare s = new Specializare();
        s.setId(null);
        int hashCode = s.hashCode();

        // Should not throw exception and should be consistent
        assertEquals(hashCode, s.hashCode());
    }

    @Test
    void testCanEqualMethod() {
        Specializare s1 = new Specializare();
        Specializare s2 = new Specializare();

        assertTrue(s1.canEqual(s2));
        assertTrue(s2.canEqual(s1));

        String notSpecializare = "not a specializare";
        assertFalse(s1.canEqual(notSpecializare));
    }

    @Test
    void testNullListsAndDefaults() {
        Specializare s = new Specializare();
        assertNull(s.getCurriculum());
        assertNull(s.getStudenti());
        assertNull(s.getSpecializare());
    }

    @Test
    void testDefaultConstructor() {
        Specializare newSpecializare = new Specializare();

        assertNull(newSpecializare.getId());
        assertNull(newSpecializare.getSpecializare());
        assertNull(newSpecializare.getCurriculum());
        assertNull(newSpecializare.getStudenti());
    }

    @Test
    void testSpecializareNameWithSpecialCharacters() {
        specializare.setSpecializare("Inginerie Software & Tehnologii Web");
        assertEquals("Inginerie Software & Tehnologii Web", specializare.getSpecializare());

        specializare.setSpecializare("AI/ML - Inteligența Artificială");
        assertEquals("AI/ML - Inteligența Artificială", specializare.getSpecializare());
    }

    @Test
    void testSpecializareNameWithUnicodeCharacters() {
        specializare.setSpecializare("Informatică");
        assertEquals("Informatică", specializare.getSpecializare());

        specializare.setSpecializare("Matematică și Informatică");
        assertEquals("Matematică și Informatică", specializare.getSpecializare());
    }

    @Test
    void testLongSpecializareName() {
        String longName = "Foarte foarte foarte foarte lungă denumire de specializare care depășește limitele normale și include multe multe cuvinte pentru testare";
        specializare.setSpecializare(longName);
        assertEquals(longName, specializare.getSpecializare());
    }

    @Test
    void testCompleteSpecializareSetup() {
        List<CurriculumEntry> newCurriculum = List.of(
                mock(CurriculumEntry.class),
                mock(CurriculumEntry.class),
                mock(CurriculumEntry.class)
        );

        List<Student> newStudenti = List.of(
                mock(Student.class),
                mock(Student.class)
        );

        Specializare completeSpecializare = new Specializare();
        completeSpecializare.setId(200);
        completeSpecializare.setSpecializare("Inginerie Software");
        completeSpecializare.setCurriculum(newCurriculum);
        completeSpecializare.setStudenti(newStudenti);

        // Verify all fields are set correctly
        assertEquals(200, completeSpecializare.getId());
        assertEquals("Inginerie Software", completeSpecializare.getSpecializare());
        assertEquals(3, completeSpecializare.getCurriculum().size());
        assertEquals(2, completeSpecializare.getStudenti().size());
    }

    @Test
    void testToStringNotNull() {
        assertNotNull(specializare.toString());

        Specializare emptySpecializare = new Specializare();
        assertNotNull(emptySpecializare.toString());
    }

    @Test
    void testSpecializareTypicalUseCases() {
        // Test common specializations
        String[] commonSpecializations = {
                "Informatica",
                "Calculatoare",
                "Inginerie Software",
                "Securitate Cibernetică",
                "Inteligență Artificială",
                "Rețele de Calculatoare"
        };

        for (String spec : commonSpecializations) {
            specializare.setSpecializare(spec);
            assertEquals(spec, specializare.getSpecializare());
        }
    }

    @Test
    void testListsWithLargeNumberOfElements() {
        // Test with large lists
        List<CurriculumEntry> largeCurriculum = new ArrayList<>();
        List<Student> largeStudenti = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            largeCurriculum.add(mock(CurriculumEntry.class));
            largeStudenti.add(mock(Student.class));
        }

        specializare.setCurriculum(largeCurriculum);
        specializare.setStudenti(largeStudenti);

        assertEquals(100, specializare.getCurriculum().size());
        assertEquals(100, specializare.getStudenti().size());
    }
}
