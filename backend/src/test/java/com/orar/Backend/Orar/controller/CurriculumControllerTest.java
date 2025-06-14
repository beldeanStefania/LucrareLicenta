package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.MaterieDTO;
import com.orar.Backend.Orar.service.CurriculumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CurriculumControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CurriculumService curriculumService;

    @InjectMocks
    private CurriculumController curriculumController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(curriculumController).build();
    }

    @Test
    void getMateriiObligatorii_ShouldReturnMateriiList() throws Exception {
        String studentCod = "S123";
        int anContract = 1;

        List<MaterieDTO> materii = List.of(
                new MaterieDTO("Matematica", 1, "MAT101", 6),
                new MaterieDTO("Informatica", 1, "INF101", 5)
        );

        when(curriculumService.getMateriiObligatoriiByYear(studentCod, anContract)).thenReturn(materii);

        mockMvc.perform(get("/api/curriculum/materii-obligatorii/{studentCod}/{anContract}", studentCod, anContract))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nume").value("Matematica"))
                .andExpect(jsonPath("$[1].cod").value("INF101"));
    }

    @Test
    void getMateriiObligatorii_WhenExceptionThrown_ShouldReturn500() throws Exception {
        String studentCod = "S999";
        int anContract = 2;

        when(curriculumService.getMateriiObligatoriiByYear(studentCod, anContract))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/api/curriculum/materii-obligatorii/{studentCod}/{anContract}", studentCod, anContract))
                .andExpect(status().isInternalServerError());
    }
}
