package com.orar.Backend.Orar.model;

import com.orar.Backend.Orar.enums.Tip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurriculumEntryTest {

    private CurriculumEntry curriculumEntry;
    private Specializare specializare;
    private Materie materie;
    private MateriiOptionale optionale;

    @BeforeEach
    void setUp() {
        specializare = mock(Specializare.class);
        materie = mock(Materie.class);
        optionale = mock(MateriiOptionale.class);

        curriculumEntry = new CurriculumEntry();
        curriculumEntry.setId(1);
        curriculumEntry.setSpecializare(specializare);
        curriculumEntry.setMaterie(materie);
        curriculumEntry.setAn(2);
        curriculumEntry.setSemestru(3);
        curriculumEntry.setOptionale(optionale);
        curriculumEntry.setTip(Tip.OBLIGATORIE);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, curriculumEntry.getId());
        assertEquals(specializare, curriculumEntry.getSpecializare());
        assertEquals(materie, curriculumEntry.getMaterie());
        assertEquals(2, curriculumEntry.getAn());
        assertEquals(3, curriculumEntry.getSemestru());
        assertEquals(optionale, curriculumEntry.getOptionale());
        assertEquals(Tip.OBLIGATORIE, curriculumEntry.getTip());
    }

    @Test
    void testSettersWithNullValues() {
        curriculumEntry.setId(null);
        curriculumEntry.setSpecializare(null);
        curriculumEntry.setMaterie(null);
        curriculumEntry.setAn(null);
        curriculumEntry.setSemestru(null);
        curriculumEntry.setOptionale(null);
        curriculumEntry.setTip(null);

        assertNull(curriculumEntry.getId());
        assertNull(curriculumEntry.getSpecializare());
        assertNull(curriculumEntry.getMaterie());
        assertNull(curriculumEntry.getAn());
        assertNull(curriculumEntry.getSemestru());
        assertNull(curriculumEntry.getOptionale());
        assertNull(curriculumEntry.getTip());
    }

    @Test
    void testSetIdSpecifically() {
        curriculumEntry.setId(999);
        assertEquals(999, curriculumEntry.getId());

        curriculumEntry.setId(0);
        assertEquals(0, curriculumEntry.getId());

        curriculumEntry.setId(-1);
        assertEquals(-1, curriculumEntry.getId());
    }

    @Test
    void testAn() {
        // Test valid years
        curriculumEntry.setAn(1);
        assertEquals(1, curriculumEntry.getAn());

        curriculumEntry.setAn(4);
        assertEquals(4, curriculumEntry.getAn());

        // Test edge cases
        curriculumEntry.setAn(0);
        assertEquals(0, curriculumEntry.getAn());

        curriculumEntry.setAn(-1);
        assertEquals(-1, curriculumEntry.getAn());
    }

    @Test
    void testSemestru() {
        // Test valid semesters
        curriculumEntry.setSemestru(1);
        assertEquals(1, curriculumEntry.getSemestru());

        curriculumEntry.setSemestru(2);
        assertEquals(2, curriculumEntry.getSemestru());

        // Test edge cases
        curriculumEntry.setSemestru(0);
        assertEquals(0, curriculumEntry.getSemestru());

        curriculumEntry.setSemestru(-1);
        assertEquals(-1, curriculumEntry.getSemestru());
    }

    @Test
    void testAllTipValues() {
        // Test all enum values
        curriculumEntry.setTip(Tip.OBLIGATORIE);
        assertEquals(Tip.OBLIGATORIE, curriculumEntry.getTip());

        curriculumEntry.setTip(Tip.OPTIONALA);
        assertEquals(Tip.OPTIONALA, curriculumEntry.getTip());

        curriculumEntry.setTip(Tip.FACULTATIVA);
        assertEquals(Tip.FACULTATIVA, curriculumEntry.getTip());
    }

    @Test
    void testEqualsAndHashCode() {
        CurriculumEntry c1 = new CurriculumEntry();
        c1.setId(1);
        c1.setAn(1);
        c1.setSemestru(1);
        c1.setTip(Tip.OBLIGATORIE);

        CurriculumEntry c2 = new CurriculumEntry();
        c2.setId(1);
        c2.setAn(2);
        c2.setSemestru(2);
        c2.setTip(Tip.FACULTATIVA);

        CurriculumEntry c3 = new CurriculumEntry();
        c3.setId(2);

        assertEquals(c1, c2); // id egal ⇒ obiectele sunt considerate egale
        assertEquals(c1.hashCode(), c2.hashCode());

        assertNotEquals(c1, c3);
        assertNotEquals(c1.hashCode(), c3.hashCode());
    }

    @Test
    void testEqualsSameObject() {
        assertEquals(curriculumEntry, curriculumEntry);
        assertTrue(curriculumEntry.equals(curriculumEntry));
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(curriculumEntry, null);
        assertFalse(curriculumEntry.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        String differentObject = "not a curriculum entry";
        assertNotEquals(curriculumEntry, differentObject);
        assertFalse(curriculumEntry.equals(differentObject));
    }

    @Test
    void testEqualsWithSameIdDifferentFields() {
        CurriculumEntry c1 = new CurriculumEntry();
        c1.setId(1);
        c1.setAn(1);
        c1.setTip(Tip.OBLIGATORIE);

        CurriculumEntry c2 = new CurriculumEntry();
        c2.setId(1);
        c2.setAn(3);
        c2.setTip(Tip.FACULTATIVA);

        // Should be equal because only id is included in equals/hashCode
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testEqualsWithBothNullIds() {
        CurriculumEntry c1 = new CurriculumEntry();
        c1.setId(null);

        CurriculumEntry c2 = new CurriculumEntry();
        c2.setId(null);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testEqualsWithOneNullId() {
        CurriculumEntry c1 = new CurriculumEntry();
        c1.setId(1);

        CurriculumEntry c2 = new CurriculumEntry();
        c2.setId(null);

        assertNotEquals(c1, c2);
        assertNotEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testHashCodeConsistency() {
        int initialHashCode = curriculumEntry.hashCode();

        // Hash code should remain consistent across multiple calls
        assertEquals(initialHashCode, curriculumEntry.hashCode());
        assertEquals(initialHashCode, curriculumEntry.hashCode());

        // Changing non-included fields should not affect hash code
        curriculumEntry.setAn(99);
        curriculumEntry.setTip(Tip.FACULTATIVA);
        assertEquals(initialHashCode, curriculumEntry.hashCode());
    }

    @Test
    void testHashCodeWithIdChange() {
        int initialHashCode = curriculumEntry.hashCode();

        curriculumEntry.setId(999);
        int newHashCode = curriculumEntry.hashCode();

        // Since id is included in equals/hashCode, hashCode should change
        assertNotEquals(initialHashCode, newHashCode);
    }

    @Test
    void testHashCodeWithNullId() {
        CurriculumEntry c = new CurriculumEntry();
        c.setId(null);
        int hashCode = c.hashCode();

        // Should not throw exception and should be consistent
        assertEquals(hashCode, c.hashCode());
    }

    @Test
    void testCanEqualMethod() {
        CurriculumEntry c1 = new CurriculumEntry();
        CurriculumEntry c2 = new CurriculumEntry();

        assertTrue(c1.canEqual(c2));
        assertTrue(c2.canEqual(c1));

        String notCurriculumEntry = "not a curriculum entry";
        assertFalse(c1.canEqual(notCurriculumEntry));
    }

    @Test
    void testDefaultConstructor() {
        CurriculumEntry newEntry = new CurriculumEntry();

        assertNull(newEntry.getId());
        assertNull(newEntry.getSpecializare());
        assertNull(newEntry.getMaterie());
        assertNull(newEntry.getAn());
        assertNull(newEntry.getSemestru());
        assertNull(newEntry.getOptionale());
        assertNull(newEntry.getTip());
    }

    @Test
    void testSettersAndGetters() {
        CurriculumEntry entry = new CurriculumEntry();

        Specializare newSpecializare = mock(Specializare.class);
        Materie newMaterie = mock(Materie.class);
        MateriiOptionale newOptionale = mock(MateriiOptionale.class);

        entry.setId(100);
        entry.setAn(3);
        entry.setSemestru(2);
        entry.setTip(Tip.OPTIONALA);
        entry.setSpecializare(newSpecializare);
        entry.setMaterie(newMaterie);
        entry.setOptionale(newOptionale);

        assertEquals(100, entry.getId());
        assertEquals(3, entry.getAn());
        assertEquals(2, entry.getSemestru());
        assertEquals(Tip.OPTIONALA, entry.getTip());
        assertSame(newSpecializare, entry.getSpecializare());
        assertSame(newMaterie, entry.getMaterie());
        assertSame(newOptionale, entry.getOptionale());
    }

    @Test
    void testSetSpecializare() {
        Specializare newSpecializare = mock(Specializare.class);
        curriculumEntry.setSpecializare(newSpecializare);
        assertEquals(newSpecializare, curriculumEntry.getSpecializare());
    }

    @Test
    void testSetMaterie() {
        Materie newMaterie = mock(Materie.class);
        curriculumEntry.setMaterie(newMaterie);
        assertEquals(newMaterie, curriculumEntry.getMaterie());
    }

    @Test
    void testSetOptionale() {
        MateriiOptionale newOptionale = mock(MateriiOptionale.class);
        curriculumEntry.setOptionale(newOptionale);
        assertEquals(newOptionale, curriculumEntry.getOptionale());
    }

    @Test
    void testCompleteCurriculumEntrySetup() {
        Specializare newSpecializare = mock(Specializare.class);
        Materie newMaterie = mock(Materie.class);
        MateriiOptionale newOptionale = mock(MateriiOptionale.class);

        CurriculumEntry completeEntry = new CurriculumEntry();
        completeEntry.setId(200);
        completeEntry.setSpecializare(newSpecializare);
        completeEntry.setMaterie(newMaterie);
        completeEntry.setAn(3);
        completeEntry.setSemestru(5);
        completeEntry.setOptionale(newOptionale);
        completeEntry.setTip(Tip.OPTIONALA);

        // Verify all fields are set correctly
        assertEquals(200, completeEntry.getId());
        assertEquals(newSpecializare, completeEntry.getSpecializare());
        assertEquals(newMaterie, completeEntry.getMaterie());
        assertEquals(3, completeEntry.getAn());
        assertEquals(5, completeEntry.getSemestru());
        assertEquals(newOptionale, completeEntry.getOptionale());
        assertEquals(Tip.OPTIONALA, completeEntry.getTip());
    }

    @Test
    void testToStringNotNull() {
        assertNotNull(curriculumEntry.toString());

        CurriculumEntry emptyEntry = new CurriculumEntry();
        assertNotNull(emptyEntry.toString());
    }

    @Test
    void testBusinessLogicScenarios() {
        // Test typical curriculum scenarios

        // Mandatory first year subject
        curriculumEntry.setAn(1);
        curriculumEntry.setSemestru(1);
        curriculumEntry.setTip(Tip.OBLIGATORIE);
        curriculumEntry.setOptionale(null); // Mandatory subjects don't have optional reference

        assertEquals(1, curriculumEntry.getAn());
        assertEquals(1, curriculumEntry.getSemestru());
        assertEquals(Tip.OBLIGATORIE, curriculumEntry.getTip());
        assertNull(curriculumEntry.getOptionale());

        // Optional third year subject
        curriculumEntry.setAn(3);
        curriculumEntry.setSemestru(6);
        curriculumEntry.setTip(Tip.OPTIONALA);
        curriculumEntry.setOptionale(optionale);

        assertEquals(3, curriculumEntry.getAn());
        assertEquals(6, curriculumEntry.getSemestru());
        assertEquals(Tip.OPTIONALA, curriculumEntry.getTip());
        assertEquals(optionale, curriculumEntry.getOptionale());

        // Elective fourth year subject
        curriculumEntry.setAn(4);
        curriculumEntry.setSemestru(8);
        curriculumEntry.setTip(Tip.FACULTATIVA);

        assertEquals(4, curriculumEntry.getAn());
        assertEquals(8, curriculumEntry.getSemestru());
        assertEquals(Tip.FACULTATIVA, curriculumEntry.getTip());
    }

    @Test
    void testTypicalCurriculumPatterns() {
        // Test common curriculum patterns
        Object[][] curriculumPatterns = {
                {1, 1, Tip.OBLIGATORIE, false}, // First year, first semester, mandatory, no optional
                {1, 2, Tip.OBLIGATORIE, false}, // First year, second semester, mandatory, no optional
                {2, 3, Tip.OBLIGATORIE, false}, // Second year, first semester, mandatory, no optional
                {3, 5, Tip.OPTIONALA, true},    // Third year, first semester, optional, has optional reference
                {3, 6, Tip.OPTIONALA, true},    // Third year, second semester, optional, has optional reference
                {4, 7, Tip.FACULTATIVA, false}, // Fourth year, first semester, elective, no optional
                {4, 8, Tip.FACULTATIVA, false}  // Fourth year, second semester, elective, no optional
        };

        for (Object[] pattern : curriculumPatterns) {
            curriculumEntry.setAn((Integer) pattern[0]);
            curriculumEntry.setSemestru((Integer) pattern[1]);
            curriculumEntry.setTip((Tip) pattern[2]);

            if ((Boolean) pattern[3]) {
                curriculumEntry.setOptionale(optionale);
            } else {
                curriculumEntry.setOptionale(null);
            }

            assertEquals(pattern[0], curriculumEntry.getAn());
            assertEquals(pattern[1], curriculumEntry.getSemestru());
            assertEquals(pattern[2], curriculumEntry.getTip());

            if ((Boolean) pattern[3]) {
                assertEquals(optionale, curriculumEntry.getOptionale());
            } else {
                assertNull(curriculumEntry.getOptionale());
            }
        }
    }

    @Test
    void testNullSafetyOperations() {
        CurriculumEntry nullEntry = new CurriculumEntry();

        // Should not throw exceptions
        assertNotNull(nullEntry.toString());

        int hashCode1 = nullEntry.hashCode();
        int hashCode2 = nullEntry.hashCode();
        assertEquals(hashCode1, hashCode2);

        assertFalse(nullEntry.equals(null));
        assertTrue(nullEntry.equals(new CurriculumEntry()));
    }

    @Test
    void testIdBasedEquality() {
        // Test the specific behavior of id-based equality
        CurriculumEntry c1 = new CurriculumEntry();
        CurriculumEntry c2 = new CurriculumEntry();
        CurriculumEntry c3 = new CurriculumEntry();

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
        c1.setAn(1);
        c1.setTip(Tip.OBLIGATORIE);
        c2.setAn(4);
        c2.setTip(Tip.FACULTATIVA);

        // Should still be equal because only id matters
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testIntegerFieldTypes() {
        CurriculumEntry newEntry = new CurriculumEntry();

        // Test that numeric fields are Integer (not int), so they can be null
        assertNull(newEntry.getId());
        assertNull(newEntry.getAn());
        assertNull(newEntry.getSemestru());

        newEntry.setId(Integer.MAX_VALUE);
        newEntry.setAn(Integer.MAX_VALUE);
        newEntry.setSemestru(Integer.MAX_VALUE);

        assertEquals(Integer.MAX_VALUE, newEntry.getId());
        assertEquals(Integer.MAX_VALUE, newEntry.getAn());
        assertEquals(Integer.MAX_VALUE, newEntry.getSemestru());

        newEntry.setId(Integer.MIN_VALUE);
        newEntry.setAn(Integer.MIN_VALUE);
        newEntry.setSemestru(Integer.MIN_VALUE);

        assertEquals(Integer.MIN_VALUE, newEntry.getId());
        assertEquals(Integer.MIN_VALUE, newEntry.getAn());
        assertEquals(Integer.MIN_VALUE, newEntry.getSemestru());
    }

    @Test
    void testRelationshipFieldsManagement() {
        // Test managing relationship fields separately
        Specializare spec1 = mock(Specializare.class);
        Specializare spec2 = mock(Specializare.class);

        Materie mat1 = mock(Materie.class);
        Materie mat2 = mock(Materie.class);

        MateriiOptionale opt1 = mock(MateriiOptionale.class);
        MateriiOptionale opt2 = mock(MateriiOptionale.class);

        // Test changing specializare
        curriculumEntry.setSpecializare(spec1);
        assertEquals(spec1, curriculumEntry.getSpecializare());

        curriculumEntry.setSpecializare(spec2);
        assertEquals(spec2, curriculumEntry.getSpecializare());

        // Test changing materie
        curriculumEntry.setMaterie(mat1);
        assertEquals(mat1, curriculumEntry.getMaterie());

        curriculumEntry.setMaterie(mat2);
        assertEquals(mat2, curriculumEntry.getMaterie());

        // Test changing optionale
        curriculumEntry.setOptionale(opt1);
        assertEquals(opt1, curriculumEntry.getOptionale());

        curriculumEntry.setOptionale(opt2);
        assertEquals(opt2, curriculumEntry.getOptionale());
    }

    @Test
    void testEnumHandling() {
        // Test all enum values and transitions
        curriculumEntry.setTip(Tip.OBLIGATORIE);
        assertEquals(Tip.OBLIGATORIE, curriculumEntry.getTip());

        curriculumEntry.setTip(Tip.OPTIONALA);
        assertEquals(Tip.OPTIONALA, curriculumEntry.getTip());

        curriculumEntry.setTip(Tip.FACULTATIVA);
        assertEquals(Tip.FACULTATIVA, curriculumEntry.getTip());

        // Test setting to null
        curriculumEntry.setTip(null);
        assertNull(curriculumEntry.getTip());
    }

    @Test
    void testValidCurriculumStructure() {
        // Test a valid curriculum structure
        curriculumEntry.setId(1);
        curriculumEntry.setSpecializare(specializare);
        curriculumEntry.setMaterie(materie);
        curriculumEntry.setAn(2);
        curriculumEntry.setSemestru(3);
        curriculumEntry.setTip(Tip.OBLIGATORIE);
        curriculumEntry.setOptionale(null); // Mandatory subjects typically don't have optional reference

        // Verify the structure is complete and valid
        assertNotNull(curriculumEntry.getId());
        assertNotNull(curriculumEntry.getSpecializare());
        assertNotNull(curriculumEntry.getMaterie());
        assertNotNull(curriculumEntry.getAn());
        assertNotNull(curriculumEntry.getSemestru());
        assertNotNull(curriculumEntry.getTip());
        assertTrue(curriculumEntry.getAn() > 0);
        assertTrue(curriculumEntry.getSemestru() > 0);
    }
}
