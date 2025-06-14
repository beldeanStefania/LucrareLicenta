package com.orar.Backend.Orar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orar.Backend.Orar.dto.ImportResultDTO;
import com.orar.Backend.Orar.dto.MaterieDTO;
import com.orar.Backend.Orar.model.Materie;
import com.orar.Backend.Orar.repository.MaterieRepository;
import com.orar.Backend.Orar.service.MaterieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MaterieControllerTest {

    @Mock
    private MaterieService materieService;

    @Mock
    private MaterieRepository materieRepository;

    @InjectMocks
    private MaterieController materieController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private MaterieDTO materieDTO;
    private Materie materie;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(materieController).build();
        objectMapper = new ObjectMapper();

        materieDTO = new MaterieDTO("Matematica", 1, "MAT101", 5);
        materie = new Materie();
        materie.setId(1);
        materie.setNume("Matematica");
        materie.setSemestru(1);
        materie.setCod("MAT101");
        materie.setCredite(5);
    }

    @Test
    void search_ShouldReturnMatchingMaterii() throws Exception {
        when(materieRepository.findByNumeContainingIgnoreCaseOrCodContainingIgnoreCase("mat", "mat"))
                .thenReturn(List.of(materie));

        mockMvc.perform(get("/api/materie/search?q=mat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nume").value("Matematica"))
                .andExpect(jsonPath("$[0].cod").value("MAT101"));
    }

    @Test
    void getAll_ShouldReturnAllMaterii() throws Exception {
        when(materieService.getAll()).thenReturn(List.of(materie));

        mockMvc.perform(get("/api/materie/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nume").value("Matematica"));
    }

    @Test
    void addMaterie_ShouldReturnCreated() throws Exception {
        when(materieService.add(any(MaterieDTO.class))).thenReturn(materie);

        mockMvc.perform(post("/api/materie/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(materieDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nume").value("Matematica"));
    }

    @Test
    void addMaterie_WithException_ShouldReturnBadRequest() throws Exception {
        when(materieService.add(any(MaterieDTO.class))).thenThrow(new RuntimeException("fail"));

        mockMvc.perform(post("/api/materie/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(materieDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateMaterie_ShouldReturnUpdated() throws Exception {
        when(materieService.update(any(MaterieDTO.class))).thenReturn(materie);

        mockMvc.perform(put("/api/materie/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(materieDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cod").value("MAT101"));
    }

    @Test
    void updateMaterie_WithException_ShouldReturnBadRequest() throws Exception {
        when(materieService.update(any(MaterieDTO.class))).thenThrow(new RuntimeException("fail"));

        mockMvc.perform(put("/api/materie/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(materieDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteMaterie_ShouldReturnOk() throws Exception {
        doNothing().when(materieService).delete(1);

        mockMvc.perform(delete("/api/materie/delete/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    void deleteMaterie_WithException_ShouldReturnBadRequest() throws Exception {
        doThrow(new RuntimeException("fail")).when(materieService).delete(1);

        mockMvc.perform(delete("/api/materie/delete/{id}", 1))
                .andExpect(status().isBadRequest());
    }

    @Test
    void importMaterii_ShouldReturnImportResult() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "materii.csv",
                "text/csv", "nume,semestru,cod,credite\nMatematica,1,MAT101,5".getBytes());

        ImportResultDTO result = new ImportResultDTO(1, true, "OK");

        when(materieService.importFromCsv(any())).thenReturn(List.of(result));

        mockMvc.perform(multipart("/api/materie/import").file(mockFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].row").value(1))
                .andExpect(jsonPath("$[0].success").value(true))
                .andExpect(jsonPath("$[0].message").value("OK"));
    }
}