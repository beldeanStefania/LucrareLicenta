package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SalaTest {

    private Sala sala;
    private Cladire cladire;
    private Orar orar1;
    private Orar orar2;

    @BeforeEach
    void setUp() {
        sala = new Sala();
        sala.setId(1);
        sala.setNume("Sala 101");
        sala.setCapacitate(30);

        cladire = mock(Cladire.class);
        sala.setCladire(cladire);

        orar1 = mock(Orar.class);
        orar2 = mock(Orar.class);

        sala.setOrar(List.of(orar1, orar2));
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, sala.getId());
        assertEquals("Sala 101", sala.getNume());
        assertEquals(30, sala.getCapacitate());
        assertEquals(cladire, sala.getCladire());

        assertNotNull(sala.getOrar());
        assertEquals(2, sala.getOrar().size());
        assertTrue(sala.getOrar().contains(orar1));
    }

    @Test
    void testSettersWithNullValues() {
        sala.setId(null);
        sala.setNume(null);
        sala.setCapacitate(null);
        sala.setCladire(null);
        sala.setOrar(null);

        assertNull(sala.getId());
        assertNull(sala.getNume());
        assertNull(sala.getCapacitate());
        assertNull(sala.getCladire());
        assertNull(sala.getOrar());
    }

    @Test
    void testSettersWithEmptyStrings() {
        sala.setNume("");
        assertEquals("", sala.getNume());
    }

    @Test
    void testSetIdSpecifically() {
        sala.setId(999);
        assertEquals(999, sala.getId());

        sala.setId(0);
        assertEquals(0, sala.getId());

        sala.setId(-1);
        assertEquals(-1, sala.getId());
    }

    @Test
    void testSetCapacitateEdgeCases() {
        sala.setCapacitate(1);
        assertEquals(1, sala.getCapacitate());

        sala.setCapacitate(500);
        assertEquals(500, sala.getCapacitate());

        sala.setCapacitate(0);
        assertEquals(0, sala.getCapacitate());

        sala.setCapacitate(-1);
        assertEquals(-1, sala.getCapacitate());
    }

    @Test
    void testUpdateSalaFields() {
        sala.setNume("Sala 305");
        sala.setCapacitate(45);

        assertEquals("Sala 305", sala.getNume());
        assertEquals(45, sala.getCapacitate());
    }

    @Test
    void testOrarListManipulation() {
        List<Orar> newOrar = new ArrayList<>();
        newOrar.add(orar1);

        sala.setOrar(newOrar);
        assertEquals(1, sala.getOrar().size());
        assertTrue(sala.getOrar().contains(orar1));

        // Test empty list
        sala.setOrar(new ArrayList<>());
        assertTrue(sala.getOrar().isEmpty());
    }

    @Test
    void testEqualsAndHashCode() {
        Sala s1 = new Sala();
        s1.setId(10);

        Sala s2 = new Sala();
        s2.setId(10);

        // Since no fields are explicitly included in equals/hashCode, all instances are equal
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testEqualsSameObject() {
        assertEquals(sala, sala);
        assertTrue(sala.equals(sala));
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(sala, null);
        assertFalse(sala.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        String differentObject = "not a sala";
        assertNotEquals(sala, differentObject);
        assertFalse(sala.equals(differentObject));
    }

    @Test
    void testEqualsWithDifferentId() {
        Sala s1 = new Sala();
        s1.setId(1);

        Sala s2 = new Sala();
        s2.setId(2);

        // Since no fields are explicitly included in equals/hashCode, all instances are equal
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testEqualsWithSameIdDifferentFields() {
        Sala s1 = new Sala();
        s1.setId(1);
        s1.setNume("Sala A");
        s1.setCapacitate(50);

        Sala s2 = new Sala();
        s2.setId(1);
        s2.setNume("Sala B");
        s2.setCapacitate(100);

        // Should be equal because @EqualsAndHashCode(onlyExplicitlyIncluded = true) with no explicitly included fields
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testEqualsWithBothNullIds() {
        Sala s1 = new Sala();
        s1.setId(null);

        Sala s2 = new Sala();
        s2.setId(null);

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testEqualsWithOneNullId() {
        Sala s1 = new Sala();
        s1.setId(1);

        Sala s2 = new Sala();
        s2.setId(null);

        // Since no fields are explicitly included in equals/hashCode, all instances are equal
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testHashCodeConsistency() {
        int initialHashCode = sala.hashCode();

        // Hash code should remain consistent across multiple calls
        assertEquals(initialHashCode, sala.hashCode());
        assertEquals(initialHashCode, sala.hashCode());

        // Changing fields should not affect hash code (due to @EqualsAndHashCode configuration)
        sala.setNume("changedNume");
        sala.setCapacitate(999);
        assertEquals(initialHashCode, sala.hashCode());
    }

    @Test
    void testHashCodeWithIdChange() {
        int initialHashCode = sala.hashCode();

        sala.setId(999);
        int newHashCode = sala.hashCode();

        // Since no fields are explicitly included in equals/hashCode, hashCode is constant
        assertEquals(initialHashCode, newHashCode);
    }

    @Test
    void testHashCodeWithNullId() {
        Sala s = new Sala();
        s.setId(null);
        int hashCode = s.hashCode();

        // Should not throw exception and should be consistent
        assertEquals(hashCode, s.hashCode());
    }

    @Test
    void testCanEqualMethod() {
        Sala s1 = new Sala();
        Sala s2 = new Sala();

        assertTrue(s1.canEqual(s2));
        assertTrue(s2.canEqual(s1));

        String notSala = "not a sala";
        assertFalse(s1.canEqual(notSala));
    }

    @Test
    void testNullFields() {
        Sala s = new Sala();

        assertNull(s.getId());
        assertNull(s.getNume());
        assertNull(s.getCapacitate());
        assertNull(s.getCladire());
        assertNull(s.getOrar());
    }

    @Test
    void testDefaultConstructor() {
        Sala newSala = new Sala();

        assertNull(newSala.getId());
        assertNull(newSala.getNume());
        assertNull(newSala.getCapacitate());
        assertNull(newSala.getCladire());
        assertNull(newSala.getOrar());
    }

    @Test
    void testSalaNameWithSpecialCharacters() {
        sala.setNume("Sala A&B - Complex");
        assertEquals("Sala A&B - Complex", sala.getNume());

        sala.setNume("Sala 101/A");
        assertEquals("Sala 101/A", sala.getNume());
    }

    @Test
    void testSalaNameWithUnicodeCharacters() {
        sala.setNume("Sală");
        assertEquals("Sală", sala.getNume());

        sala.setNume("Sală de conferințe");
        assertEquals("Sală de conferințe", sala.getNume());
    }

    @Test
    void testLongSalaName() {
        String longName = "Foarte foarte foarte lungă denumire de sală care depășește limitele normale și include multe cuvinte pentru testare";
        sala.setNume(longName);
        assertEquals(longName, sala.getNume());
    }

    @Test
    void testCapacitateTypicalValues() {
        // Test common capacity values
        int[] commonCapacities = {10, 20, 30, 50, 100, 150, 200, 300, 500};

        for (int capacity : commonCapacities) {
            sala.setCapacitate(capacity);
            assertEquals(capacity, sala.getCapacitate());
        }
    }

    @Test
    void testCompleteSalaSetup() {
        Cladire newCladire = mock(Cladire.class);
        List<Orar> newOrar = List.of(
                mock(Orar.class),
                mock(Orar.class),
                mock(Orar.class)
        );

        Sala completeSala = new Sala();
        completeSala.setId(200);
        completeSala.setNume("Amfiteatru Mare");
        completeSala.setCapacitate(300);
        completeSala.setCladire(newCladire);
        completeSala.setOrar(newOrar);

        // Verify all fields are set correctly
        assertEquals(200, completeSala.getId());
        assertEquals("Amfiteatru Mare", completeSala.getNume());
        assertEquals(300, completeSala.getCapacitate());
        assertEquals(newCladire, completeSala.getCladire());
        assertEquals(3, completeSala.getOrar().size());
    }

    @Test
    void testToStringNotNull() {
        assertNotNull(sala.toString());

        Sala emptySala = new Sala();
        assertNotNull(emptySala.toString());
    }

    @Test
    void testSalaTypicalUseCases() {
        // Test common room types
        String[] commonRoomNames = {
                "Sala 101",
                "Amfiteatru",
                "Laborator Informatică",
                "Sala de conferințe",
                "Sala de seminar",
                "Aula Magna"
        };

        for (String name : commonRoomNames) {
            sala.setNume(name);
            assertEquals(name, sala.getNume());
        }
    }

    @Test
    void testOrarWithLargeNumberOfElements() {
        // Test with large orar list
        List<Orar> largeOrar = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            largeOrar.add(mock(Orar.class));
        }

        sala.setOrar(largeOrar);
        assertEquals(50, sala.getOrar().size());
    }
}
