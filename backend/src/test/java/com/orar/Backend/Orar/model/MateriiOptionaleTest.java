package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void testSettersWithNullValues() {
        optionale.setId(null);
        optionale.setNume(null);
        optionale.setCurriculumEntries(null);

        assertNull(optionale.getId());
        assertNull(optionale.getNume());
        assertNull(optionale.getCurriculumEntries());
    }

    @Test
    void testSettersWithEmptyStrings() {
        optionale.setNume("");
        assertEquals("", optionale.getNume());
    }

    @Test
    void testSetIdSpecifically() {
        optionale.setId(999);
        assertEquals(999, optionale.getId());

        optionale.setId(0);
        assertEquals(0, optionale.getId());

        optionale.setId(-1);
        assertEquals(-1, optionale.getId());
    }

    @Test
    void testSetCurriculumEntries() {
        CurriculumEntry entry = mock(CurriculumEntry.class);
        optionale.setCurriculumEntries(Collections.singletonList(entry));
        assertEquals(1, optionale.getCurriculumEntries().size());
        assertTrue(optionale.getCurriculumEntries().contains(entry));
    }

    @Test
    void testCurriculumEntriesListManipulation() {
        CurriculumEntry entry1 = mock(CurriculumEntry.class);
        CurriculumEntry entry2 = mock(CurriculumEntry.class);

        List<CurriculumEntry> entries = new ArrayList<>();
        entries.add(entry1);
        entries.add(entry2);

        optionale.setCurriculumEntries(entries);
        assertEquals(2, optionale.getCurriculumEntries().size());
        assertTrue(optionale.getCurriculumEntries().contains(entry1));
        assertTrue(optionale.getCurriculumEntries().contains(entry2));

        // Test empty list
        optionale.setCurriculumEntries(new ArrayList<>());
        assertTrue(optionale.getCurriculumEntries().isEmpty());
    }

    @Test
    void testEqualsAndHashCode() {
        MateriiOptionale m1 = new MateriiOptionale();
        m1.setId(1);

        MateriiOptionale m2 = new MateriiOptionale();
        m2.setId(1);

        MateriiOptionale m3 = new MateriiOptionale();
        m3.setId(2);

        // Since id is explicitly included in equals/hashCode, objects with same id are equal
        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());

        // Objects with different ids are not equal
        assertNotEquals(m1, m3);
        assertNotEquals(m1.hashCode(), m3.hashCode());
    }

    @Test
    void testEqualsSameObject() {
        assertEquals(optionale, optionale);
        assertTrue(optionale.equals(optionale));
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(optionale, null);
        assertFalse(optionale.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        String differentObject = "not a materii optionale";
        assertNotEquals(optionale, differentObject);
        assertFalse(optionale.equals(differentObject));
    }

    @Test
    void testEqualsWithSameIdDifferentFields() {
        MateriiOptionale m1 = new MateriiOptionale();
        m1.setId(1);
        m1.setNume("AI Avansat");

        MateriiOptionale m2 = new MateriiOptionale();
        m2.setId(1);
        m2.setNume("Machine Learning");

        // Should be equal because only id is included in equals/hashCode
        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    void testEqualsWithBothNullIds() {
        MateriiOptionale m1 = new MateriiOptionale();
        m1.setId(null);

        MateriiOptionale m2 = new MateriiOptionale();
        m2.setId(null);

        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    void testEqualsWithOneNullId() {
        MateriiOptionale m1 = new MateriiOptionale();
        m1.setId(1);

        MateriiOptionale m2 = new MateriiOptionale();
        m2.setId(null);

        assertNotEquals(m1, m2);
        assertNotEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    void testHashCodeConsistency() {
        int initialHashCode = optionale.hashCode();

        // Hash code should remain consistent across multiple calls
        assertEquals(initialHashCode, optionale.hashCode());
        assertEquals(initialHashCode, optionale.hashCode());

        // Changing non-included fields should not affect hash code
        optionale.setNume("changedNume");
        assertEquals(initialHashCode, optionale.hashCode());
    }

    @Test
    void testHashCodeWithIdChange() {
        int initialHashCode = optionale.hashCode();

        optionale.setId(999);
        int newHashCode = optionale.hashCode();

        // Since id is included in equals/hashCode, hashCode should change
        assertNotEquals(initialHashCode, newHashCode);
    }

    @Test
    void testHashCodeWithNullId() {
        MateriiOptionale m = new MateriiOptionale();
        m.setId(null);
        int hashCode = m.hashCode();

        // Should not throw exception and should be consistent
        assertEquals(hashCode, m.hashCode());
    }

    @Test
    void testCanEqualMethod() {
        MateriiOptionale m1 = new MateriiOptionale();
        MateriiOptionale m2 = new MateriiOptionale();

        assertTrue(m1.canEqual(m2));
        assertTrue(m2.canEqual(m1));

        String notMaterii = "not a materii optionale";
        assertFalse(m1.canEqual(notMaterii));
    }

    @Test
    void testDefaultConstructor() {
        MateriiOptionale newMaterii = new MateriiOptionale();

        assertNull(newMaterii.getId());
        assertNull(newMaterii.getNume());
        assertNull(newMaterii.getCurriculumEntries());
    }

    @Test
    void testMateriiNameWithSpecialCharacters() {
        optionale.setNume("AI & Machine Learning");
        assertEquals("AI & Machine Learning", optionale.getNume());

        optionale.setNume("Prog. Avansată - C++");
        assertEquals("Prog. Avansată - C++", optionale.getNume());

        optionale.setNume("Web Dev (PHP/JS)");
        assertEquals("Web Dev (PHP/JS)", optionale.getNume());
    }

    @Test
    void testMateriiNameWithUnicodeCharacters() {
        optionale.setNume("Programare în Python");
        assertEquals("Programare în Python", optionale.getNume());

        optionale.setNume("Inteligență Artificială");
        assertEquals("Inteligență Artificială", optionale.getNume());

        optionale.setNume("Învățare Automată");
        assertEquals("Învățare Automată", optionale.getNume());
    }

    @Test
    void testLongMateriiName() {
        String longName = "Foarte foarte foarte lungă denumire de materie opțională care depășește limitele normale și include multe cuvinte pentru testare completă";
        optionale.setNume(longName);
        assertEquals(longName, optionale.getNume());
    }

    @Test
    void testCompleteMateriiOptionaleSetup() {
        List<CurriculumEntry> newEntries = List.of(
                mock(CurriculumEntry.class),
                mock(CurriculumEntry.class),
                mock(CurriculumEntry.class)
        );

        MateriiOptionale completeMaterii = new MateriiOptionale();
        completeMaterii.setId(200);
        completeMaterii.setNume("Deep Learning și Rețele Neuronale");
        completeMaterii.setCurriculumEntries(newEntries);

        // Verify all fields are set correctly
        assertEquals(200, completeMaterii.getId());
        assertEquals("Deep Learning și Rețele Neuronale", completeMaterii.getNume());
        assertEquals(3, completeMaterii.getCurriculumEntries().size());
    }

    @Test
    void testToStringNotNull() {
        assertNotNull(optionale.toString());

        MateriiOptionale emptyMaterii = new MateriiOptionale();
        assertNotNull(emptyMaterii.toString());
    }

    @Test
    void testMateriiTypicalUseCases() {
        // Test common optional subjects
        String[] commonSubjects = {
                "Inteligență Artificială",
                "Machine Learning",
                "Blockchain Technology",
                "Cyber Security",
                "Mobile Development",
                "Game Development",
                "Data Science",
                "Cloud Computing",
                "DevOps",
                "Quantum Computing"
        };

        for (String subject : commonSubjects) {
            optionale.setNume(subject);
            assertEquals(subject, optionale.getNume());
        }
    }

    @Test
    void testCurriculumEntriesWithLargeNumberOfElements() {
        // Test with large curriculum entries list
        List<CurriculumEntry> largeEntries = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            largeEntries.add(mock(CurriculumEntry.class));
        }

        optionale.setCurriculumEntries(largeEntries);
        assertEquals(50, optionale.getCurriculumEntries().size());
    }

    @Test
    void testBusinessLogicScenarios() {
        // Test typical optional subjects scenarios

        // Technical elective
        optionale.setNume("Advanced Algorithms");
        CurriculumEntry algorithmEntry = mock(CurriculumEntry.class);
        optionale.setCurriculumEntries(List.of(algorithmEntry));

        assertEquals("Advanced Algorithms", optionale.getNume());
        assertEquals(1, optionale.getCurriculumEntries().size());

        // Interdisciplinary elective
        optionale.setNume("Bioinformatics");
        CurriculumEntry bioEntry1 = mock(CurriculumEntry.class);
        CurriculumEntry bioEntry2 = mock(CurriculumEntry.class);
        optionale.setCurriculumEntries(List.of(bioEntry1, bioEntry2));

        assertEquals("Bioinformatics", optionale.getNume());
        assertEquals(2, optionale.getCurriculumEntries().size());
    }

    @Test
    void testNullSafetyOperations() {
        MateriiOptionale nullMaterii = new MateriiOptionale();

        // Should not throw exceptions
        assertNotNull(nullMaterii.toString());

        int hashCode1 = nullMaterii.hashCode();
        int hashCode2 = nullMaterii.hashCode();
        assertEquals(hashCode1, hashCode2);

        assertFalse(nullMaterii.equals(null));
        assertTrue(nullMaterii.equals(new MateriiOptionale()));
    }

    @Test
    void testSubjectCategories() {
        // Test different categories of optional subjects
        String[][] subjectCategories = {
                {"Programming", "Advanced Java Programming"},
                {"AI/ML", "Neural Networks"},
                {"Security", "Ethical Hacking"},
                {"Web", "Full Stack Development"},
                {"Mobile", "iOS Development"},
                {"Data", "Big Data Analytics"},
                {"Graphics", "Computer Graphics"},
                {"Networks", "Network Security"}
        };

        for (String[] category : subjectCategories) {
            optionale.setNume(category[1]);
            assertEquals(category[1], optionale.getNume());

            // Simulate creating curriculum entry for this category
            CurriculumEntry categoryEntry = mock(CurriculumEntry.class);
            optionale.setCurriculumEntries(List.of(categoryEntry));
            assertEquals(1, optionale.getCurriculumEntries().size());
        }
    }

    @Test
    void testIdBasedEquality() {
        // Test the specific behavior of id-based equality
        MateriiOptionale m1 = new MateriiOptionale();
        MateriiOptionale m2 = new MateriiOptionale();
        MateriiOptionale m3 = new MateriiOptionale();

        // Initially all have null ids, so they should be equal
        assertEquals(m1, m2);
        assertEquals(m1, m3);

        // Set same id to m1 and m2
        m1.setId(100);
        m2.setId(100);
        m3.setId(200);

        assertEquals(m1, m2);
        assertNotEquals(m1, m3);
        assertNotEquals(m2, m3);

        // Change other fields but keep same id
        m1.setNume("Subject A");
        m2.setNume("Subject B");

        // Should still be equal because only id matters
        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    void testIntegerIdFieldType() {
        MateriiOptionale newMaterii = new MateriiOptionale();

        // Test that id is Integer (not int), so it can be null
        assertNull(newMaterii.getId());

        newMaterii.setId(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, newMaterii.getId());

        newMaterii.setId(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, newMaterii.getId());

        newMaterii.setId(null);
        assertNull(newMaterii.getId());
    }
}
