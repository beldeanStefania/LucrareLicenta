package com.orar.Backend.Orar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orar.Backend.Orar.dto.RepartizareProfDTO;
import com.orar.Backend.Orar.exception.MaterieDoesNotExistException;
import com.orar.Backend.Orar.exception.ProfesorNotFoundException;
import com.orar.Backend.Orar.exception.RepartizareProfAlreadyExistsException;
import com.orar.Backend.Orar.model.RepartizareProf;
import com.orar.Backend.Orar.service.RepartizareProfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RepartizareProfControllerTest {

    @Mock
    private RepartizareProfService repartizareProfService;

    @InjectMocks
    private RepartizareProfController repartizareProfController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private RepartizareProf repartizareProf;
    private RepartizareProfDTO repartizareProfDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(repartizareProfController).build();
        objectMapper = new ObjectMapper();

        // Setup test data
        repartizareProf = new RepartizareProf();
        repartizareProf.setId(1);
        repartizareProf.setTip("Curs");

        repartizareProfDTO = new RepartizareProfDTO();
        repartizareProfDTO.setId(1);
        repartizareProfDTO.setTip("Curs");
        repartizareProfDTO.setNumeProfesor("Popescu");
        repartizareProfDTO.setPrenumeProfesor("Ion");
        repartizareProfDTO.setMaterie("Matematica");
    }

    @Test
    void getAll_ShouldReturnListOfRepartizareProf() throws Exception {
        // Given
        List<RepartizareProf> repartizari = Arrays.asList(repartizareProf);
        when(repartizareProfService.getAll()).thenReturn(repartizari);

        // When & Then
        mockMvc.perform(get("/api/repartizareProf"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].tip").value("Curs"));

        verify(repartizareProfService, times(1)).getAll();
    }

    @Test
    void getMateriiProfesor_WithValidProfesorId_ShouldReturnMateriiList() throws Exception {
        // Given
        Integer profesorId = 1;
        List<RepartizareProfDTO> materiiList = Arrays.asList(repartizareProfDTO);
        when(repartizareProfService.getMateriiProfesor(profesorId)).thenReturn(materiiList);

        // When & Then
        mockMvc.perform(get("/api/repartizareProf/materiiProfesor/{profesorId}", profesorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].materie").value("Matematica"));

        verify(repartizareProfService, times(1)).getMateriiProfesor(profesorId);
    }

    @Test
    void getMateriiProfesor_WithException_ShouldReturnBadRequest() throws Exception {
        // Given
        Integer profesorId = 1;
        when(repartizareProfService.getMateriiProfesor(profesorId))
                .thenThrow(new RuntimeException("Test exception"));

        // When & Then
        mockMvc.perform(get("/api/repartizareProf/materiiProfesor/{profesorId}", profesorId))
                .andExpect(status().isBadRequest());

        verify(repartizareProfService, times(1)).getMateriiProfesor(profesorId);
    }

    @Test
    void getMateriiDistincteProfesor_WithValidProfesorId_ShouldReturnDistinctMateriiList() throws Exception {
        // Given
        Integer profesorId = 1;
        List<String> materiiDistincte = Arrays.asList("Matematica", "Fizica");
        when(repartizareProfService.getMateriiDistincteProfesor(profesorId)).thenReturn(materiiDistincte);

        // When & Then
        mockMvc.perform(get("/api/repartizareProf/materiiProfesorDistincte/{profesorId}", profesorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0]").value("Matematica"))
                .andExpect(jsonPath("$[1]").value("Fizica"));

        verify(repartizareProfService, times(1)).getMateriiDistincteProfesor(profesorId);
    }

    @Test
    void getMateriiDistincteProfesor_WithException_ShouldReturnBadRequest() throws Exception {
        // Given
        Integer profesorId = 1;
        when(repartizareProfService.getMateriiDistincteProfesor(profesorId))
                .thenThrow(new RuntimeException("Test exception"));

        // When & Then
        mockMvc.perform(get("/api/repartizareProf/materiiProfesorDistincte/{profesorId}", profesorId))
                .andExpect(status().isBadRequest());

        verify(repartizareProfService, times(1)).getMateriiDistincteProfesor(profesorId);
    }

    @Test
    void addRepartizareProf_WithValidData_ShouldReturnCreatedRepartizare() throws Exception {
        // Given
        when(repartizareProfService.add(any(RepartizareProfDTO.class))).thenReturn(repartizareProf);

        // When & Then
        mockMvc.perform(post("/api/repartizareProf")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(repartizareProfDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tip").value("Curs"));

        verify(repartizareProfService, times(1)).add(any(RepartizareProfDTO.class));
    }

    @Test
    void addRepartizareProf_WithProfesorNotFoundException_ShouldReturnBadRequest() throws Exception {
        // Given
        when(repartizareProfService.add(any(RepartizareProfDTO.class)))
                .thenThrow(new ProfesorNotFoundException("Profesor not found"));

        // When & Then
        mockMvc.perform(post("/api/repartizareProf")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(repartizareProfDTO)))
                .andExpect(status().isBadRequest());

        verify(repartizareProfService, times(1)).add(any(RepartizareProfDTO.class));
    }

    @Test
    void addRepartizareProf_WithRepartizareProfAlreadyExistsException_ShouldReturnBadRequest() throws Exception {
        // Given
        when(repartizareProfService.add(any(RepartizareProfDTO.class)))
                .thenThrow(new RepartizareProfAlreadyExistsException("Repartizare already exists"));

        // When & Then
        mockMvc.perform(post("/api/repartizareProf")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(repartizareProfDTO)))
                .andExpect(status().isBadRequest());

        verify(repartizareProfService, times(1)).add(any(RepartizareProfDTO.class));
    }

    @Test
    void addRepartizareProf_WithMaterieDoesNotExistException_ShouldReturnBadRequest() throws Exception {
        // Given
        when(repartizareProfService.add(any(RepartizareProfDTO.class)))
                .thenThrow(new MaterieDoesNotExistException("Materie does not exist"));

        // When & Then
        mockMvc.perform(post("/api/repartizareProf")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(repartizareProfDTO)))
                .andExpect(status().isBadRequest());

        verify(repartizareProfService, times(1)).add(any(RepartizareProfDTO.class));
    }

    @Test
    void updateRepartizareProf_WithValidData_ShouldReturnUpdatedRepartizare() throws Exception {
        // Given
        Integer id = 1;
        when(repartizareProfService.updateRepartizareProf(anyInt(), any(RepartizareProfDTO.class)))
                .thenReturn(repartizareProf);

        // When & Then
        mockMvc.perform(put("/api/repartizareProf/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(repartizareProfDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tip").value("Curs"));

        verify(repartizareProfService, times(1)).updateRepartizareProf(eq(id), any(RepartizareProfDTO.class));
    }

    @Test
    void updateRepartizareProf_WithException_ShouldReturnBadRequest() throws Exception {
        // Given
        Integer id = 1;
        when(repartizareProfService.updateRepartizareProf(anyInt(), any(RepartizareProfDTO.class)))
                .thenThrow(new ProfesorNotFoundException("Profesor not found"));

        // When & Then
        mockMvc.perform(put("/api/repartizareProf/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(repartizareProfDTO)))
                .andExpect(status().isBadRequest());

        verify(repartizareProfService, times(1)).updateRepartizareProf(eq(id), any(RepartizareProfDTO.class));
    }

    @Test
    void deleteRepartizareProf_WithValidId_ShouldReturnOk() throws Exception {
        // Given
        Integer id = 1;
        doNothing().when(repartizareProfService).delete(id);

        // When & Then
        mockMvc.perform(delete("/api/repartizareProf/{id}", id))
                .andExpect(status().isOk());

        verify(repartizareProfService, times(1)).delete(id);
    }

    @Test
    void deleteRepartizareProf_WithException_ShouldReturnBadRequest() throws Exception {
        // Given
        Integer id = 1;
        doThrow(new RuntimeException("Delete failed")).when(repartizareProfService).delete(id);

        // When & Then
        mockMvc.perform(delete("/api/repartizareProf/{id}", id))
                .andExpect(status().isBadRequest());

        verify(repartizareProfService, times(1)).delete(id);
    }

    @Test
    void addRepartizareProf_WithInvalidData_ShouldHandleValidationErrors() throws Exception {
        // Given - DTO fără date obligatorii
        RepartizareProfDTO invalidDTO = new RepartizareProfDTO();

        // When & Then
        mockMvc.perform(post("/api/repartizareProf")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isOk()); // Controller-ul nu verifică validarea în mod explicit

        // Nota: Pentru validare completă, ai nevoie de adnotări @Valid pe DTO și @NotNull/@NotEmpty pe câmpuri
    }

    @Test
    void updateRepartizareProf_WithAllExceptionTypes_ShouldReturnBadRequest() throws Exception {
        // Test pentru RepartizareProfAlreadyExistsException
        Integer id = 1;
        when(repartizareProfService.updateRepartizareProf(anyInt(), any(RepartizareProfDTO.class)))
                .thenThrow(new RepartizareProfAlreadyExistsException("Already exists"));

        mockMvc.perform(put("/api/repartizareProf/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(repartizareProfDTO)))
                .andExpect(status().isBadRequest());

        // Test pentru MaterieDoesNotExistException
        when(repartizareProfService.updateRepartizareProf(anyInt(), any(RepartizareProfDTO.class)))
                .thenThrow(new MaterieDoesNotExistException("Materie not found"));

        mockMvc.perform(put("/api/repartizareProf/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(repartizareProfDTO)))
                .andExpect(status().isBadRequest());
    }
}