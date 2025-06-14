package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.model.Specializare;
import com.orar.Backend.Orar.repository.SpecializareRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SpecializareServiceTest {

    @InjectMocks
    private SpecializareService specializareService;

    @Mock
    private SpecializareRepository specializareRepository;

    private Specializare spec1;
    private Specializare spec2;

    @BeforeEach
    void setup() {
        spec1 = new Specializare();
        spec1.setId(1);
        spec1.setSpecializare("CTI");

        spec2 = new Specializare();
        spec2.setId(2);
        spec2.setSpecializare("IS");
    }

    @Test
    void testGetAll_ReturnsAllSpecializari() {
        when(specializareRepository.findAll()).thenReturn(List.of(spec1, spec2));

        List<Specializare> result = specializareService.getAll();

        assertEquals(2, result.size());
        assertEquals("CTI", result.get(0).getSpecializare());
        assertEquals("IS", result.get(1).getSpecializare());

        verify(specializareRepository, times(1)).findAll();
    }
}
