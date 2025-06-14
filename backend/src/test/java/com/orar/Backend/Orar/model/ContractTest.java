package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ContractTest {

    @Test
    void testConstructorAndGetters() {
        Contract contract = new Contract("STUD123", 2);

        assertEquals("STUD123", contract.getStudentCod());
        assertEquals(2, contract.getAnContract());
        assertNull(contract.getId()); // not generated yet
        assertNull(contract.getDataGenerare()); // Hibernate sets this at persist time
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
}
