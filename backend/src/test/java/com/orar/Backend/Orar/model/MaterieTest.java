package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MaterieTest {

    private Materie materie;
    private CatalogStudentMaterie catalog1, catalog2;
    private RepartizareProf repartizare1, repartizare2;

    @BeforeEach
    void setUp() {
        catalog1 = mock(CatalogStudentMaterie.class);
        catalog2 = mock(CatalogStudentMaterie.class);
        repartizare1 = mock(RepartizareProf.class);
        repartizare2 = mock(RepartizareProf.class);

        materie = new Materie();
        materie.setId(1);
        materie.setNume("Programare Orientată pe Obiecte");
        materie.setSemestru(3);
        materie.setAn(2);
        materie.setCod("POO001");
        materie.setCredite(6);
        materie.setCatalogStudentMaterie(List.of(catalog1, catalog2));
        materie.setRepartizareProfs(List.of(repartizare1, repartizare2));
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, materie.getId());
        assertEquals("Programare Orientată pe Obiecte", materie.getNume());
        assertEquals(3, materie.getSemestru());
        assertEquals(2, materie.getAn());
        assertEquals("POO001", materie.getCod());
        assertEquals(6, materie.getCredite());
        assertEquals(2, materie.getCatalogStudentMaterie().size());
        assertEquals(2, materie.getRepartizareProfs().size());
    }

    @Test
    void testSettersWithNullValues() {
        materie.setId(null);
        materie.setNume(null);
        materie.setSemestru(null);
        materie.setAn(null);
        materie.setCod(null);
        materie.setCredite(null);
        materie.setCatalogStudentMaterie(null);
        materie.setRepartizareProfs(null);

        assertNull(materie.getId());
        assertNull(materie.getNume());
        assertNull(materie.getSemestru());
        assertNull(materie.getAn());
        assertNull(materie.getCod());
        assertNull(materie.getCredite());
        assertNull(materie.getCatalogStudentMaterie());
        assertNull(materie.getRepartizareProfs());
    }

    @Test
    void testSettersWithEmptyStrings() {
        materie.setNume("");
        materie.setCod("");

        assertEquals("", materie.getNume());
        assertEquals("", materie.getCod());
    }

    @Test
    void testSetIdSpecifically() {
        materie.setId(999);
        assertEquals(999, materie.getId());

        materie.setId(0);
        assertEquals(0, materie.getId());

        materie.setId(-1);
        assertEquals(-1, materie.getId());
    }

    @Test
    void testSemestru() {
        // Test valid semesters
        materie.setSemestru(1);
        assertEquals(1, materie.getSemestru());

        materie.setSemestru(2);
        assertEquals(2, materie.getSemestru());

        // Test edge cases
        materie.setSemestru(0);
        assertEquals(0, materie.getSemestru());

        materie.setSemestru(-1);
        assertEquals(-1, materie.getSemestru());
    }

    @Test
    void testAn() {
        // Test valid years
        materie.setAn(1);
        assertEquals(1, materie.getAn());

        materie.setAn(4);
        assertEquals(4, materie.getAn());

        // Test edge cases
        materie.setAn(0);
        assertEquals(0, materie.getAn());

        materie.setAn(-1);
        assertEquals(-1, materie.getAn());
    }

    @Test
    void testCredite() {
        // Test typical credit values
        int[] creditValues = {1, 2, 3, 4, 5, 6, 7, 8, 10};

        for (int credit : creditValues) {
            materie.setCredite(credit);
            assertEquals(credit, materie.getCredite());
        }

        // Test edge cases
        materie.setCredite(0);
        assertEquals(0, materie.getCredite());

        materie.setCredite(-1);
        assertEquals(-1, materie.getCredite());
    }

    @Test
    void testCatalogStudentMaterieListManipulation() {
        List<CatalogStudentMaterie> newCatalog = new ArrayList<>();
        newCatalog.add(catalog1);

        materie.setCatalogStudentMaterie(newCatalog);
        assertEquals(1, materie.getCatalogStudentMaterie().size());
        assertTrue(materie.getCatalogStudentMaterie().contains(catalog1));

        // Test empty list
        materie.setCatalogStudentMaterie(new ArrayList<>());
        assertTrue(materie.getCatalogStudentMaterie().isEmpty());
    }

    @Test
    void testRepartizareProfListManipulation() {
        List<RepartizareProf> newRepartizari = new ArrayList<>();
        newRepartizari.add(repartizare1);

        materie.setRepartizareProfs(newRepartizari);
        assertEquals(1, materie.getRepartizareProfs().size());
        assertTrue(materie.getRepartizareProfs().contains(repartizare1));

        // Test empty list
        materie.setRepartizareProfs(new ArrayList<>());
        assertTrue(materie.getRepartizareProfs().isEmpty());
    }

    @Test
    void testEqualsAndHashCode() {
        Materie m1 = new Materie();
        m1.setId(1);
        m1.setCod("M001");

        Materie m2 = new Materie();
        m2.setId(1);
        m2.setCod("M002"); // cod diferit, dar id egal

        Materie m3 = new Materie();
        m3.setId(2);
        m3.setCod("M001");

        // Compară după id → m1 și m2 sunt egale
        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());

        // id diferit → nu sunt egale
        assertNotEquals(m1, m3);
        assertNotEquals(m1.hashCode(), m3.hashCode());
    }

    @Test
    void testEqualsSameObject() {
        assertEquals(materie, materie);
        assertTrue(materie.equals(materie));
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(materie, null);
        assertFalse(materie.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        String differentObject = "not a materie";
        assertNotEquals(materie, differentObject);
        assertFalse(materie.equals(differentObject));
    }

    @Test
    void testEqualsWithSameIdDifferentFields() {
        Materie m1 = new Materie();
        m1.setId(1);
        m1.setNume("Matematică");
        m1.setCod("MAT001");

        Materie m2 = new Materie();
        m2.setId(1);
        m2.setNume("Fizică");
        m2.setCod("FIZ001");

        // Should be equal because only id is included in equals/hashCode
        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    void testEqualsWithBothNullIds() {
        Materie m1 = new Materie();
        m1.setId(null);

        Materie m2 = new Materie();
        m2.setId(null);

        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    void testEqualsWithOneNullId() {
        Materie m1 = new Materie();
        m1.setId(1);

        Materie m2 = new Materie();
        m2.setId(null);

        assertNotEquals(m1, m2);
        assertNotEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    void testHashCodeConsistency() {
        int initialHashCode = materie.hashCode();

        // Hash code should remain consistent across multiple calls
        assertEquals(initialHashCode, materie.hashCode());
        assertEquals(initialHashCode, materie.hashCode());

        // Changing non-included fields should not affect hash code
        materie.setNume("changedNume");
        materie.setCod("changedCod");
        assertEquals(initialHashCode, materie.hashCode());
    }

    @Test
    void testHashCodeWithIdChange() {
        int initialHashCode = materie.hashCode();

        materie.setId(999);
        int newHashCode = materie.hashCode();

        // Since id is included in equals/hashCode, hashCode should change
        assertNotEquals(initialHashCode, newHashCode);
    }

    @Test
    void testHashCodeWithNullId() {
        Materie m = new Materie();
        m.setId(null);
        int hashCode = m.hashCode();

        // Should not throw exception and should be consistent
        assertEquals(hashCode, m.hashCode());
    }

    @Test
    void testCanEqualMethod() {
        Materie m1 = new Materie();
        Materie m2 = new Materie();

        assertTrue(m1.canEqual(m2));
        assertTrue(m2.canEqual(m1));

        String notMaterie = "not a materie";
        assertFalse(m1.canEqual(notMaterie));
    }

    @Test
    void testDefaultConstructor() {
        Materie newMaterie = new Materie();

        assertNull(newMaterie.getId());
        assertNull(newMaterie.getNume());
        assertNull(newMaterie.getSemestru());
        assertNull(newMaterie.getAn());
        assertNull(newMaterie.getCod());
        assertNull(newMaterie.getCredite());
        assertNull(newMaterie.getCatalogStudentMaterie());
        assertNull(newMaterie.getRepartizareProfs());
    }

    @Test
    void testAllArgsConstructor() {
        List<CatalogStudentMaterie> catalogList = List.of(catalog1, catalog2);
        List<RepartizareProf> repartizareList = List.of(repartizare1, repartizare2);

        Materie fullMaterie = new Materie(
                100,
                "Algoritmi și Structuri de Date",
                4,
                2,
                "ASD001",
                7,
                catalogList,
                repartizareList
        );

        assertEquals(100, fullMaterie.getId());
        assertEquals("Algoritmi și Structuri de Date", fullMaterie.getNume());
        assertEquals(4, fullMaterie.getSemestru());
        assertEquals(2, fullMaterie.getAn());
        assertEquals("ASD001", fullMaterie.getCod());
        assertEquals(7, fullMaterie.getCredite());
        assertEquals(2, fullMaterie.getCatalogStudentMaterie().size());
        assertEquals(2, fullMaterie.getRepartizareProfs().size());
    }

    @Test
    void testMaterieNameWithSpecialCharacters() {
        materie.setNume("Programare în C++");
        assertEquals("Programare în C++", materie.getNume());

        materie.setNume("AI & Machine Learning");
        assertEquals("AI & Machine Learning", materie.getNume());

        materie.setNume("Web Dev (HTML/CSS/JS)");
        assertEquals("Web Dev (HTML/CSS/JS)", materie.getNume());
    }

    @Test
    void testMaterieNameWithUnicodeCharacters() {
        materie.setNume("Programare în Limbajul Român");
        assertEquals("Programare în Limbajul Român", materie.getNume());

        materie.setNume("Inteligență Artificială");
        assertEquals("Inteligență Artificială", materie.getNume());

        materie.setNume("Învățare Automată");
        assertEquals("Învățare Automată", materie.getNume());
    }

    @Test
    void testCodWithVariousFormats() {
        String[] codFormats = {
                "POO001", "MAT101", "FIZ201", "BIO301",
                "CHEM401", "ENG501", "ROM601", "HIS701"
        };

        for (String cod : codFormats) {
            materie.setCod(cod);
            assertEquals(cod, materie.getCod());
        }
    }

    @Test
    void testLongMateriiName() {
        String longName = "Foarte foarte foarte lungă denumire de materie care depășește limitele normale și include multe cuvinte pentru testare completă a sistemului";
        materie.setNume(longName);
        assertEquals(longName, materie.getNume());
    }

    @Test
    void testCompleteMaterieSetup() {
        List<CatalogStudentMaterie> newCatalog = List.of(
                mock(CatalogStudentMaterie.class),
                mock(CatalogStudentMaterie.class),
                mock(CatalogStudentMaterie.class)
        );

        List<RepartizareProf> newRepartizari = List.of(
                mock(RepartizareProf.class),
                mock(RepartizareProf.class)
        );

        Materie completeMaterie = new Materie();
        completeMaterie.setId(200);
        completeMaterie.setNume("Sisteme de Operare");
        completeMaterie.setSemestru(5);
        completeMaterie.setAn(3);
        completeMaterie.setCod("SO001");
        completeMaterie.setCredite(8);
        completeMaterie.setCatalogStudentMaterie(newCatalog);
        completeMaterie.setRepartizareProfs(newRepartizari);

        // Verify all fields are set correctly
        assertEquals(200, completeMaterie.getId());
        assertEquals("Sisteme de Operare", completeMaterie.getNume());
        assertEquals(5, completeMaterie.getSemestru());
        assertEquals(3, completeMaterie.getAn());
        assertEquals("SO001", completeMaterie.getCod());
        assertEquals(8, completeMaterie.getCredite());
        assertEquals(3, completeMaterie.getCatalogStudentMaterie().size());
        assertEquals(2, completeMaterie.getRepartizareProfs().size());
    }

    @Test
    void testToStringNotNull() {
        assertNotNull(materie.toString());

        Materie emptyMaterie = new Materie();
        assertNotNull(emptyMaterie.toString());
    }

    @Test
    void testMaterieTypicalUseCases() {
        // Test common university subjects
        String[][] subjectData = {
                {"Programare Orientată pe Obiecte", "POO001", "6"},
                {"Algoritmi și Structuri de Date", "ASD001", "7"},
                {"Baze de Date", "BD001", "5"},
                {"Sisteme de Operare", "SO001", "8"},
                {"Rețele de Calculatoare", "RC001", "6"},
                {"Inteligență Artificială", "IA001", "7"},
                {"Ingineria Programării", "IP001", "6"}
        };

        for (String[] subject : subjectData) {
            materie.setNume(subject[0]);
            materie.setCod(subject[1]);
            materie.setCredite(Integer.parseInt(subject[2]));

            assertEquals(subject[0], materie.getNume());
            assertEquals(subject[1], materie.getCod());
            assertEquals(Integer.parseInt(subject[2]), materie.getCredite());
        }
    }

    @Test
    void testCatalogWithLargeNumberOfElements() {
        // Test with large catalog list
        List<CatalogStudentMaterie> largeCatalog = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            largeCatalog.add(mock(CatalogStudentMaterie.class));
        }

        materie.setCatalogStudentMaterie(largeCatalog);
        assertEquals(100, materie.getCatalogStudentMaterie().size());
    }

    @Test
    void testRepartizareWithLargeNumberOfElements() {
        // Test with large repartizare list
        List<RepartizareProf> largeRepartizari = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            largeRepartizari.add(mock(RepartizareProf.class));
        }

        materie.setRepartizareProfs(largeRepartizari);
        assertEquals(50, materie.getRepartizareProfs().size());
    }

    @Test
    void testBusinessLogicScenarios() {
        // Test typical curriculum scenarios

        // First year subject
        materie.setNume("Introducere în Programare");
        materie.setAn(1);
        materie.setSemestru(1);
        materie.setCredite(6);
        materie.setCod("IP001");

        assertEquals("Introducere în Programare", materie.getNume());
        assertEquals(1, materie.getAn());
        assertEquals(1, materie.getSemestru());
        assertEquals(6, materie.getCredite());
        assertEquals("IP001", materie.getCod());

        // Advanced subject
        materie.setNume("Machine Learning Avansat");
        materie.setAn(4);
        materie.setSemestru(7);
        materie.setCredite(8);
        materie.setCod("MLA001");

        assertEquals("Machine Learning Avansat", materie.getNume());
        assertEquals(4, materie.getAn());
        assertEquals(7, materie.getSemestru());
        assertEquals(8, materie.getCredite());
        assertEquals("MLA001", materie.getCod());
    }

    @Test
    void testNullSafetyOperations() {
        Materie nullMaterie = new Materie();

        // Should not throw exceptions
        assertNotNull(nullMaterie.toString());

        int hashCode1 = nullMaterie.hashCode();
        int hashCode2 = nullMaterie.hashCode();
        assertEquals(hashCode1, hashCode2);

        assertFalse(nullMaterie.equals(null));
        assertTrue(nullMaterie.equals(new Materie()));
    }

    @Test
    void testIdBasedEquality() {
        // Test the specific behavior of id-based equality
        Materie m1 = new Materie();
        Materie m2 = new Materie();
        Materie m3 = new Materie();

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
        m1.setCod("SUBA001");
        m2.setNume("Subject B");
        m2.setCod("SUBB001");

        // Should still be equal because only id matters
        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    void testIntegerFieldTypes() {
        Materie newMaterie = new Materie();

        // Test that numeric fields are Integer (not int), so they can be null
        assertNull(newMaterie.getId());
        assertNull(newMaterie.getSemestru());
        assertNull(newMaterie.getAn());
        assertNull(newMaterie.getCredite());

        newMaterie.setId(Integer.MAX_VALUE);
        newMaterie.setSemestru(Integer.MAX_VALUE);
        newMaterie.setAn(Integer.MAX_VALUE);
        newMaterie.setCredite(Integer.MAX_VALUE);

        assertEquals(Integer.MAX_VALUE, newMaterie.getId());
        assertEquals(Integer.MAX_VALUE, newMaterie.getSemestru());
        assertEquals(Integer.MAX_VALUE, newMaterie.getAn());
        assertEquals(Integer.MAX_VALUE, newMaterie.getCredite());

        newMaterie.setId(Integer.MIN_VALUE);
        newMaterie.setSemestru(Integer.MIN_VALUE);
        newMaterie.setAn(Integer.MIN_VALUE);
        newMaterie.setCredite(Integer.MIN_VALUE);

        assertEquals(Integer.MIN_VALUE, newMaterie.getId());
        assertEquals(Integer.MIN_VALUE, newMaterie.getSemestru());
        assertEquals(Integer.MIN_VALUE, newMaterie.getAn());
        assertEquals(Integer.MIN_VALUE, newMaterie.getCredite());
    }
}
