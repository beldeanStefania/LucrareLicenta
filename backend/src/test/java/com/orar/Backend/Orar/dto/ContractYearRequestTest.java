package com.orar.Backend.Orar.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.orar.Backend.Orar.dto.ContractYearRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ContractYearRequestTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        ContractYearRequest request = new ContractYearRequest();

        request.setStudentCod("S12345");
        request.setAnContract(2);
        request.setCoduriMaterii(List.of("INF101", "INF102", "INF103"));

        assertEquals("S12345", request.getStudentCod());
        assertEquals(2, request.getAnContract());
        assertEquals(3, request.getCoduriMaterii().size());
        assertTrue(request.getCoduriMaterii().contains("INF101"));
    }
}
