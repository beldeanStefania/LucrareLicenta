package com.orar.Backend.Orar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orar.Backend.Orar.dto.OrarDTO;
import com.orar.Backend.Orar.dto.OrarDetailsDTO;
import com.orar.Backend.Orar.exception.OrarAlreadyExistsException;
import com.orar.Backend.Orar.exception.OrarNotFoundException;
import com.orar.Backend.Orar.model.Orar;
import com.orar.Backend.Orar.service.OrarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrarControllerTest {

    @Mock
    private OrarService orarService;

    @InjectMocks
    private OrarController orarController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private OrarDTO orarDTO;
    private Orar orar;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orarController).build();
        objectMapper = new ObjectMapper();

        orarDTO = new OrarDTO();
        orarDTO.setGrupa("341");
        orarDTO.setOraInceput(8);
        orarDTO.setOraSfarsit(10);
        orarDTO.setZi("Luni");
        orarDTO.setSalaId(1);
        orarDTO.setRepartizareProfId(1);
        orarDTO.setFrecventa("Saptamanal");

        orar = new Orar();
        orar.setId(1);
        orar.setGrupa("341");
        orar.setOraInceput(8);
        orar.setOraSfarsit(10);
        orar.setZi("Luni");
        orar.setFrecventa("Saptamanal");
    }

    @Test
    void getAll_ShouldReturnListOfOrar() throws Exception {
        when(orarService.getAll()).thenReturn(List.of(orar));

        mockMvc.perform(get("/api/orare"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].grupa").value("341"));
    }

    @Test
    void getOrarDetailsByGrupa_ShouldReturnDetailsList() throws Exception {
        OrarDetailsDTO dto = OrarDetailsDTO.builder()
                .grupa("341")
                .zi("Luni")
                .disciplina("Matematica")
                .build();

        when(orarService.getOrarDetailsByGrupa("341"))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/api/orare/getAll/{grupa}", "341"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].grupa").value("341"));
    }

    @Test
    void getOrarDetailsByProfesor_ShouldReturnDetailsList() throws Exception {
        OrarDetailsDTO dto = OrarDetailsDTO.builder()
                .grupa("341")
                .cadruDidactic("Popescu Ion")
                .build();

        when(orarService.getOrarDetailsByProfesor(1))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/api/orare/getAllProfesor/{profesorId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cadruDidactic").value("Popescu Ion"));
    }

    @Test
    void addOrar_ShouldReturnCreatedOrar() throws Exception {
        when(orarService.add(any(OrarDTO.class))).thenReturn(orar);

        mockMvc.perform(post("/api/orare/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orarDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grupa").value("341"));
    }

    @Test
    void addOrar_WithAlreadyExists_ShouldReturnBadRequest() throws Exception {
        when(orarService.add(any(OrarDTO.class))).thenThrow(new OrarAlreadyExistsException("Conflict"));

        mockMvc.perform(post("/api/orare/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orarDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateOrar_ShouldReturnUpdatedOrar() throws Exception {
        when(orarService.updateOrar(eq(1), any(OrarDTO.class))).thenReturn(orar);

        mockMvc.perform(put("/api/orare/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orarDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grupa").value("341"));
    }

    @Test
    void updateOrar_WithError_ShouldReturnBadRequest() throws Exception {
        when(orarService.updateOrar(eq(1), any(OrarDTO.class))).thenThrow(new OrarNotFoundException("Not found"));

        mockMvc.perform(put("/api/orare/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orarDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteOrar_ShouldReturnOk() throws Exception {
        doNothing().when(orarService).deleteOrar(1);

        mockMvc.perform(delete("/api/orare/delete/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    void deleteOrar_WithError_ShouldReturnBadRequest() throws Exception {
        doThrow(new OrarNotFoundException("Not found"))
                .when(orarService).deleteOrar(1);

        mockMvc.perform(delete("/api/orare/delete/{id}", 1))
                .andExpect(status().isBadRequest());
    }
}
