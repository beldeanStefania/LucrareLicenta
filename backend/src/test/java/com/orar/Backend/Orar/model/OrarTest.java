package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrarTest {

    private Orar orar;
    private Sala sala;
    private RepartizareProf repartizareProf;

    @BeforeEach
    void setUp() {
        sala = mock(Sala.class);
        repartizareProf = mock(RepartizareProf.class);

        orar = new Orar();
        orar.setId(1);
        orar.setSala(sala);
        orar.setRepartizareProf(repartizareProf);
        orar.setFormatia("FAF2022");
        orar.setOraInceput(10);
        orar.setOraSfarsit(12);
        orar.setGrupa("231");
        orar.setZi("Luni");
        orar.setFrecventa("saptamanal");
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, orar.getId());
        assertEquals(sala, orar.getSala());
        assertEquals(repartizareProf, orar.getRepartizareProf());
        assertEquals("FAF2022", orar.getFormatia());
        assertEquals(10, orar.getOraInceput());
        assertEquals(12, orar.getOraSfarsit());
        assertEquals("231", orar.getGrupa());
        assertEquals("Luni", orar.getZi());
        assertEquals("saptamanal", orar.getFrecventa());
    }

    @Test
    void testSettersWithNullValues() {
        orar.setId(null);
        orar.setSala(null);
        orar.setRepartizareProf(null);
        orar.setFormatia(null);
        orar.setGrupa(null);
        orar.setZi(null);
        orar.setFrecventa(null);

        assertNull(orar.getId());
        assertNull(orar.getSala());
        assertNull(orar.getRepartizareProf());
        assertNull(orar.getFormatia());
        assertNull(orar.getGrupa());
        assertNull(orar.getZi());
        assertNull(orar.getFrecventa());
    }

    @Test
    void testSettersWithEmptyStrings() {
        orar.setFormatia("");
        orar.setGrupa("");
        orar.setZi("");
        orar.setFrecventa("");

        assertEquals("", orar.getFormatia());
        assertEquals("", orar.getGrupa());
        assertEquals("", orar.getZi());
        assertEquals("", orar.getFrecventa());
    }

    @Test
    void testSetIdSpecifically() {
        orar.setId(999);
        assertEquals(999, orar.getId());

        orar.setId(0);
        assertEquals(0, orar.getId());

        orar.setId(-1);
        assertEquals(-1, orar.getId());
    }

    @Test
    void testSetOraIncepetAndSfarsitEdgeCases() {
        // Test valid hour ranges
        orar.setOraInceput(8);
        orar.setOraSfarsit(20);
        assertEquals(8, orar.getOraInceput());
        assertEquals(20, orar.getOraSfarsit());

        // Test edge cases
        orar.setOraInceput(0);
        orar.setOraSfarsit(23);
        assertEquals(0, orar.getOraInceput());
        assertEquals(23, orar.getOraSfarsit());

        // Test negative values (shouldn't normally happen but test edge)
        orar.setOraInceput(-1);
        orar.setOraSfarsit(25);
        assertEquals(-1, orar.getOraInceput());
        assertEquals(25, orar.getOraSfarsit());
    }

    @Test
    void testSetSala() {
        Sala newSala = mock(Sala.class);
        orar.setSala(newSala);
        assertEquals(newSala, orar.getSala());
    }

    @Test
    void testSetRepartizareProf() {
        RepartizareProf newProf = mock(RepartizareProf.class);
        orar.setRepartizareProf(newProf);
        assertEquals(newProf, orar.getRepartizareProf());
    }

    @Test
    void testSetFormatiaVariousFormats() {
        String[] formatiFormats = {
                "FAF2022", "FI2023", "CTI2021", "IA2024",
                "MATEMATICA2022", "FIZICA2023", "CHIMIE2021"
        };

        for (String format : formatiFormats) {
            orar.setFormatia(format);
            assertEquals(format, orar.getFormatia());
        }
    }

    @Test
    void testSetGrupaVariousFormats() {
        String[] grupaFormats = {
                "231", "232A", "233B", "211/1", "211/2",
                "M1", "M2", "D1", "ID1"
        };

        for (String grupa : grupaFormats) {
            orar.setGrupa(grupa);
            assertEquals(grupa, orar.getGrupa());
        }
    }

    @Test
    void testSetZiAllDays() {
        String[] zile = {
                "Luni", "Marti", "Miercuri", "Joi", "Vineri", "Sambata", "Duminica"
        };

        for (String zi : zile) {
            orar.setZi(zi);
            assertEquals(zi, orar.getZi());
        }
    }

    @Test
    void testSetFrecventaTypes() {
        String[] frecvente = {
                "saptamanal", "o data la 2 saptamani", "lunar",
                "zilnic", "optional", "blocuri"
        };

        for (String frecventa : frecvente) {
            orar.setFrecventa(frecventa);
            assertEquals(frecventa, orar.getFrecventa());
        }
    }

    @Test
    void testEqualsAndHashCode() {
        Orar o1 = new Orar();
        o1.setId(10);
        Orar o2 = new Orar();
        o2.setId(10);

        // Since no fields are explicitly included in equals/hashCode, all instances are equal
        assertEquals(o1, o2);
        assertEquals(o1.hashCode(), o2.hashCode());
    }

    @Test
    void testEqualsSameObject() {
        assertEquals(orar, orar);
        assertTrue(orar.equals(orar));
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(orar, null);
        assertFalse(orar.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        String differentObject = "not an orar";
        assertNotEquals(orar, differentObject);
        assertFalse(orar.equals(differentObject));
    }

    @Test
    void testEqualsWithDifferentId() {
        Orar o1 = new Orar();
        o1.setId(1);

        Orar o2 = new Orar();
        o2.setId(2);

        // Since no fields are explicitly included in equals/hashCode, all instances are equal
        assertEquals(o1, o2);
        assertEquals(o1.hashCode(), o2.hashCode());
    }

    @Test
    void testEqualsWithSameIdDifferentFields() {
        Orar o1 = new Orar();
        o1.setId(1);
        o1.setFormatia("FAF2022");
        o1.setZi("Luni");

        Orar o2 = new Orar();
        o2.setId(1);
        o2.setFormatia("FI2023");
        o2.setZi("Marti");

        // Should be equal because @EqualsAndHashCode(onlyExplicitlyIncluded = true) with no explicitly included fields
        assertEquals(o1, o2);
        assertEquals(o1.hashCode(), o2.hashCode());
    }

    @Test
    void testEqualsWithBothNullIds() {
        Orar o1 = new Orar();
        o1.setId(null);

        Orar o2 = new Orar();
        o2.setId(null);

        assertEquals(o1, o2);
        assertEquals(o1.hashCode(), o2.hashCode());
    }

    @Test
    void testEqualsWithOneNullId() {
        Orar o1 = new Orar();
        o1.setId(1);

        Orar o2 = new Orar();
        o2.setId(null);

        // Since no fields are explicitly included in equals/hashCode, all instances are equal
        assertEquals(o1, o2);
        assertEquals(o1.hashCode(), o2.hashCode());
    }

    @Test
    void testHashCodeConsistency() {
        int initialHashCode = orar.hashCode();

        // Hash code should remain consistent across multiple calls
        assertEquals(initialHashCode, orar.hashCode());
        assertEquals(initialHashCode, orar.hashCode());

        // Changing fields should not affect hash code (due to @EqualsAndHashCode configuration)
        orar.setFormatia("changedFormatia");
        orar.setZi("changedZi");
        assertEquals(initialHashCode, orar.hashCode());
    }

    @Test
    void testHashCodeWithIdChange() {
        int initialHashCode = orar.hashCode();

        orar.setId(999);
        int newHashCode = orar.hashCode();

        // Since no fields are explicitly included in equals/hashCode, hashCode is constant
        assertEquals(initialHashCode, newHashCode);
    }

    @Test
    void testHashCodeWithNullId() {
        Orar o = new Orar();
        o.setId(null);
        int hashCode = o.hashCode();

        // Should not throw exception and should be consistent
        assertEquals(hashCode, o.hashCode());
    }

    @Test
    void testCanEqualMethod() {
        Orar o1 = new Orar();
        Orar o2 = new Orar();

        assertTrue(o1.canEqual(o2));
        assertTrue(o2.canEqual(o1));

        String notOrar = "not an orar";
        assertFalse(o1.canEqual(notOrar));
    }

    @Test
    void testDefaultConstructor() {
        Orar newOrar = new Orar();

        assertNull(newOrar.getId());
        assertNull(newOrar.getSala());
        assertNull(newOrar.getRepartizareProf());
        assertNull(newOrar.getFormatia());
        assertEquals(0, newOrar.getOraInceput()); // int default
        assertEquals(0, newOrar.getOraSfarsit()); // int default
        assertNull(newOrar.getGrupa());
        assertNull(newOrar.getZi());
        assertNull(newOrar.getFrecventa());
    }

    @Test
    void testStringFieldsWithSpecialCharacters() {
        orar.setFormatia("FAF-2022/A");
        orar.setGrupa("231/A-B");
        orar.setZi("Luni-Marti");
        orar.setFrecventa("o dată la 2 săptămâni");

        assertEquals("FAF-2022/A", orar.getFormatia());
        assertEquals("231/A-B", orar.getGrupa());
        assertEquals("Luni-Marti", orar.getZi());
        assertEquals("o dată la 2 săptămâni", orar.getFrecventa());
    }

    @Test
    void testStringFieldsWithUnicodeCharacters() {
        orar.setFormatia("INFORMATICĂ2022");
        orar.setGrupa("GRUPĂ231");
        orar.setZi("Mércuri");
        orar.setFrecventa("săptămânal");

        assertEquals("INFORMATICĂ2022", orar.getFormatia());
        assertEquals("GRUPĂ231", orar.getGrupa());
        assertEquals("Mércuri", orar.getZi());
        assertEquals("săptămânal", orar.getFrecventa());
    }

    @Test
    void testLongStringFields() {
        String longFormatia = "Foarte foarte lungă denumire de formație care depășește limitele normale";
        String longGrupa = "Foarte foarte lungă denumire de grupă care depășește limitele normale";
        String longZi = "Foarte foarte lungă denumire de zi care depășește limitele normale";
        String longFrecventa = "Foarte foarte lungă descriere a frecvenței care depășește limitele normale";

        orar.setFormatia(longFormatia);
        orar.setGrupa(longGrupa);
        orar.setZi(longZi);
        orar.setFrecventa(longFrecventa);

        assertEquals(longFormatia, orar.getFormatia());
        assertEquals(longGrupa, orar.getGrupa());
        assertEquals(longZi, orar.getZi());
        assertEquals(longFrecventa, orar.getFrecventa());
    }

    @Test
    void testCompleteOrarSetup() {
        Sala newSala = mock(Sala.class);
        RepartizareProf newRepartizare = mock(RepartizareProf.class);

        Orar completeOrar = new Orar();
        completeOrar.setId(200);
        completeOrar.setSala(newSala);
        completeOrar.setRepartizareProf(newRepartizare);
        completeOrar.setFormatia("CTI2024");
        completeOrar.setOraInceput(14);
        completeOrar.setOraSfarsit(16);
        completeOrar.setGrupa("241");
        completeOrar.setZi("Miercuri");
        completeOrar.setFrecventa("saptamanal");

        // Verify all fields are set correctly
        assertEquals(200, completeOrar.getId());
        assertEquals(newSala, completeOrar.getSala());
        assertEquals(newRepartizare, completeOrar.getRepartizareProf());
        assertEquals("CTI2024", completeOrar.getFormatia());
        assertEquals(14, completeOrar.getOraInceput());
        assertEquals(16, completeOrar.getOraSfarsit());
        assertEquals("241", completeOrar.getGrupa());
        assertEquals("Miercuri", completeOrar.getZi());
        assertEquals("saptamanal", completeOrar.getFrecventa());
    }

    @Test
    void testToStringNotNull() {
        assertNotNull(orar.toString());

        Orar emptyOrar = new Orar();
        assertNotNull(emptyOrar.toString());
    }

    @Test
    void testBusinessLogicScenarios() {
        // Test typical university schedule scenarios

        // Morning class
        orar.setOraInceput(8);
        orar.setOraSfarsit(10);
        orar.setZi("Luni");
        assertTrue(orar.getOraInceput() < orar.getOraSfarsit());

        // Afternoon class
        orar.setOraInceput(14);
        orar.setOraSfarsit(16);
        orar.setZi("Miercuri");
        assertTrue(orar.getOraInceput() < orar.getOraSfarsit());

        // Evening class
        orar.setOraInceput(18);
        orar.setOraSfarsit(20);
        orar.setZi("Vineri");
        assertTrue(orar.getOraInceput() < orar.getOraSfarsit());
    }

    @Test
    void testTypicalSchedulePatterns() {
        // Test common schedule patterns
        String[][] schedulePatterns = {
                {"FAF2022", "231", "Luni", "saptamanal"},
                {"FI2023", "232A", "Marti", "o data la 2 saptamani"},
                {"CTI2021", "233B", "Miercuri", "saptamanal"},
                {"IA2024", "234", "Joi", "lunar"},
                {"MATE2022", "M1", "Vineri", "saptamanal"}
        };

        for (String[] pattern : schedulePatterns) {
            orar.setFormatia(pattern[0]);
            orar.setGrupa(pattern[1]);
            orar.setZi(pattern[2]);
            orar.setFrecventa(pattern[3]);

            assertEquals(pattern[0], orar.getFormatia());
            assertEquals(pattern[1], orar.getGrupa());
            assertEquals(pattern[2], orar.getZi());
            assertEquals(pattern[3], orar.getFrecventa());
        }
    }

    @Test
    void testHourRangeValidation() {
        // Test various hour combinations
        int[][] hourCombinations = {
                {8, 10}, {10, 12}, {12, 14}, {14, 16}, {16, 18}, {18, 20}
        };

        for (int[] hours : hourCombinations) {
            orar.setOraInceput(hours[0]);
            orar.setOraSfarsit(hours[1]);

            assertEquals(hours[0], orar.getOraInceput());
            assertEquals(hours[1], orar.getOraSfarsit());
            assertTrue(orar.getOraInceput() < orar.getOraSfarsit());
        }
    }

    @Test
    void testNullSafetyOperations() {
        Orar nullOrar = new Orar();

        // Should not throw exceptions
        assertNotNull(nullOrar.toString());

        int hashCode1 = nullOrar.hashCode();
        int hashCode2 = nullOrar.hashCode();
        assertEquals(hashCode1, hashCode2);

        assertFalse(nullOrar.equals(null));
        assertTrue(nullOrar.equals(new Orar()));
    }

    @Test
    void testPrimitiveIntDefaults() {
        Orar newOrar = new Orar();

        // Test that primitive ints have default value 0
        assertEquals(0, newOrar.getOraInceput());
        assertEquals(0, newOrar.getOraSfarsit());

        // Test setting to max and min values
        newOrar.setOraInceput(Integer.MAX_VALUE);
        newOrar.setOraSfarsit(Integer.MIN_VALUE);

        assertEquals(Integer.MAX_VALUE, newOrar.getOraInceput());
        assertEquals(Integer.MIN_VALUE, newOrar.getOraSfarsit());
    }
}
