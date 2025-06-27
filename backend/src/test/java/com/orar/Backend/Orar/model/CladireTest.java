package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CladireTest {

    private Cladire cladire;
    private Sala sala1, sala2;

    @BeforeEach
    void setUp() {
        sala1 = mock(Sala.class);
        sala2 = mock(Sala.class);

        cladire = new Cladire();
        cladire.setId(1);
        cladire.setNume("Facultatea de Științe Economice și Gestiunea Afacerilor");
        cladire.setAdresa("Strada Teodor Mihali Nr. 58-60");
        cladire.setSala(List.of(sala1, sala2));
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, cladire.getId());
        assertEquals("Facultatea de Științe Economice și Gestiunea Afacerilor", cladire.getNume());
        assertEquals("Strada Teodor Mihali Nr. 58-60", cladire.getAdresa());
        assertEquals(2, cladire.getSala().size());
        assertTrue(cladire.getSala().contains(sala1));
        assertTrue(cladire.getSala().contains(sala2));
    }

    @Test
    void testSettersWithNullValues() {
        cladire.setId(null);
        cladire.setNume(null);
        cladire.setAdresa(null);
        cladire.setSala(null);

        assertNull(cladire.getId());
        assertNull(cladire.getNume());
        assertNull(cladire.getAdresa());
        assertNull(cladire.getSala());
    }

    @Test
    void testSettersWithEmptyStrings() {
        cladire.setNume("");
        cladire.setAdresa("");

        assertEquals("", cladire.getNume());
        assertEquals("", cladire.getAdresa());
    }

    @Test
    void testSetIdSpecifically() {
        cladire.setId(999);
        assertEquals(999, cladire.getId());

        cladire.setId(0);
        assertEquals(0, cladire.getId());

        cladire.setId(-1);
        assertEquals(-1, cladire.getId());
    }

    @Test
    void testSetSalaMethod() {
        // This test ensures setSala method is called and covered
        List<Sala> newSali = new ArrayList<>();
        newSali.add(sala1);

        cladire.setSala(newSali);
        assertEquals(1, cladire.getSala().size());
        assertTrue(cladire.getSala().contains(sala1));

        // Test empty list
        cladire.setSala(new ArrayList<>());
        assertTrue(cladire.getSala().isEmpty());
    }

    @Test
    void testSalaListManipulation() {
        List<Sala> sali = new ArrayList<>();
        sali.add(sala1);
        sali.add(sala2);

        cladire.setSala(sali);
        assertEquals(2, cladire.getSala().size());
        assertTrue(cladire.getSala().contains(sala1));
        assertTrue(cladire.getSala().contains(sala2));

        // Test with null list
        cladire.setSala(null);
        assertNull(cladire.getSala());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        Cladire cladire = new Cladire();
        cladire.setId(1);
        cladire.setNume("FSEGA");
        cladire.setAdresa("Str. Teodor Mihali");

        assertEquals(1, cladire.getId());
        assertEquals("FSEGA", cladire.getNume());
        assertEquals("Str. Teodor Mihali", cladire.getAdresa());
        assertNull(cladire.getSala()); // Default should be null
    }

    @Test
    void testAllArgsConstructor() {
        Sala sala = mock(Sala.class);
        Cladire cladire = new Cladire(2, "UTCN", "Str. Barițiu", Collections.singletonList(sala));

        assertEquals(2, cladire.getId());
        assertEquals("UTCN", cladire.getNume());
        assertEquals("Str. Barițiu", cladire.getAdresa());
        assertEquals(1, cladire.getSala().size());
        assertTrue(cladire.getSala().contains(sala));
    }

    @Test
    void testParameterizedConstructorWithNullValues() {
        Cladire cladire = new Cladire(null, null, null, null);

        assertNull(cladire.getId());
        assertNull(cladire.getNume());
        assertNull(cladire.getAdresa());
        assertNull(cladire.getSala());
    }

    @Test
    void testEqualsAndHashCode() {
        Cladire c1 = new Cladire();
        c1.setId(100);

        Cladire c2 = new Cladire();
        c2.setId(100);

        Cladire c3 = new Cladire();
        c3.setId(200);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotEquals(c1, c3);
        assertNotEquals(c1.hashCode(), c3.hashCode());
    }

    @Test
    void testEqualsSameObject() {
        assertEquals(cladire, cladire);
        assertTrue(cladire.equals(cladire));
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(cladire, null);
        assertFalse(cladire.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        String differentObject = "not a cladire";
        assertNotEquals(cladire, differentObject);
        assertFalse(cladire.equals(differentObject));
    }

    @Test
    void testEqualsWithSameIdDifferentFields() {
        Cladire c1 = new Cladire();
        c1.setId(1);
        c1.setNume("Cladire A");
        c1.setAdresa("Adresa A");

        Cladire c2 = new Cladire();
        c2.setId(1);
        c2.setNume("Cladire B");
        c2.setAdresa("Adresa B");

        // Should be equal because only id is included in equals/hashCode
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testEqualsWithBothNullIds() {
        Cladire c1 = new Cladire();
        c1.setId(null);

        Cladire c2 = new Cladire();
        c2.setId(null);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testEqualsWithOneNullId() {
        Cladire c1 = new Cladire();
        c1.setId(1);

        Cladire c2 = new Cladire();
        c2.setId(null);

        assertNotEquals(c1, c2);
        assertNotEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testHashCodeConsistency() {
        int initialHashCode = cladire.hashCode();

        // Hash code should remain consistent across multiple calls
        assertEquals(initialHashCode, cladire.hashCode());
        assertEquals(initialHashCode, cladire.hashCode());

        // Changing non-included fields should not affect hash code
        cladire.setNume("changedNume");
        cladire.setAdresa("changedAdresa");
        assertEquals(initialHashCode, cladire.hashCode());
    }

    @Test
    void testHashCodeWithIdChange() {
        int initialHashCode = cladire.hashCode();

        cladire.setId(999);
        int newHashCode = cladire.hashCode();

        // Since id is included in equals/hashCode, hashCode should change
        assertNotEquals(initialHashCode, newHashCode);
    }

    @Test
    void testHashCodeWithNullId() {
        Cladire c = new Cladire();
        c.setId(null);
        int hashCode = c.hashCode();

        // Should not throw exception and should be consistent
        assertEquals(hashCode, c.hashCode());
    }

    @Test
    void testCanEqualMethod() {
        Cladire c1 = new Cladire();
        Cladire c2 = new Cladire();

        assertTrue(c1.canEqual(c2));
        assertTrue(c2.canEqual(c1));

        String notCladire = "not a cladire";
        assertFalse(c1.canEqual(notCladire));
    }

    @Test
    void testDefaultConstructor() {
        Cladire newCladire = new Cladire();

        assertNull(newCladire.getId());
        assertNull(newCladire.getNume());
        assertNull(newCladire.getAdresa());
        assertNull(newCladire.getSala());
    }

    @Test
    void testBuildingNamesWithRomanianCharacters() {
        cladire.setNume("Facultatea de Științe Economice și Gestiunea Afacerilor");
        assertEquals("Facultatea de Științe Economice și Gestiunea Afacerilor", cladire.getNume());

        cladire.setNume("Facultatea de Calculatoare și Informatică");
        assertEquals("Facultatea de Calculatoare și Informatică", cladire.getNume());

        cladire.setNume("Facultatea de Arhitectură și Urbanism");
        assertEquals("Facultatea de Arhitectură și Urbanism", cladire.getNume());
    }

    @Test
    void testAddressesWithRomanianFormat() {
        cladire.setAdresa("Strada Teodor Mihali Nr. 58-60, Cluj-Napoca");
        assertEquals("Strada Teodor Mihali Nr. 58-60, Cluj-Napoca", cladire.getAdresa());

        cladire.setAdresa("Bulevardul Muncii Nr. 103-105, Cluj-Napoca");
        assertEquals("Bulevardul Muncii Nr. 103-105, Cluj-Napoca", cladire.getAdresa());

        cladire.setAdresa("Piața Victoriei Nr. 15, Cluj-Napoca");
        assertEquals("Piața Victoriei Nr. 15, Cluj-Napoca", cladire.getAdresa());
    }

    @Test
    void testBuildingNamesWithSpecialCharacters() {
        cladire.setNume("FSEGA - Facultatea de Științe Economice & Gestiunea Afacerilor");
        assertEquals("FSEGA - Facultatea de Științe Economice & Gestiunea Afacerilor", cladire.getNume());

        cladire.setNume("UTCN (Universitatea Tehnică Cluj-Napoca)");
        assertEquals("UTCN (Universitatea Tehnică Cluj-Napoca)", cladire.getNume());
    }

    @Test
    void testLongBuildingNames() {
        String longName = "Foarte foarte foarte lungă denumire de clădire universitară care include multe informații despre facultate și departamente";
        cladire.setNume(longName);
        assertEquals(longName, cladire.getNume());
    }

    @Test
    void testCompleteCladireSetup() {
        List<Sala> sali = List.of(
                mock(Sala.class),
                mock(Sala.class),
                mock(Sala.class),
                mock(Sala.class),
                mock(Sala.class)
        );

        Cladire completeCladire = new Cladire();
        completeCladire.setId(100);
        completeCladire.setNume("Facultatea de Automatică și Calculatoare");
        completeCladire.setAdresa("Strada Memorandumului Nr. 28, Cluj-Napoca");
        completeCladire.setSala(sali);

        // Verify all fields are set correctly
        assertEquals(100, completeCladire.getId());
        assertEquals("Facultatea de Automatică și Calculatoare", completeCladire.getNume());
        assertEquals("Strada Memorandumului Nr. 28, Cluj-Napoca", completeCladire.getAdresa());
        assertEquals(5, completeCladire.getSala().size());
    }

    @Test
    void testToStringNotNull() {
        assertNotNull(cladire.toString());

        Cladire emptyCladire = new Cladire();
        assertNotNull(emptyCladire.toString());
    }

    @Test
    void testUniversityBuildingTypicalUseCases() {
        // Test typical Romanian university buildings
        String[][] buildingData = {
                {"FSEGA", "Strada Teodor Mihali Nr. 58-60"},
                {"UTCN", "Bulevardul Muncii Nr. 103-105"},
                {"UBB", "Strada Mihail Kogălniceanu Nr. 1"},
                {"USAMV", "Calea Mănăștur Nr. 3-5"},
                {"UMF", "Strada Victor Babeș Nr. 8"}
        };

        for (String[] building : buildingData) {
            cladire.setNume(building[0]);
            cladire.setAdresa(building[1]);

            assertEquals(building[0], cladire.getNume());
            assertEquals(building[1], cladire.getAdresa());
        }
    }

    @Test
    void testSalaWithLargeNumberOfRooms() {
        // Test with large room list
        List<Sala> largeSali = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            largeSali.add(mock(Sala.class));
        }

        cladire.setSala(largeSali);
        assertEquals(100, cladire.getSala().size());
    }

    @Test
    void testBusinessLogicScenarios() {
        // Test typical university building scenarios

        // Main academic building
        cladire.setNume("Corpul Principal FSEGA");
        cladire.setAdresa("Strada Teodor Mihali Nr. 58-60, Cluj-Napoca");
        List<Sala> saliPrincipale = List.of(sala1, sala2);
        cladire.setSala(saliPrincipale);

        assertEquals("Corpul Principal FSEGA", cladire.getNume());
        assertEquals("Strada Teodor Mihali Nr. 58-60, Cluj-Napoca", cladire.getAdresa());
        assertEquals(2, cladire.getSala().size());

        // Annex building
        cladire.setNume("Anexa FSEGA");
        cladire.setAdresa("Strada Teodor Mihali Nr. 62, Cluj-Napoca");
        List<Sala> saliAnexa = List.of(sala1);
        cladire.setSala(saliAnexa);

        assertEquals("Anexa FSEGA", cladire.getNume());
        assertEquals("Strada Teodor Mihali Nr. 62, Cluj-Napoca", cladire.getAdresa());
        assertEquals(1, cladire.getSala().size());
    }

    @Test
    void testNullSafetyOperations() {
        Cladire nullCladire = new Cladire();

        // Should not throw exceptions
        assertNotNull(nullCladire.toString());

        int hashCode1 = nullCladire.hashCode();
        int hashCode2 = nullCladire.hashCode();
        assertEquals(hashCode1, hashCode2);

        assertFalse(nullCladire.equals(null));
        assertTrue(nullCladire.equals(new Cladire()));
    }

    @Test
    void testIdBasedEquality() {
        // Test the specific behavior of id-based equality
        Cladire c1 = new Cladire();
        Cladire c2 = new Cladire();
        Cladire c3 = new Cladire();

        // Initially all have null ids, so they should be equal
        assertEquals(c1, c2);
        assertEquals(c1, c3);

        // Set same id to c1 and c2
        c1.setId(100);
        c2.setId(100);
        c3.setId(200);

        assertEquals(c1, c2);
        assertNotEquals(c1, c3);
        assertNotEquals(c2, c3);

        // Change other fields but keep same id
        c1.setNume("Building A");
        c1.setAdresa("Address A");
        c2.setNume("Building B");
        c2.setAdresa("Address B");

        // Should still be equal because only id matters
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testIntegerFieldType() {
        Cladire newCladire = new Cladire();

        // Test that id is Integer (not int), so it can be null
        assertNull(newCladire.getId());

        newCladire.setId(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, newCladire.getId());

        newCladire.setId(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, newCladire.getId());

        newCladire.setId(null);
        assertNull(newCladire.getId());
    }

    @Test
    void testRomanianUniversityBuildingNames() {
        // Test authentic Romanian university building names
        String[] romanianBuildings = {
                "Facultatea de Științe Economice și Gestiunea Afacerilor",
                "Facultatea de Automatică și Calculatoare",
                "Facultatea de Inginerie Electrică și Electronică",
                "Facultatea de Construcții Civile",
                "Facultatea de Arhitectură și Urbanism",
                "Facultatea de Biologie și Geologie",
                "Facultatea de Chimie și Inginerie Chimică",
                "Facultatea de Fizică"
        };

        for (String building : romanianBuildings) {
            cladire.setNume(building);
            assertEquals(building, cladire.getNume());
        }
    }

    @Test
    void testRomanianCityAddresses() {
        // Test addresses from various Romanian cities
        String[] romanianAddresses = {
                "Strada Teodor Mihali Nr. 58-60, Cluj-Napoca",
                "Bulevardul Unirii Nr. 15-17, București",
                "Strada Vasile Pârvan Nr. 4, Timișoara",
                "Bulevardul Revoluției Nr. 5, Iași",
                "Strada Universitații Nr. 1, Craiova",
                "Calea Victoriei Nr. 91-93, București"
        };

        for (String address : romanianAddresses) {
            cladire.setAdresa(address);
            assertEquals(address, cladire.getAdresa());
        }
    }

    @Test
    void testCladireValidationScenarios() {
        // Test realistic building validation scenarios

        // Valid building
        Cladire validCladire = new Cladire();
        validCladire.setId(1);
        validCladire.setNume("FSEGA");
        validCladire.setAdresa("Strada Teodor Mihali Nr. 58-60");
        validCladire.setSala(List.of(sala1, sala2));

        assertNotNull(validCladire.getId());
        assertNotNull(validCladire.getNume());
        assertNotNull(validCladire.getAdresa());
        assertNotNull(validCladire.getSala());
        assertFalse(validCladire.getSala().isEmpty());

        // Edge case building
        Cladire edgeCladire = new Cladire();
        edgeCladire.setId(0);
        edgeCladire.setNume("");
        edgeCladire.setAdresa("");
        edgeCladire.setSala(new ArrayList<>());

        assertEquals(0, edgeCladire.getId());
        assertEquals("", edgeCladire.getNume());
        assertEquals("", edgeCladire.getAdresa());
        assertTrue(edgeCladire.getSala().isEmpty());
    }

    @Test
    void testConstructorVariations() {
        // Test all constructor variations

        // Default constructor
        Cladire defaultCladire = new Cladire();
        assertNull(defaultCladire.getId());
        assertNull(defaultCladire.getNume());
        assertNull(defaultCladire.getAdresa());
        assertNull(defaultCladire.getSala());

        // All-args constructor with valid data
        List<Sala> sali = List.of(sala1, sala2);
        Cladire fullCladire = new Cladire(1, "FSEGA", "Str. Mihali", sali);
        assertEquals(1, fullCladire.getId());
        assertEquals("FSEGA", fullCladire.getNume());
        assertEquals("Str. Mihali", fullCladire.getAdresa());
        assertEquals(2, fullCladire.getSala().size());

        // All-args constructor with edge cases
        Cladire edgeCladire = new Cladire(0, "", "", new ArrayList<>());
        assertEquals(0, edgeCladire.getId());
        assertEquals("", edgeCladire.getNume());
        assertEquals("", edgeCladire.getAdresa());
        assertTrue(edgeCladire.getSala().isEmpty());
    }
}
