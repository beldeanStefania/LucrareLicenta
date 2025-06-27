package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContractTest {

    private Contract contract;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        testDateTime = LocalDateTime.of(2024, 6, 27, 15, 30, 0);

        contract = new Contract();
        contract.setId(1L);
        contract.setStudentCod("STUD123");
        contract.setAnContract(2);
        contract.setDataGenerare(testDateTime);
        contract.setCoduriMaterii(Arrays.asList("MAT001", "INF002", "POO003"));
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, contract.getId());
        assertEquals("STUD123", contract.getStudentCod());
        assertEquals(2, contract.getAnContract());
        assertEquals(testDateTime, contract.getDataGenerare());
        assertEquals(3, contract.getCoduriMaterii().size());
        assertTrue(contract.getCoduriMaterii().contains("MAT001"));
        assertTrue(contract.getCoduriMaterii().contains("INF002"));
        assertTrue(contract.getCoduriMaterii().contains("POO003"));
    }

    @Test
    void testSettersWithNullValues() {
        contract.setId(null);
        contract.setStudentCod(null);
        contract.setDataGenerare(null);
        contract.setCoduriMaterii(null);

        assertNull(contract.getId());
        assertNull(contract.getStudentCod());
        assertNull(contract.getDataGenerare());
        assertNull(contract.getCoduriMaterii());
    }

    @Test
    void testSettersWithEmptyStrings() {
        contract.setStudentCod("");
        assertEquals("", contract.getStudentCod());
    }

    @Test
    void testSetIdSpecifically() {
        contract.setId(999L);
        assertEquals(999L, contract.getId());

        contract.setId(0L);
        assertEquals(0L, contract.getId());

        contract.setId(-1L);
        assertEquals(-1L, contract.getId());
    }

    @Test
    void testAnContract() {
        // Test valid contract years
        contract.setAnContract(1);
        assertEquals(1, contract.getAnContract());

        contract.setAnContract(4);
        assertEquals(4, contract.getAnContract());

        // Test edge cases
        contract.setAnContract(0);
        assertEquals(0, contract.getAnContract());

        contract.setAnContract(-1);
        assertEquals(-1, contract.getAnContract());

        contract.setAnContract(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, contract.getAnContract());

        contract.setAnContract(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, contract.getAnContract());
    }

    @Test
    void testCoduriMateriiListManipulation() {
        List<String> newMaterii = new ArrayList<>();
        newMaterii.add("BD001");
        newMaterii.add("SO002");

        contract.setCoduriMaterii(newMaterii);
        assertEquals(2, contract.getCoduriMaterii().size());
        assertTrue(contract.getCoduriMaterii().contains("BD001"));
        assertTrue(contract.getCoduriMaterii().contains("SO002"));

        // Test empty list
        contract.setCoduriMaterii(new ArrayList<>());
        assertTrue(contract.getCoduriMaterii().isEmpty());
    }

    @Test
    void testLocalDateTimeHandling() {
        LocalDateTime now = LocalDateTime.now();
        contract.setDataGenerare(now);
        assertEquals(now, contract.getDataGenerare());

        LocalDateTime pastDate = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
        contract.setDataGenerare(pastDate);
        assertEquals(pastDate, contract.getDataGenerare());

        LocalDateTime futureDate = LocalDateTime.of(2030, 12, 31, 23, 59, 59);
        contract.setDataGenerare(futureDate);
        assertEquals(futureDate, contract.getDataGenerare());
    }

    @Test
    void testConstructorAndGetters() {
        Contract contract = new Contract("STUD123", 2);

        assertEquals("STUD123", contract.getStudentCod());
        assertEquals(2, contract.getAnContract());
        assertNull(contract.getId()); // not generated yet
        assertNull(contract.getDataGenerare()); // Hibernate sets this at persist time
        assertNull(contract.getCoduriMaterii());
    }

    @Test
    void testParameterizedConstructor() {
        Contract newContract = new Contract("STUD456", 3);

        assertEquals("STUD456", newContract.getStudentCod());
        assertEquals(3, newContract.getAnContract());
        assertNull(newContract.getId());
        assertNull(newContract.getDataGenerare());
        assertNull(newContract.getCoduriMaterii());
    }

    @Test
    void testSettersAndCoduriMaterii() {
        Contract contract = new Contract();
        contract.setId(99L);
        contract.setStudentCod("STUD456");
        contract.setAnContract(3);
        contract.setDataGenerare(LocalDateTime.now());

        List<String> materii = Arrays.asList("MAT01", "INF02", "AI03");
        contract.setCoduriMaterii(materii);

        assertEquals(99L, contract.getId());
        assertEquals("STUD456", contract.getStudentCod());
        assertEquals(3, contract.getAnContract());
        assertEquals(materii, contract.getCoduriMaterii());
        assertNotNull(contract.getDataGenerare());
    }

    @Test
    void testEqualsAndHashCode() {
        Contract c1 = new Contract();
        c1.setId(1L);

        Contract c2 = new Contract();
        c2.setId(1L);

        Contract c3 = new Contract();
        c3.setId(2L);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());

        assertNotEquals(c1, c3);
        assertNotEquals(c1.hashCode(), c3.hashCode());
    }

    @Test
    void testEqualsSameObject() {
        assertEquals(contract, contract);
        assertTrue(contract.equals(contract));
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(contract, null);
        assertFalse(contract.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        String differentObject = "not a contract";
        assertNotEquals(contract, differentObject);
        assertFalse(contract.equals(differentObject));
    }

    @Test
    void testEqualsWithSameIdDifferentFields() {
        Contract c1 = new Contract();
        c1.setId(1L);
        c1.setStudentCod("STUD001");
        c1.setAnContract(1);

        Contract c2 = new Contract();
        c2.setId(1L);
        c2.setStudentCod("STUD002");
        c2.setAnContract(3);

        // Should be equal because only id is included in equals/hashCode
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testEqualsWithBothNullIds() {
        Contract c1 = new Contract();
        c1.setId(null);

        Contract c2 = new Contract();
        c2.setId(null);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testEqualsWithOneNullId() {
        Contract c1 = new Contract();
        c1.setId(1L);

        Contract c2 = new Contract();
        c2.setId(null);

        assertNotEquals(c1, c2);
        assertNotEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testHashCodeConsistency() {
        int initialHashCode = contract.hashCode();

        // Hash code should remain consistent across multiple calls
        assertEquals(initialHashCode, contract.hashCode());
        assertEquals(initialHashCode, contract.hashCode());

        // Changing non-included fields should not affect hash code
        contract.setStudentCod("changedCod");
        contract.setAnContract(99);
        assertEquals(initialHashCode, contract.hashCode());
    }

    @Test
    void testHashCodeWithIdChange() {
        int initialHashCode = contract.hashCode();

        contract.setId(999L);
        int newHashCode = contract.hashCode();

        // Since id is included in equals/hashCode, hashCode should change
        assertNotEquals(initialHashCode, newHashCode);
    }

    @Test
    void testHashCodeWithNullId() {
        Contract c = new Contract();
        c.setId(null);
        int hashCode = c.hashCode();

        // Should not throw exception and should be consistent
        assertEquals(hashCode, c.hashCode());
    }

    @Test
    void testCanEqualMethod() {
        Contract c1 = new Contract();
        Contract c2 = new Contract();

        assertTrue(c1.canEqual(c2));
        assertTrue(c2.canEqual(c1));

        String notContract = "not a contract";
        assertFalse(c1.canEqual(notContract));
    }

    @Test
    void testDefaultConstructor() {
        Contract newContract = new Contract();

        assertNull(newContract.getId());
        assertNull(newContract.getStudentCod());
        assertEquals(0, newContract.getAnContract()); // int default
        assertNull(newContract.getDataGenerare());
        assertNull(newContract.getCoduriMaterii());
    }

    @Test
    void testStudentCodWithVariousFormats() {
        String[] studentCodes = {
                "STUD123", "STU456789", "S001", "STUDENT2024001",
                "FAF231001", "CTI241002", "IA251003"
        };

        for (String cod : studentCodes) {
            contract.setStudentCod(cod);
            assertEquals(cod, contract.getStudentCod());
        }
    }

    @Test
    void testStudentCodWithSpecialCharacters() {
        contract.setStudentCod("STUD-123");
        assertEquals("STUD-123", contract.getStudentCod());

        contract.setStudentCod("STUD_456");
        assertEquals("STUD_456", contract.getStudentCod());

        contract.setStudentCod("STUD.789");
        assertEquals("STUD.789", contract.getStudentCod());
    }

    @Test
    void testLongStudentCod() {
        String longCod = "FOARTE_LUNG_COD_STUDENT_CARE_DEPASESTE_LIMITELE_NORMALE_2024_001";
        contract.setStudentCod(longCod);
        assertEquals(longCod, contract.getStudentCod());
    }

    @Test
    void testCompleteContractSetup() {
        List<String> materii = Arrays.asList(
                "POO001", "ASD002", "BD003", "SO004", "RC005"
        );
        LocalDateTime dataContract = LocalDateTime.of(2024, 9, 15, 10, 0, 0);

        Contract completeContract = new Contract();
        completeContract.setId(200L);
        completeContract.setStudentCod("STUD2024001");
        completeContract.setAnContract(3);
        completeContract.setDataGenerare(dataContract);
        completeContract.setCoduriMaterii(materii);

        // Verify all fields are set correctly
        assertEquals(200L, completeContract.getId());
        assertEquals("STUD2024001", completeContract.getStudentCod());
        assertEquals(3, completeContract.getAnContract());
        assertEquals(dataContract, completeContract.getDataGenerare());
        assertEquals(5, completeContract.getCoduriMaterii().size());
        assertEquals(materii, completeContract.getCoduriMaterii());
    }

    @Test
    void testToStringNotNull() {
        assertNotNull(contract.toString());

        Contract emptyContract = new Contract();
        assertNotNull(emptyContract.toString());
    }

    @Test
    void testContractTypicalUseCases() {
        // Test typical contract scenarios
        String[][] contractData = {
                {"STUD2024001", "1"}, // First year student
                {"STUD2023002", "2"}, // Second year student
                {"STUD2022003", "3"}, // Third year student
                {"STUD2021004", "4"}  // Fourth year student
        };

        for (String[] data : contractData) {
            contract.setStudentCod(data[0]);
            contract.setAnContract(Integer.parseInt(data[1]));

            assertEquals(data[0], contract.getStudentCod());
            assertEquals(Integer.parseInt(data[1]), contract.getAnContract());
        }
    }

    @Test
    void testCoduriMateriiWithLargeNumberOfElements() {
        // Test with large subject list
        List<String> largeMaterii = new ArrayList<>();

        for (int i = 1; i <= 50; i++) {
            largeMaterii.add("MAT" + String.format("%03d", i));
        }

        contract.setCoduriMaterii(largeMaterii);
        assertEquals(50, contract.getCoduriMaterii().size());
        assertTrue(contract.getCoduriMaterii().contains("MAT001"));
        assertTrue(contract.getCoduriMaterii().contains("MAT050"));
    }

    @Test
    void testBusinessLogicScenarios() {
        // Test typical university contract scenarios

        // First year contract with basic subjects
        contract.setStudentCod("STUD2024001");
        contract.setAnContract(1);
        contract.setCoduriMaterii(Arrays.asList(
                "MAT001", "INF001", "FIZ001", "ENG001", "ROM001"
        ));
        contract.setDataGenerare(LocalDateTime.of(2024, 9, 1, 8, 0, 0));

        assertEquals("STUD2024001", contract.getStudentCod());
        assertEquals(1, contract.getAnContract());
        assertEquals(5, contract.getCoduriMaterii().size());
        assertTrue(contract.getCoduriMaterii().contains("MAT001"));

        // Third year contract with specialized subjects
        contract.setStudentCod("STUD2022005");
        contract.setAnContract(3);
        contract.setCoduriMaterii(Arrays.asList(
                "POO001", "ASD002", "BD003", "IA004", "RC005", "SO006"
        ));
        contract.setDataGenerare(LocalDateTime.of(2024, 9, 15, 14, 30, 0));

        assertEquals("STUD2022005", contract.getStudentCod());
        assertEquals(3, contract.getAnContract());
        assertEquals(6, contract.getCoduriMaterii().size());
        assertTrue(contract.getCoduriMaterii().contains("IA004"));
    }

    @Test
    void testNullSafetyOperations() {
        Contract nullContract = new Contract();

        // Should not throw exceptions
        assertNotNull(nullContract.toString());

        int hashCode1 = nullContract.hashCode();
        int hashCode2 = nullContract.hashCode();
        assertEquals(hashCode1, hashCode2);

        assertFalse(nullContract.equals(null));
        assertTrue(nullContract.equals(new Contract()));
    }

    @Test
    void testIdBasedEquality() {
        // Test the specific behavior of id-based equality
        Contract c1 = new Contract();
        Contract c2 = new Contract();
        Contract c3 = new Contract();

        // Initially all have null ids, so they should be equal
        assertEquals(c1, c2);
        assertEquals(c1, c3);

        // Set same id to c1 and c2
        c1.setId(100L);
        c2.setId(100L);
        c3.setId(200L);

        assertEquals(c1, c2);
        assertNotEquals(c1, c3);
        assertNotEquals(c2, c3);

        // Change other fields but keep same id
        c1.setStudentCod("STUD001");
        c1.setAnContract(1);
        c2.setStudentCod("STUD002");
        c2.setAnContract(4);

        // Should still be equal because only id matters
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testLongFieldType() {
        Contract newContract = new Contract();

        // Test that id is Long (not long), so it can be null
        assertNull(newContract.getId());

        newContract.setId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, newContract.getId());

        newContract.setId(Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, newContract.getId());

        newContract.setId(null);
        assertNull(newContract.getId());
    }

    @Test
    void testAnContractPrimitiveType() {
        Contract newContract = new Contract();

        // Test that anContract is int (not Integer), so it has default value 0
        assertEquals(0, newContract.getAnContract());

        newContract.setAnContract(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, newContract.getAnContract());

        newContract.setAnContract(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, newContract.getAnContract());
    }

    @Test
    void testLocalDateTimePrecision() {
        LocalDateTime precise = LocalDateTime.of(2024, 6, 27, 15, 30, 45, 123456789);
        contract.setDataGenerare(precise);
        assertEquals(precise, contract.getDataGenerare());

        // Test that nanosecond precision is maintained
        assertEquals(123456789, contract.getDataGenerare().getNano());
    }

    @Test
    void testContractWithDuplicateMaterii() {
        List<String> materiiWithDuplicates = Arrays.asList(
                "MAT001", "INF002", "MAT001", "POO003", "INF002"
        );

        contract.setCoduriMaterii(materiiWithDuplicates);
        assertEquals(5, contract.getCoduriMaterii().size()); // Allows duplicates

        // Count occurrences
        long mat001Count = contract.getCoduriMaterii().stream()
                .filter(cod -> "MAT001".equals(cod))
                .count();
        assertEquals(2, mat001Count);
    }

    @Test
    void testContractCreationTimestamp() {
        LocalDateTime before = LocalDateTime.now();
        Contract newContract = new Contract("STUD001", 2);
        newContract.setDataGenerare(LocalDateTime.now());
        LocalDateTime after = LocalDateTime.now();

        assertTrue(newContract.getDataGenerare().isAfter(before.minusSeconds(1)));
        assertTrue(newContract.getDataGenerare().isBefore(after.plusSeconds(1)));
    }

    @Test
    void testContractValidationScenarios() {
        // Test realistic contract validation scenarios

        // Valid contract
        Contract validContract = new Contract("STUD2024001", 2);
        validContract.setCoduriMaterii(Arrays.asList("MAT001", "INF002", "POO003"));
        validContract.setDataGenerare(LocalDateTime.now());

        assertNotNull(validContract.getStudentCod());
        assertTrue(validContract.getAnContract() > 0 && validContract.getAnContract() <= 4);
        assertNotNull(validContract.getCoduriMaterii());
        assertFalse(validContract.getCoduriMaterii().isEmpty());

        // Edge case contract
        Contract edgeContract = new Contract("", 0);
        edgeContract.setCoduriMaterii(new ArrayList<>());

        assertEquals("", edgeContract.getStudentCod());
        assertEquals(0, edgeContract.getAnContract());
        assertTrue(edgeContract.getCoduriMaterii().isEmpty());
    }
}
