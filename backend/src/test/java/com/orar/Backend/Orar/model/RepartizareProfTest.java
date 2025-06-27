package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RepartizareProfTest {

    private RepartizareProf repartizareProf;
    private Profesor profesor;
    private Materie materie;
    private Orar orar1, orar2;

    @BeforeEach
    void setUp() {
        profesor = mock(Profesor.class);
        materie = mock(Materie.class);
        orar1 = mock(Orar.class);
        orar2 = mock(Orar.class);

        repartizareProf = new RepartizareProf();
        repartizareProf.setId(1);
        repartizareProf.setTip("Curs");
        repartizareProf.setProfesor(profesor);
        repartizareProf.setMaterie(materie);
        repartizareProf.setOrar(List.of(orar1, orar2));
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, repartizareProf.getId());
        assertEquals("Curs", repartizareProf.getTip());
        assertEquals(profesor, repartizareProf.getProfesor());
        assertEquals(materie, repartizareProf.getMaterie());
        assertEquals(2, repartizareProf.getOrar().size());
    }

    @Test
    void testSettersWithNullValues() {
        repartizareProf.setTip(null);
        repartizareProf.setProfesor(null);
        repartizareProf.setMaterie(null);
        repartizareProf.setOrar(null);

        assertNull(repartizareProf.getTip());
        assertNull(repartizareProf.getProfesor());
        assertNull(repartizareProf.getMaterie());
        assertNull(repartizareProf.getOrar());
    }

    @Test
    void testSettersWithEmptyStrings() {
        repartizareProf.setTip("");
        assertEquals("", repartizareProf.getTip());
    }

    @Test
    void testSetIdSpecifically() {
        repartizareProf.setId(999);
        assertEquals(999, repartizareProf.getId());

        repartizareProf.setId(0);
        assertEquals(0, repartizareProf.getId());

        repartizareProf.setId(-1);
        assertEquals(-1, repartizareProf.getId());
    }

    @Test
    void testSetTip() {
        repartizareProf.setTip("Laborator");
        assertEquals("Laborator", repartizareProf.getTip());

        repartizareProf.setTip("Seminar");
        assertEquals("Seminar", repartizareProf.getTip());

        repartizareProf.setTip("Proiect");
        assertEquals("Proiect", repartizareProf.getTip());
    }

    @Test
    void testSetProfesor() {
        Profesor newProf = mock(Profesor.class);
        repartizareProf.setProfesor(newProf);
        assertEquals(newProf, repartizareProf.getProfesor());
    }

    @Test
    void testSetMaterie() {
        Materie newMaterie = mock(Materie.class);
        repartizareProf.setMaterie(newMaterie);
        assertEquals(newMaterie, repartizareProf.getMaterie());
    }

    @Test
    void testOrarInitialization() {
        RepartizareProf empty = new RepartizareProf();
        assertNotNull(empty.getOrar());
        assertTrue(empty.getOrar().isEmpty());
    }

    @Test
    void testOrarListManipulation() {
        List<Orar> newOrar = new ArrayList<>();
        newOrar.add(orar1);

        repartizareProf.setOrar(newOrar);
        assertEquals(1, repartizareProf.getOrar().size());
        assertTrue(repartizareProf.getOrar().contains(orar1));

        // Test empty list
        repartizareProf.setOrar(new ArrayList<>());
        assertTrue(repartizareProf.getOrar().isEmpty());
    }

    @Test
    void testEqualsAndHashCode() {
        RepartizareProf r1 = new RepartizareProf();
        r1.setId(10);
        RepartizareProf r2 = new RepartizareProf();
        r2.setId(10);

        // Since no fields are explicitly included in equals/hashCode, all instances are equal
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testEqualsSameObject() {
        assertEquals(repartizareProf, repartizareProf);
        assertTrue(repartizareProf.equals(repartizareProf));
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(repartizareProf, null);
        assertFalse(repartizareProf.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        String differentObject = "not a repartizare";
        assertNotEquals(repartizareProf, differentObject);
        assertFalse(repartizareProf.equals(differentObject));
    }

    @Test
    void testEqualsWithDifferentId() {
        RepartizareProf r1 = new RepartizareProf();
        r1.setId(1);

        RepartizareProf r2 = new RepartizareProf();
        r2.setId(2);

        // Since no fields are explicitly included in equals/hashCode, all instances are equal
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testEqualsWithSameIdDifferentFields() {
        RepartizareProf r1 = new RepartizareProf();
        r1.setId(1);
        r1.setTip("Curs");

        RepartizareProf r2 = new RepartizareProf();
        r2.setId(1);
        r2.setTip("Laborator");

        // Should be equal because @EqualsAndHashCode(onlyExplicitlyIncluded = true) with no explicitly included fields
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testEqualsWithBothZeroIds() {
        RepartizareProf r1 = new RepartizareProf();
        r1.setId(0);

        RepartizareProf r2 = new RepartizareProf();
        r2.setId(0);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testHashCodeConsistency() {
        int initialHashCode = repartizareProf.hashCode();

        // Hash code should remain consistent across multiple calls
        assertEquals(initialHashCode, repartizareProf.hashCode());
        assertEquals(initialHashCode, repartizareProf.hashCode());

        // Changing fields should not affect hash code (due to @EqualsAndHashCode configuration)
        repartizareProf.setTip("changedTip");
        assertEquals(initialHashCode, repartizareProf.hashCode());
    }

    @Test
    void testHashCodeWithIdChange() {
        int initialHashCode = repartizareProf.hashCode();

        repartizareProf.setId(999);
        int newHashCode = repartizareProf.hashCode();

        // Since no fields are explicitly included in equals/hashCode, hashCode is constant
        assertEquals(initialHashCode, newHashCode);
    }

    @Test
    void testHashCodeWithZeroId() {
        RepartizareProf r = new RepartizareProf();
        r.setId(0);
        int hashCode = r.hashCode();

        // Should not throw exception and should be consistent
        assertEquals(hashCode, r.hashCode());
    }

    @Test
    void testCanEqualMethod() {
        RepartizareProf r1 = new RepartizareProf();
        RepartizareProf r2 = new RepartizareProf();

        assertTrue(r1.canEqual(r2));
        assertTrue(r2.canEqual(r1));

        String notRepartizare = "not a repartizare";
        assertFalse(r1.canEqual(notRepartizare));
    }

    @Test
    void testDefaultConstructor() {
        RepartizareProf newRepartizare = new RepartizareProf();

        assertEquals(0, newRepartizare.getId()); // int default
        assertNull(newRepartizare.getTip());
        assertNull(newRepartizare.getProfesor());
        assertNull(newRepartizare.getMaterie());
        assertNotNull(newRepartizare.getOrar()); // Initialized in constructor
        assertTrue(newRepartizare.getOrar().isEmpty());
    }

    @Test
    void testTipWithSpecialCharacters() {
        repartizareProf.setTip("Curs&Laborator");
        assertEquals("Curs&Laborator", repartizareProf.getTip());

        repartizareProf.setTip("Tip/Special");
        assertEquals("Tip/Special", repartizareProf.getTip());
    }

    @Test
    void testTipWithUnicodeCharacters() {
        repartizareProf.setTip("Curs Programare în Română");
        assertEquals("Curs Programare în Română", repartizareProf.getTip());

        repartizareProf.setTip("Laborator Învățare");
        assertEquals("Laborator Învățare", repartizareProf.getTip());
    }

    @Test
    void testLongTipName() {
        String longTip = "Foarte foarte foarte lungă denumire de tip care depășește limitele normale și include multe cuvinte pentru testare";
        repartizareProf.setTip(longTip);
        assertEquals(longTip, repartizareProf.getTip());
    }

    @Test
    void testCompleteRepartizareSetup() {
        Profesor newProfesor = mock(Profesor.class);
        Materie newMaterie = mock(Materie.class);
        List<Orar> newOrar = List.of(
                mock(Orar.class),
                mock(Orar.class),
                mock(Orar.class)
        );

        RepartizareProf completeRepartizare = new RepartizareProf();
        completeRepartizare.setId(200);
        completeRepartizare.setTip("Proiect");
        completeRepartizare.setProfesor(newProfesor);
        completeRepartizare.setMaterie(newMaterie);
        completeRepartizare.setOrar(newOrar);

        // Verify all fields are set correctly
        assertEquals(200, completeRepartizare.getId());
        assertEquals("Proiect", completeRepartizare.getTip());
        assertEquals(newProfesor, completeRepartizare.getProfesor());
        assertEquals(newMaterie, completeRepartizare.getMaterie());
        assertEquals(3, completeRepartizare.getOrar().size());
    }

    @Test
    void testToStringNotNull() {
        assertNotNull(repartizareProf.toString());

        RepartizareProf emptyRepartizare = new RepartizareProf();
        assertNotNull(emptyRepartizare.toString());
    }

    @Test
    void testTipTypicalUseCases() {
        // Test common tip values
        String[] commonTips = {
                "Curs",
                "Laborator",
                "Seminar",
                "Proiect",
                "Practică",
                "Conferință",
                "Workshop"
        };

        for (String tip : commonTips) {
            repartizareProf.setTip(tip);
            assertEquals(tip, repartizareProf.getTip());
        }
    }

    @Test
    void testOrarWithLargeNumberOfElements() {
        // Test with large orar list
        List<Orar> largeOrar = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            largeOrar.add(mock(Orar.class));
        }

        repartizareProf.setOrar(largeOrar);
        assertEquals(100, repartizareProf.getOrar().size());
    }

    @Test
    void testIdPrimitiveInt() {
        // Test that id is primitive int (not Integer)
        RepartizareProf newRepartizare = new RepartizareProf();
        assertEquals(0, newRepartizare.getId()); // Default value for int

        newRepartizare.setId(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, newRepartizare.getId());

        newRepartizare.setId(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, newRepartizare.getId());
    }

    @Test
    void testBusinessLogicScenarios() {
        // Test a professor teaching multiple subjects
        Profesor prof1 = mock(Profesor.class);
        Materie materie1 = mock(Materie.class);
        Materie materie2 = mock(Materie.class);

        RepartizareProf repartizare1 = new RepartizareProf();
        repartizare1.setId(1);
        repartizare1.setTip("Curs");
        repartizare1.setProfesor(prof1);
        repartizare1.setMaterie(materie1);

        RepartizareProf repartizare2 = new RepartizareProf();
        repartizare2.setId(2);
        repartizare2.setTip("Laborator");
        repartizare2.setProfesor(prof1);
        repartizare2.setMaterie(materie2);

        // Verify they are separate repartitions but with same professor
        assertNotSame(repartizare1, repartizare2);
        assertEquals(prof1, repartizare1.getProfesor());
        assertEquals(prof1, repartizare2.getProfesor());
        assertNotSame(repartizare1.getMaterie(), repartizare2.getMaterie());
    }

    @Test
    void testNullSafetyOperations() {
        RepartizareProf nullRepartizare = new RepartizareProf();

        // Should not throw exceptions
        assertNotNull(nullRepartizare.toString());

        int hashCode1 = nullRepartizare.hashCode();
        int hashCode2 = nullRepartizare.hashCode();
        assertEquals(hashCode1, hashCode2);

        assertFalse(nullRepartizare.equals(null));
        assertTrue(nullRepartizare.equals(new RepartizareProf()));
    }
}
