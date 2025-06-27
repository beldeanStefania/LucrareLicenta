package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfesorTest {

    private Profesor profesor;
    private User user;
    private RepartizareProf r1, r2;

    @BeforeEach
    void setUp() {
        user = mock(User.class);
        r1 = mock(RepartizareProf.class);
        r2 = mock(RepartizareProf.class);

        profesor = new Profesor();
        profesor.setId(1);
        profesor.setNume("Popescu");
        profesor.setPrenume("Ion");
        profesor.setUser(user);
        profesor.setRepartizareProfs(List.of(r1, r2));
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, profesor.getId());
        assertEquals("Popescu", profesor.getNume());
        assertEquals("Ion", profesor.getPrenume());
        assertEquals(user, profesor.getUser());
        assertEquals(2, profesor.getRepartizareProfs().size());
    }

    @Test
    void testSettersWithNullValues() {
        profesor.setId(null);
        profesor.setNume(null);
        profesor.setPrenume(null);
        profesor.setUser(null);
        profesor.setRepartizareProfs(null);

        assertNull(profesor.getId());
        assertNull(profesor.getNume());
        assertNull(profesor.getPrenume());
        assertNull(profesor.getUser());
        assertNull(profesor.getRepartizareProfs());
    }

    @Test
    void testSettersWithEmptyStrings() {
        profesor.setNume("");
        profesor.setPrenume("");

        assertEquals("", profesor.getNume());
        assertEquals("", profesor.getPrenume());
    }

    @Test
    void testSetIdSpecifically() {
        profesor.setId(999);
        assertEquals(999, profesor.getId());

        profesor.setId(0);
        assertEquals(0, profesor.getId());

        profesor.setId(-1);
        assertEquals(-1, profesor.getId());
    }

    @Test
    void testSetNumeAndPrenume() {
        profesor.setNume("Ionescu");
        profesor.setPrenume("Maria");
        assertEquals("Ionescu", profesor.getNume());
        assertEquals("Maria", profesor.getPrenume());
    }

    @Test
    void testSetUser() {
        User newUser = mock(User.class);
        profesor.setUser(newUser);
        assertEquals(newUser, profesor.getUser());
    }

    @Test
    void testSetRepartizareProfs() {
        RepartizareProf newR = mock(RepartizareProf.class);
        profesor.setRepartizareProfs(List.of(newR));
        assertEquals(1, profesor.getRepartizareProfs().size());
        assertEquals(newR, profesor.getRepartizareProfs().get(0));
    }

    @Test
    void testRepartizareProfListManipulation() {
        List<RepartizareProf> newRepartizari = new ArrayList<>();
        newRepartizari.add(r1);

        profesor.setRepartizareProfs(newRepartizari);
        assertEquals(1, profesor.getRepartizareProfs().size());
        assertTrue(profesor.getRepartizareProfs().contains(r1));

        // Test empty list
        profesor.setRepartizareProfs(new ArrayList<>());
        assertTrue(profesor.getRepartizareProfs().isEmpty());
    }

    @Test
    void testEqualsAndHashCode() {
        Profesor p1 = new Profesor();
        p1.setId(7);
        Profesor p2 = new Profesor();
        p2.setId(7);

        // Since no fields are explicitly included in equals/hashCode, all instances are equal
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testEqualsSameObject() {
        assertEquals(profesor, profesor);
        assertTrue(profesor.equals(profesor));
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(profesor, null);
        assertFalse(profesor.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        String differentObject = "not a profesor";
        assertNotEquals(profesor, differentObject);
        assertFalse(profesor.equals(differentObject));
    }

    @Test
    void testEqualsWithDifferentId() {
        Profesor p1 = new Profesor();
        p1.setId(1);

        Profesor p2 = new Profesor();
        p2.setId(2);

        // Since no fields are explicitly included in equals/hashCode, all instances are equal
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testEqualsWithSameIdDifferentFields() {
        Profesor p1 = new Profesor();
        p1.setId(1);
        p1.setNume("Popescu");
        p1.setPrenume("Ion");

        Profesor p2 = new Profesor();
        p2.setId(1);
        p2.setNume("Ionescu");
        p2.setPrenume("Maria");

        // Should be equal because @EqualsAndHashCode(onlyExplicitlyIncluded = true) with no explicitly included fields
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testEqualsWithBothNullIds() {
        Profesor p1 = new Profesor();
        p1.setId(null);

        Profesor p2 = new Profesor();
        p2.setId(null);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testEqualsWithOneNullId() {
        Profesor p1 = new Profesor();
        p1.setId(1);

        Profesor p2 = new Profesor();
        p2.setId(null);

        // Since no fields are explicitly included in equals/hashCode, all instances are equal
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testHashCodeConsistency() {
        int initialHashCode = profesor.hashCode();

        // Hash code should remain consistent across multiple calls
        assertEquals(initialHashCode, profesor.hashCode());
        assertEquals(initialHashCode, profesor.hashCode());

        // Changing fields should not affect hash code (due to @EqualsAndHashCode configuration)
        profesor.setNume("changedNume");
        profesor.setPrenume("changedPrenume");
        assertEquals(initialHashCode, profesor.hashCode());
    }

    @Test
    void testHashCodeWithIdChange() {
        int initialHashCode = profesor.hashCode();

        profesor.setId(999);
        int newHashCode = profesor.hashCode();

        // Since no fields are explicitly included in equals/hashCode, hashCode is constant
        assertEquals(initialHashCode, newHashCode);
    }

    @Test
    void testHashCodeWithNullId() {
        Profesor p = new Profesor();
        p.setId(null);
        int hashCode = p.hashCode();

        // Should not throw exception and should be consistent
        assertEquals(hashCode, p.hashCode());
    }

    @Test
    void testCanEqualMethod() {
        Profesor p1 = new Profesor();
        Profesor p2 = new Profesor();

        assertTrue(p1.canEqual(p2));
        assertTrue(p2.canEqual(p1));

        String notProfesor = "not a profesor";
        assertFalse(p1.canEqual(notProfesor));
    }

    @Test
    void testDefaultConstructor() {
        Profesor newProfesor = new Profesor();

        assertNull(newProfesor.getId());
        assertNull(newProfesor.getNume());
        assertNull(newProfesor.getPrenume());
        assertNull(newProfesor.getRepartizareProfs());
        assertNull(newProfesor.getUser());
    }

    @Test
    void testProfesorNameWithSpecialCharacters() {
        profesor.setNume("Pop-escu");
        profesor.setPrenume("Ion-Andrei");

        assertEquals("Pop-escu", profesor.getNume());
        assertEquals("Ion-Andrei", profesor.getPrenume());

        profesor.setNume("O'Connor");
        profesor.setPrenume("Jean-Claude");

        assertEquals("O'Connor", profesor.getNume());
        assertEquals("Jean-Claude", profesor.getPrenume());
    }

    @Test
    void testProfesorNameWithUnicodeCharacters() {
        profesor.setNume("Pîrvulescu");
        profesor.setPrenume("Ștefan");

        assertEquals("Pîrvulescu", profesor.getNume());
        assertEquals("Ștefan", profesor.getPrenume());

        profesor.setNume("Țuțea");
        profesor.setPrenume("Gheorghe");

        assertEquals("Țuțea", profesor.getNume());
        assertEquals("Gheorghe", profesor.getPrenume());
    }

    @Test
    void testLongProfesorNames() {
        String longNume = "Foarte foarte foarte lung nume de profesor care depășește limitele normale";
        String longPrenume = "Foarte foarte foarte lung prenume de profesor care depășește limitele normale";

        profesor.setNume(longNume);
        profesor.setPrenume(longPrenume);

        assertEquals(longNume, profesor.getNume());
        assertEquals(longPrenume, profesor.getPrenume());
    }

    @Test
    void testCompleteProfesorSetup() {
        User newUser = mock(User.class);
        List<RepartizareProf> newRepartizari = List.of(
                mock(RepartizareProf.class),
                mock(RepartizareProf.class),
                mock(RepartizareProf.class)
        );

        Profesor completeProfesor = new Profesor();
        completeProfesor.setId(200);
        completeProfesor.setNume("Mihalcea");
        completeProfesor.setPrenume("Diana");
        completeProfesor.setUser(newUser);
        completeProfesor.setRepartizareProfs(newRepartizari);

        // Verify all fields are set correctly
        assertEquals(200, completeProfesor.getId());
        assertEquals("Mihalcea", completeProfesor.getNume());
        assertEquals("Diana", completeProfesor.getPrenume());
        assertEquals(newUser, completeProfesor.getUser());
        assertEquals(3, completeProfesor.getRepartizareProfs().size());
    }

    @Test
    void testToStringNotNull() {
        assertNotNull(profesor.toString());

        Profesor emptyProfesor = new Profesor();
        assertNotNull(emptyProfesor.toString());
    }

    @Test
    void testProfesorTypicalUseCases() {
        // Test common professor names
        String[][] commonNames = {
                {"Popescu", "Ion"},
                {"Ionescu", "Maria"},
                {"Mihalcea", "Diana"},
                {"Pîrvulescu", "Ștefan"},
                {"Constantin", "Alexandru"},
                {"Gheorghe", "Ana"}
        };

        for (String[] name : commonNames) {
            profesor.setNume(name[0]);
            profesor.setPrenume(name[1]);
            assertEquals(name[0], profesor.getNume());
            assertEquals(name[1], profesor.getPrenume());
        }
    }

    @Test
    void testRepartizareProfsWithLargeNumberOfElements() {
        // Test with large repartizare list
        List<RepartizareProf> largeRepartizari = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            largeRepartizari.add(mock(RepartizareProf.class));
        }

        profesor.setRepartizareProfs(largeRepartizari);
        assertEquals(50, profesor.getRepartizareProfs().size());
    }

    @Test
    void testBusinessLogicScenarios() {
        // Test a professor with multiple repartitions
        RepartizareProf curs = mock(RepartizareProf.class);
        RepartizareProf laborator = mock(RepartizareProf.class);
        RepartizareProf seminar = mock(RepartizareProf.class);

        profesor.setRepartizareProfs(List.of(curs, laborator, seminar));

        assertEquals(3, profesor.getRepartizareProfs().size());
        assertTrue(profesor.getRepartizareProfs().contains(curs));
        assertTrue(profesor.getRepartizareProfs().contains(laborator));
        assertTrue(profesor.getRepartizareProfs().contains(seminar));
    }

    @Test
    void testNullSafetyOperations() {
        Profesor nullProfesor = new Profesor();

        // Should not throw exceptions
        assertNotNull(nullProfesor.toString());

        int hashCode1 = nullProfesor.hashCode();
        int hashCode2 = nullProfesor.hashCode();
        assertEquals(hashCode1, hashCode2);

        assertFalse(nullProfesor.equals(null));
        assertTrue(nullProfesor.equals(new Profesor()));
    }
}
