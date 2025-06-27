package com.orar.Backend.Orar.model;

import com.orar.Backend.Orar.enums.MaterieStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CatalogStudentMaterieTest {

    private CatalogStudentMaterie catalogEntry;
    private Student student;
    private Materie materie;

    @BeforeEach
    void setUp() {
        student = mock(Student.class);
        materie = mock(Materie.class);

        catalogEntry = new CatalogStudentMaterie();
        catalogEntry.setId(1);
        catalogEntry.setNota(8.5);
        catalogEntry.setSemestru(3);
        catalogEntry.setStatus(MaterieStatus.ACTIV);
        catalogEntry.setStudent(student);
        catalogEntry.setMaterie(materie);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, catalogEntry.getId());
        assertEquals(8.5, catalogEntry.getNota());
        assertEquals(3, catalogEntry.getSemestru());
        assertEquals(MaterieStatus.ACTIV, catalogEntry.getStatus());
        assertEquals(student, catalogEntry.getStudent());
        assertEquals(materie, catalogEntry.getMaterie());
    }

    @Test
    void testSettersWithNullValues() {
        catalogEntry.setId(null);
        catalogEntry.setNota(null);
        catalogEntry.setSemestru(null);
        catalogEntry.setStatus(null);
        catalogEntry.setStudent(null);
        catalogEntry.setMaterie(null);

        assertNull(catalogEntry.getId());
        assertNull(catalogEntry.getNota());
        assertNull(catalogEntry.getSemestru());
        assertNull(catalogEntry.getStatus());
        assertNull(catalogEntry.getStudent());
        assertNull(catalogEntry.getMaterie());
    }

    @Test
    void testSetIdSpecifically() {
        catalogEntry.setId(999);
        assertEquals(999, catalogEntry.getId());

        catalogEntry.setId(0);
        assertEquals(0, catalogEntry.getId());

        catalogEntry.setId(-1);
        assertEquals(-1, catalogEntry.getId());
    }

    @Test
    void testNotaWithVariousValues() {
        // Test typical grades
        double[] grades = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0};

        for (double grade : grades) {
            catalogEntry.setNota(grade);
            assertEquals(grade, catalogEntry.getNota());
        }

        // Test decimal grades
        catalogEntry.setNota(7.25);
        assertEquals(7.25, catalogEntry.getNota());

        catalogEntry.setNota(8.75);
        assertEquals(8.75, catalogEntry.getNota());

        catalogEntry.setNota(9.99);
        assertEquals(9.99, catalogEntry.getNota());

        // Test edge cases
        catalogEntry.setNota(0.0);
        assertEquals(0.0, catalogEntry.getNota());

        catalogEntry.setNota(-1.0);
        assertEquals(-1.0, catalogEntry.getNota());
    }

    @Test
    void testSemestru() {
        // Test valid semesters
        catalogEntry.setSemestru(1);
        assertEquals(1, catalogEntry.getSemestru());

        catalogEntry.setSemestru(2);
        assertEquals(2, catalogEntry.getSemestru());

        catalogEntry.setSemestru(8);
        assertEquals(8, catalogEntry.getSemestru());

        // Test edge cases
        catalogEntry.setSemestru(0);
        assertEquals(0, catalogEntry.getSemestru());

        catalogEntry.setSemestru(-1);
        assertEquals(-1, catalogEntry.getSemestru());
    }

    @Test
    void testAllMaterieStatusValues() {
        // Test all enum values
        catalogEntry.setStatus(MaterieStatus.ACTIV);
        assertEquals(MaterieStatus.ACTIV, catalogEntry.getStatus());

        catalogEntry.setStatus(MaterieStatus.FINALIZATA);
        assertEquals(MaterieStatus.FINALIZATA, catalogEntry.getStatus());

        catalogEntry.setStatus(MaterieStatus.PICATA);
        assertEquals(MaterieStatus.PICATA, catalogEntry.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        CatalogStudentMaterie entry = new CatalogStudentMaterie();
        Student student = mock(Student.class);
        Materie materie = mock(Materie.class);

        entry.setId(1);
        entry.setNota(9.75);
        entry.setSemestru(2);
        entry.setStatus(MaterieStatus.FINALIZATA);
        entry.setStudent(student);
        entry.setMaterie(materie);

        assertEquals(1, entry.getId());
        assertEquals(9.75, entry.getNota());
        assertEquals(2, entry.getSemestru());
        assertEquals(MaterieStatus.FINALIZATA, entry.getStatus());
        assertSame(student, entry.getStudent());
        assertSame(materie, entry.getMaterie());
    }

    @Test
    void testEqualsAndHashCode() {
        CatalogStudentMaterie c1 = new CatalogStudentMaterie();
        c1.setId(100);

        CatalogStudentMaterie c2 = new CatalogStudentMaterie();
        c2.setId(100);

        CatalogStudentMaterie c3 = new CatalogStudentMaterie();
        c3.setId(200);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotEquals(c1, c3);
        assertNotEquals(c1.hashCode(), c3.hashCode());
    }

    @Test
    void testEqualsSameObject() {
        assertEquals(catalogEntry, catalogEntry);
        assertTrue(catalogEntry.equals(catalogEntry));
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(catalogEntry, null);
        assertFalse(catalogEntry.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        String differentObject = "not a catalog entry";
        assertNotEquals(catalogEntry, differentObject);
        assertFalse(catalogEntry.equals(differentObject));
    }

    @Test
    void testEqualsWithSameIdDifferentFields() {
        CatalogStudentMaterie c1 = new CatalogStudentMaterie();
        c1.setId(1);
        c1.setNota(8.0);
        c1.setStatus(MaterieStatus.FINALIZATA);

        CatalogStudentMaterie c2 = new CatalogStudentMaterie();
        c2.setId(1);
        c2.setNota(6.0);
        c2.setStatus(MaterieStatus.ACTIV);

        // Should be equal because only id is included in equals/hashCode
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testEqualsWithBothNullIds() {
        CatalogStudentMaterie c1 = new CatalogStudentMaterie();
        c1.setId(null);

        CatalogStudentMaterie c2 = new CatalogStudentMaterie();
        c2.setId(null);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testEqualsWithOneNullId() {
        CatalogStudentMaterie c1 = new CatalogStudentMaterie();
        c1.setId(1);

        CatalogStudentMaterie c2 = new CatalogStudentMaterie();
        c2.setId(null);

        assertNotEquals(c1, c2);
        assertNotEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testHashCodeConsistency() {
        int initialHashCode = catalogEntry.hashCode();

        // Hash code should remain consistent across multiple calls
        assertEquals(initialHashCode, catalogEntry.hashCode());
        assertEquals(initialHashCode, catalogEntry.hashCode());

        // Changing non-included fields should not affect hash code
        catalogEntry.setNota(10.0);
        catalogEntry.setStatus(MaterieStatus.FINALIZATA);
        assertEquals(initialHashCode, catalogEntry.hashCode());
    }

    @Test
    void testHashCodeWithIdChange() {
        int initialHashCode = catalogEntry.hashCode();

        catalogEntry.setId(999);
        int newHashCode = catalogEntry.hashCode();

        // Since id is included in equals/hashCode, hashCode should change
        assertNotEquals(initialHashCode, newHashCode);
    }

    @Test
    void testHashCodeWithNullId() {
        CatalogStudentMaterie c = new CatalogStudentMaterie();
        c.setId(null);
        int hashCode = c.hashCode();

        // Should not throw exception and should be consistent
        assertEquals(hashCode, c.hashCode());
    }

    @Test
    void testCanEqualMethod() {
        CatalogStudentMaterie c1 = new CatalogStudentMaterie();
        CatalogStudentMaterie c2 = new CatalogStudentMaterie();

        assertTrue(c1.canEqual(c2));
        assertTrue(c2.canEqual(c1));

        String notCatalogEntry = "not a catalog entry";
        assertFalse(c1.canEqual(notCatalogEntry));
    }

    @Test
    void testDefaultConstructor() {
        CatalogStudentMaterie newEntry = new CatalogStudentMaterie();

        assertNull(newEntry.getId());
        assertNull(newEntry.getNota());
        assertNull(newEntry.getSemestru());
        assertNull(newEntry.getStatus());
        assertNull(newEntry.getStudent());
        assertNull(newEntry.getMaterie());
    }

    @Test
    void testSetStudent() {
        Student newStudent = mock(Student.class);
        catalogEntry.setStudent(newStudent);
        assertEquals(newStudent, catalogEntry.getStudent());
    }

    @Test
    void testSetMaterie() {
        Materie newMaterie = mock(Materie.class);
        catalogEntry.setMaterie(newMaterie);
        assertEquals(newMaterie, catalogEntry.getMaterie());
    }

    @Test
    void testCompleteCatalogEntrySetup() {
        Student newStudent = mock(Student.class);
        Materie newMaterie = mock(Materie.class);

        CatalogStudentMaterie completeEntry = new CatalogStudentMaterie();
        completeEntry.setId(200);
        completeEntry.setNota(9.25);
        completeEntry.setSemestru(5);
        completeEntry.setStatus(MaterieStatus.FINALIZATA);
        completeEntry.setStudent(newStudent);
        completeEntry.setMaterie(newMaterie);

        // Verify all fields are set correctly
        assertEquals(200, completeEntry.getId());
        assertEquals(9.25, completeEntry.getNota());
        assertEquals(5, completeEntry.getSemestru());
        assertEquals(MaterieStatus.FINALIZATA, completeEntry.getStatus());
        assertEquals(newStudent, completeEntry.getStudent());
        assertEquals(newMaterie, completeEntry.getMaterie());
    }

    @Test
    void testToStringNotNull() {
        assertNotNull(catalogEntry.toString());

        CatalogStudentMaterie emptyEntry = new CatalogStudentMaterie();
        assertNotNull(emptyEntry.toString());
    }

    @Test
    void testBusinessLogicScenarios() {
        // Test typical academic catalog scenarios

        // Successful course completion
        catalogEntry.setNota(8.75);
        catalogEntry.setSemestru(2);
        catalogEntry.setStatus(MaterieStatus.FINALIZATA);

        assertEquals(8.75, catalogEntry.getNota());
        assertEquals(2, catalogEntry.getSemestru());
        assertEquals(MaterieStatus.FINALIZATA, catalogEntry.getStatus());
        assertTrue(catalogEntry.getNota() >= 5.0); // Passing grade

        // Failed course
        catalogEntry.setNota(3.5);
        catalogEntry.setSemestru(3);
        catalogEntry.setStatus(MaterieStatus.PICATA);

        assertEquals(3.5, catalogEntry.getNota());
        assertEquals(3, catalogEntry.getSemestru());
        assertEquals(MaterieStatus.PICATA, catalogEntry.getStatus());
        assertTrue(catalogEntry.getNota() < 5.0); // Failing grade

        // Course in progress
        catalogEntry.setNota(null); // No grade yet
        catalogEntry.setSemestru(4);
        catalogEntry.setStatus(MaterieStatus.ACTIV);

        assertNull(catalogEntry.getNota());
        assertEquals(4, catalogEntry.getSemestru());
        assertEquals(MaterieStatus.ACTIV, catalogEntry.getStatus());
    }

    @Test
    void testTypicalGradingPatterns() {
        // Test common Romanian grading patterns
        Object[][] gradingData = {
                {10.0, MaterieStatus.FINALIZATA, "Excellent"},
                {9.5, MaterieStatus.FINALIZATA, "Very Good"},
                {8.0, MaterieStatus.FINALIZATA, "Good"},
                {7.0, MaterieStatus.FINALIZATA, "Satisfactory"},
                {6.0, MaterieStatus.FINALIZATA, "Sufficient"},
                {5.0, MaterieStatus.FINALIZATA, "Minimum Pass"},
                {4.0, MaterieStatus.PICATA, "Fail"},
                {null, MaterieStatus.ACTIV, "In Progress"}
        };

        for (Object[] gradeData : gradingData) {
            catalogEntry.setNota((Double) gradeData[0]);
            catalogEntry.setStatus((MaterieStatus) gradeData[1]);

            assertEquals(gradeData[0], catalogEntry.getNota());
            assertEquals(gradeData[1], catalogEntry.getStatus());
        }
    }

    @Test
    void testNullSafetyOperations() {
        CatalogStudentMaterie nullEntry = new CatalogStudentMaterie();

        // Should not throw exceptions
        assertNotNull(nullEntry.toString());

        int hashCode1 = nullEntry.hashCode();
        int hashCode2 = nullEntry.hashCode();
        assertEquals(hashCode1, hashCode2);

        assertFalse(nullEntry.equals(null));
        assertTrue(nullEntry.equals(new CatalogStudentMaterie()));
    }

    @Test
    void testIdBasedEquality() {
        // Test the specific behavior of id-based equality
        CatalogStudentMaterie c1 = new CatalogStudentMaterie();
        CatalogStudentMaterie c2 = new CatalogStudentMaterie();
        CatalogStudentMaterie c3 = new CatalogStudentMaterie();

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
        c1.setNota(8.0);
        c1.setStatus(MaterieStatus.FINALIZATA);
        c2.setNota(6.0);
        c2.setStatus(MaterieStatus.ACTIV);

        // Should still be equal because only id matters
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testIntegerAndDoubleFieldTypes() {
        CatalogStudentMaterie newEntry = new CatalogStudentMaterie();

        // Test that numeric fields are nullable wrappers
        assertNull(newEntry.getId());
        assertNull(newEntry.getNota());
        assertNull(newEntry.getSemestru());

        newEntry.setId(Integer.MAX_VALUE);
        newEntry.setNota(Double.MAX_VALUE);
        newEntry.setSemestru(Integer.MAX_VALUE);

        assertEquals(Integer.MAX_VALUE, newEntry.getId());
        assertEquals(Double.MAX_VALUE, newEntry.getNota());
        assertEquals(Integer.MAX_VALUE, newEntry.getSemestru());

        newEntry.setId(Integer.MIN_VALUE);
        newEntry.setNota(Double.MIN_VALUE);
        newEntry.setSemestru(Integer.MIN_VALUE);

        assertEquals(Integer.MIN_VALUE, newEntry.getId());
        assertEquals(Double.MIN_VALUE, newEntry.getNota());
        assertEquals(Integer.MIN_VALUE, newEntry.getSemestru());
    }

    @Test
    void testRelationshipFieldsManagement() {
        // Test managing relationship fields separately
        Student stud1 = mock(Student.class);
        Student stud2 = mock(Student.class);

        Materie mat1 = mock(Materie.class);
        Materie mat2 = mock(Materie.class);

        // Test changing student
        catalogEntry.setStudent(stud1);
        assertEquals(stud1, catalogEntry.getStudent());

        catalogEntry.setStudent(stud2);
        assertEquals(stud2, catalogEntry.getStudent());

        // Test changing materie
        catalogEntry.setMaterie(mat1);
        assertEquals(mat1, catalogEntry.getMaterie());

        catalogEntry.setMaterie(mat2);
        assertEquals(mat2, catalogEntry.getMaterie());
    }

    @Test
    void testEnumStatusHandling() {
        // Test all enum values and transitions
        catalogEntry.setStatus(MaterieStatus.ACTIV);
        assertEquals(MaterieStatus.ACTIV, catalogEntry.getStatus());

        catalogEntry.setStatus(MaterieStatus.FINALIZATA);
        assertEquals(MaterieStatus.FINALIZATA, catalogEntry.getStatus());

        catalogEntry.setStatus(MaterieStatus.PICATA);
        assertEquals(MaterieStatus.PICATA, catalogEntry.getStatus());

        // Test setting to null
        catalogEntry.setStatus(null);
        assertNull(catalogEntry.getStatus());
    }

    @Test
    void testValidCatalogStructure() {
        // Test a valid catalog structure
        catalogEntry.setId(1);
        catalogEntry.setNota(8.5);
        catalogEntry.setSemestru(3);
        catalogEntry.setStatus(MaterieStatus.FINALIZATA);
        catalogEntry.setStudent(student);
        catalogEntry.setMaterie(materie);

        // Verify the structure is complete and valid
        assertNotNull(catalogEntry.getId());
        assertNotNull(catalogEntry.getNota());
        assertNotNull(catalogEntry.getSemestru());
        assertNotNull(catalogEntry.getStatus());
        assertNotNull(catalogEntry.getStudent());
        assertNotNull(catalogEntry.getMaterie());
        assertTrue(catalogEntry.getNota() >= 1.0 && catalogEntry.getNota() <= 10.0);
        assertTrue(catalogEntry.getSemestru() > 0);
    }

    @Test
    void testAcademicProgressTracking() {
        // Test different academic progress scenarios

        // First semester enrollment - active course
        catalogEntry.setNota(null);
        catalogEntry.setSemestru(1);
        catalogEntry.setStatus(MaterieStatus.ACTIV);

        assertNull(catalogEntry.getNota());
        assertEquals(1, catalogEntry.getSemestru());
        assertEquals(MaterieStatus.ACTIV, catalogEntry.getStatus());

        // Midterm progress - still active
        catalogEntry.setNota(null);
        catalogEntry.setSemestru(1);
        catalogEntry.setStatus(MaterieStatus.ACTIV);

        assertNull(catalogEntry.getNota());
        assertEquals(MaterieStatus.ACTIV, catalogEntry.getStatus());

        // Final grade - course completed
        catalogEntry.setNota(8.75);
        catalogEntry.setSemestru(1);
        catalogEntry.setStatus(MaterieStatus.FINALIZATA);

        assertEquals(8.75, catalogEntry.getNota());
        assertEquals(MaterieStatus.FINALIZATA, catalogEntry.getStatus());
    }

    @Test
    void testRomanianGradingScaleValidation() {
        // Test Romanian academic grading scale (1-10)
        double[] validGrades = {5.0, 5.5, 6.0, 6.5, 7.0, 7.5, 8.0, 8.5, 9.0, 9.5, 10.0};

        for (double grade : validGrades) {
            catalogEntry.setNota(grade);
            assertEquals(grade, catalogEntry.getNota());
            assertTrue(grade >= 5.0, "Grade should be passing (>=5.0)");
        }

        double[] failingGrades = {1.0, 2.0, 3.0, 4.0, 4.9};

        for (double grade : failingGrades) {
            catalogEntry.setNota(grade);
            assertEquals(grade, catalogEntry.getNota());
            assertTrue(grade < 5.0, "Grade should be failing (<5.0)");
        }
    }

    @Test
    void testCatalogEntryValidationScenarios() {
        // Test realistic catalog validation scenarios

        // Valid completed entry
        CatalogStudentMaterie validEntry = new CatalogStudentMaterie();
        validEntry.setId(1);
        validEntry.setNota(8.5);
        validEntry.setSemestru(2);
        validEntry.setStatus(MaterieStatus.FINALIZATA);
        validEntry.setStudent(student);
        validEntry.setMaterie(materie);

        assertNotNull(validEntry.getId());
        assertNotNull(validEntry.getNota());
        assertNotNull(validEntry.getSemestru());
        assertEquals(MaterieStatus.FINALIZATA, validEntry.getStatus());
        assertNotNull(validEntry.getStudent());
        assertNotNull(validEntry.getMaterie());

        // Edge case entry
        CatalogStudentMaterie edgeEntry = new CatalogStudentMaterie();
        edgeEntry.setId(0);
        edgeEntry.setNota(0.0);
        edgeEntry.setSemestru(0);
        edgeEntry.setStatus(MaterieStatus.ACTIV);
        edgeEntry.setStudent(null);
        edgeEntry.setMaterie(null);

        assertEquals(0, edgeEntry.getId());
        assertEquals(0.0, edgeEntry.getNota());
        assertEquals(0, edgeEntry.getSemestru());
        assertEquals(MaterieStatus.ACTIV, edgeEntry.getStatus());
        assertNull(edgeEntry.getStudent());
        assertNull(edgeEntry.getMaterie());
    }

    @Test
    void testDoubleNotaPrecision() {
        // Test precise grade handling
        double preciseGrade = 8.756789;
        catalogEntry.setNota(preciseGrade);
        assertEquals(preciseGrade, catalogEntry.getNota());

        // Test that precision is maintained
        assertEquals(8.756789, catalogEntry.getNota(), 0.000001);
    }

    @Test
    void testSemesterValidationEdgeCases() {
        // Test typical Romanian academic semesters (1-8 for 4-year program)
        for (int sem = 1; sem <= 8; sem++) {
            catalogEntry.setSemestru(sem);
            assertEquals(sem, catalogEntry.getSemestru());
        }

        // Test edge case semesters
        catalogEntry.setSemestru(0);
        assertEquals(0, catalogEntry.getSemestru());

        catalogEntry.setSemestru(-1);
        assertEquals(-1, catalogEntry.getSemestru());

        catalogEntry.setSemestru(10); // Graduate programs
        assertEquals(10, catalogEntry.getSemestru());
    }

    @Test
    void testMaterieStatusScenarios() {
        // Test realistic status scenarios based on actual enum values

        // Active course (student enrolled, no grade yet)
        catalogEntry.setNota(null);
        catalogEntry.setStatus(MaterieStatus.ACTIV);

        assertNull(catalogEntry.getNota());
        assertEquals(MaterieStatus.ACTIV, catalogEntry.getStatus());

        // Course completed successfully
        catalogEntry.setNota(8.5);
        catalogEntry.setStatus(MaterieStatus.FINALIZATA);

        assertEquals(8.5, catalogEntry.getNota());
        assertEquals(MaterieStatus.FINALIZATA, catalogEntry.getStatus());
        assertTrue(catalogEntry.getNota() >= 5.0); // Passing grade

        // Course failed
        catalogEntry.setNota(3.0);
        catalogEntry.setStatus(MaterieStatus.PICATA);

        assertEquals(3.0, catalogEntry.getNota());
        assertEquals(MaterieStatus.PICATA, catalogEntry.getStatus());
        assertTrue(catalogEntry.getNota() < 5.0); // Failing grade
    }

    @Test
    void testGradeAndStatusCorrelation() {
        // Test logical correlation between grades and status

        // High grade with completed status
        catalogEntry.setNota(9.5);
        catalogEntry.setStatus(MaterieStatus.FINALIZATA);

        assertEquals(9.5, catalogEntry.getNota());
        assertEquals(MaterieStatus.FINALIZATA, catalogEntry.getStatus());
        assertTrue(catalogEntry.getNota() >= 5.0);

        // Low grade with failed status
        catalogEntry.setNota(2.5);
        catalogEntry.setStatus(MaterieStatus.PICATA);

        assertEquals(2.5, catalogEntry.getNota());
        assertEquals(MaterieStatus.PICATA, catalogEntry.getStatus());
        assertTrue(catalogEntry.getNota() < 5.0);

        // No grade with active status
        catalogEntry.setNota(null);
        catalogEntry.setStatus(MaterieStatus.ACTIV);

        assertNull(catalogEntry.getNota());
        assertEquals(MaterieStatus.ACTIV, catalogEntry.getStatus());
    }

    @Test
    void testCompleteAcademicLifecycle() {
        // Test complete academic lifecycle of a course

        // 1. Course enrollment
        catalogEntry.setNota(null);
        catalogEntry.setStatus(MaterieStatus.ACTIV);
        catalogEntry.setSemestru(1);

        assertNull(catalogEntry.getNota());
        assertEquals(MaterieStatus.ACTIV, catalogEntry.getStatus());
        assertEquals(1, catalogEntry.getSemestru());

        // 2. Course completion with passing grade
        catalogEntry.setNota(8.0);
        catalogEntry.setStatus(MaterieStatus.FINALIZATA);

        assertEquals(8.0, catalogEntry.getNota());
        assertEquals(MaterieStatus.FINALIZATA, catalogEntry.getStatus());

        // Alternative: Course failure
        CatalogStudentMaterie failedEntry = new CatalogStudentMaterie();
        failedEntry.setNota(4.0);
        failedEntry.setStatus(MaterieStatus.PICATA);
        failedEntry.setSemestru(1);

        assertEquals(4.0, failedEntry.getNota());
        assertEquals(MaterieStatus.PICATA, failedEntry.getStatus());
        assertEquals(1, failedEntry.getSemestru());
    }
}
