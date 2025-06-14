package com.orar.Backend.Orar.dto;

import com.orar.Backend.Orar.dto.ContractDTO;
import com.orar.Backend.Orar.enums.Tip;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContractDTOTest {

    @Test
    void testConstructorAndGetters() {
        ContractDTO contract = new ContractDTO(
                "INF204",
                "Programare Java",
                5,
                2,
                Tip.OBLIGATORIE,
                true,
                101
        );

        assertEquals("INF204", contract.getCod());
        assertEquals("Programare Java", contract.getNume());
        assertEquals(5, contract.getCredite());
        assertEquals(2, contract.getSemestru());
        assertEquals(Tip.OBLIGATORIE, contract.getTip());
        assertTrue(contract.isSelected());
        assertEquals(101, contract.getMateriiOptionaleId());
    }

    @Test
    void testSetters() {
        ContractDTO contract = new ContractDTO(
                null, null, 0, 0, null, false, null
        );

        contract.setCod("INF101");
        contract.setNume("Algoritmi");
        contract.setCredite(6);
        contract.setSemestru(1);
        contract.setTip(Tip.OPTIONALA);
        contract.setSelected(false);
        contract.setMateriiOptionaleId(202);

        assertEquals("INF101", contract.getCod());
        assertEquals("Algoritmi", contract.getNume());
        assertEquals(6, contract.getCredite());
        assertEquals(1, contract.getSemestru());
        assertEquals(Tip.OPTIONALA, contract.getTip());
        assertFalse(contract.isSelected());
        assertEquals(202, contract.getMateriiOptionaleId());
    }
}
