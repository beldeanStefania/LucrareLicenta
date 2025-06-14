package com.orar.Backend.Orar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orar.Backend.Orar.dto.ImportResultDTO;
import com.orar.Backend.Orar.dto.ProfesorDTO;
import com.orar.Backend.Orar.exception.ProfesorAlreadyExistsException;
import com.orar.Backend.Orar.exception.ProfesorDoesNotExistException;
import com.orar.Backend.Orar.exception.ProfesorNotFoundException;
import com.orar.Backend.Orar.model.Profesor;
import com.orar.Backend.Orar.service.ProfesorService;
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

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProfesorControllerTest {

    @Mock
    private ProfesorService profesorService;

    @InjectMocks
    private ProfesorController profesorController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Profesor profesor;
    private ProfesorDTO profesorDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(profesorController).build();
        objectMapper = new ObjectMapper();

        profesor = new Profesor();
        profesor.setId(1);
        profesor.setNume("Popescu");
        profesor.setPrenume("Ion");

        profesorDTO = new ProfesorDTO();
        profesorDTO.setNume("Popescu");
        profesorDTO.setPrenume("Ion");
        profesorDTO.setUsername("ipopescu");
        profesorDTO.setPassword("parola123");
        profesorDTO.setEmail("ion.popescu@example.com");
    }

    @Test
    void getAll_ShouldReturnListOfProfesori() throws Exception {
        when(profesorService.getAll()).thenReturn(List.of(profesor));

        mockMvc.perform(get("/api/profesor/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nume").value("Popescu"));

        verify(profesorService, times(1)).getAll();
    }

    @Test
    void addProfesor_ShouldReturnCreatedProfesor() throws Exception {
        when(profesorService.add(any(ProfesorDTO.class))).thenReturn(profesor);

        mockMvc.perform(post("/api/profesor/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profesorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nume").value("Popescu"));
    }

    @Test
    void updateProfesor_WithValidData_ShouldReturnUpdatedProfesor() throws Exception {
        when(profesorService.update(anyInt(), any(ProfesorDTO.class))).thenReturn(profesor);

        mockMvc.perform(put("/api/profesor/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profesorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prenume").value("Ion"));
    }

    @Test
    void updateProfesor_WithProfesorNotFoundException_ShouldReturnBadRequest() throws Exception {
        when(profesorService.update(anyInt(), any(ProfesorDTO.class)))
                .thenThrow(new ProfesorNotFoundException("Not found"));

        mockMvc.perform(put("/api/profesor/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profesorDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteProfesor_WithValidNames_ShouldReturnOk() throws Exception {
        doNothing().when(profesorService).delete("Popescu", "Ion");

        mockMvc.perform(delete("/api/profesor/delete/{nume}/{prenume}", "Popescu", "Ion"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteProfesor_WithException_ShouldReturnBadRequest() throws Exception {
        doThrow(new ProfesorDoesNotExistException("Not found"))
                .when(profesorService).delete("Popescu", "Ion");

        mockMvc.perform(delete("/api/profesor/delete/{nume}/{prenume}", "Popescu", "Ion"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void importStudents_ShouldReturnReport() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "profesori.csv",
                "text/csv",
                "nume,prenume,email\nPopescu,Ion,ion@example.com".getBytes()
        );

        ImportResultDTO result = new ImportResultDTO(1, true, "Import reușit");

        when(profesorService.importFromCsv(any())).thenReturn(List.of(result));

        mockMvc.perform(multipart("/api/profesor/import").file(mockFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].row").value(1))
                .andExpect(jsonPath("$[0].success").value(true))
                .andExpect(jsonPath("$[0].message").value("Import reușit"));

        verify(profesorService, times(1)).importFromCsv(any());
    }

}
