package com.orar.Backend.Orar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orar.Backend.Orar.dto.CladireDTO;
import com.orar.Backend.Orar.exception.CladireAlreadyExistsException;
import com.orar.Backend.Orar.exception.CladireNotFoundException;
import com.orar.Backend.Orar.model.Cladire;
import com.orar.Backend.Orar.service.CladireService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CladireControllerTest {

    @Mock
    private CladireService cladireService;

    @InjectMocks
    private CladireController cladireController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Cladire cladire;
    private CladireDTO cladireDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cladireController).build();
        objectMapper = new ObjectMapper();

        // Setup test data
        cladire = new Cladire();
        cladire.setId(1);
        cladire.setNume("Corpul A");
        cladire.setAdresa("Str. Universitatii nr. 1");

        cladireDTO = new CladireDTO();
        cladireDTO.setNume("Corpul A");
        cladireDTO.setAdresa("Str. Universitatii nr. 1");
    }

    @Test
    void getAll_ShouldReturnListOfCladiri() throws Exception {
        // Given
        List<Cladire> cladiri = Arrays.asList(cladire);
        when(cladireService.getAll()).thenReturn(cladiri);

        // When & Then
        mockMvc.perform(get("/api/cladire/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nume").value("Corpul A"))
                .andExpect(jsonPath("$[0].adresa").value("Str. Universitatii nr. 1"));

        verify(cladireService, times(1)).getAll();
    }

    @Test
    void getAll_WithEmptyList_ShouldReturnEmptyArray() throws Exception {
        // Given
        when(cladireService.getAll()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/cladire/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(cladireService, times(1)).getAll();
    }

    @Test
    void addCladire_WithValidData_ShouldReturnCreatedCladire() throws Exception {
        // Given
        when(cladireService.add(any(CladireDTO.class))).thenReturn(cladire);

        // When & Then
        mockMvc.perform(post("/api/cladire/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cladireDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nume").value("Corpul A"))
                .andExpect(jsonPath("$.adresa").value("Str. Universitatii nr. 1"));

        verify(cladireService, times(1)).add(any(CladireDTO.class));
    }

    @Test
    void addCladire_WithCladireAlreadyExistsException_ShouldReturnBadRequest() throws Exception {
        // Given
        when(cladireService.add(any(CladireDTO.class)))
                .thenThrow(new CladireAlreadyExistsException("Cladire already exists"));

        // When & Then
        mockMvc.perform(post("/api/cladire/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cladireDTO)))
                .andExpect(status().isBadRequest());

        verify(cladireService, times(1)).add(any(CladireDTO.class));
    }

    @Test
    void addCladire_WithEmptyRequestBody_ShouldHandleGracefully() throws Exception {
        // Given
        CladireDTO emptyDTO = new CladireDTO();
        when(cladireService.add(any(CladireDTO.class))).thenReturn(cladire);

        // When & Then
        mockMvc.perform(post("/api/cladire/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyDTO)))
                .andExpect(status().isOk());

        verify(cladireService, times(1)).add(any(CladireDTO.class));
    }

    @Test
    void updateCladire_WithValidData_ShouldReturnUpdatedCladire() throws Exception {
        // Given
        String numeCladire = "Corpul A";
        Cladire updatedCladire = new Cladire();
        updatedCladire.setId(1);
        updatedCladire.setNume("Corpul A Updated");
        updatedCladire.setAdresa("Str. Universitatii nr. 2");

        CladireDTO updateDTO = new CladireDTO();
        updateDTO.setNume("Corpul A Updated");
        updateDTO.setAdresa("Str. Universitatii nr. 2");

        when(cladireService.update(anyString(), any(CladireDTO.class))).thenReturn(updatedCladire);

        // When & Then
        mockMvc.perform(put("/api/cladire/update/{numeCladire}", numeCladire)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nume").value("Corpul A Updated"))
                .andExpect(jsonPath("$.adresa").value("Str. Universitatii nr. 2"));

        verify(cladireService, times(1)).update(eq(numeCladire), any(CladireDTO.class));
    }

    @Test
    void updateCladire_WithCladireNotFoundException_ShouldReturnBadRequest() throws Exception {
        // Given
        String numeCladire = "Corpul X";
        when(cladireService.update(anyString(), any(CladireDTO.class)))
                .thenThrow(new CladireNotFoundException("Cladire not found"));

        // When & Then
        mockMvc.perform(put("/api/cladire/update/{numeCladire}", numeCladire)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cladireDTO)))
                .andExpect(status().isBadRequest());

        verify(cladireService, times(1)).update(eq(numeCladire), any(CladireDTO.class));
    }

    @Test
    void updateCladire_WithSpecialCharactersInNumeCladire_ShouldWork() throws Exception {
        // Given
        String numeCladire = "Corpul A-1 (Nou)";
        when(cladireService.update(anyString(), any(CladireDTO.class))).thenReturn(cladire);

        // When & Then
        mockMvc.perform(put("/api/cladire/update/{numeCladire}", numeCladire)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cladireDTO)))
                .andExpect(status().isOk());

        verify(cladireService, times(1)).update(eq(numeCladire), any(CladireDTO.class));
    }

    @Test
    void deleteCladire_WithValidNumeCladire_ShouldReturnOk() throws Exception {
        // Given
        String numeCladire = "Corpul A";
        doNothing().when(cladireService).delete(numeCladire);

        // When & Then
        mockMvc.perform(delete("/api/cladire/delete/{numeCladire}", numeCladire))
                .andExpect(status().isOk());

        verify(cladireService, times(1)).delete(numeCladire);
    }

    @Test
    void deleteCladire_WithCladireNotFoundException_ShouldReturnBadRequest() throws Exception {
        // Given
        String numeCladire = "Corpul X";
        doThrow(new CladireNotFoundException("Cladire not found"))
                .when(cladireService).delete(numeCladire);

        // When & Then
        mockMvc.perform(delete("/api/cladire/delete/{numeCladire}", numeCladire))
                .andExpect(status().isBadRequest());

        verify(cladireService, times(1)).delete(numeCladire);
    }

    @Test
    void addCladire_WithNullValues_ShouldPassToService() throws Exception {
        // Given
        CladireDTO nullDTO = new CladireDTO();
        nullDTO.setNume(null);
        nullDTO.setAdresa(null);

        when(cladireService.add(any(CladireDTO.class))).thenReturn(cladire);

        // When & Then
        mockMvc.perform(post("/api/cladire/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nullDTO)))
                .andExpect(status().isOk());

        verify(cladireService, times(1)).add(any(CladireDTO.class));
    }

    @Test
    void updateCladire_WithNullValues_ShouldPassToService() throws Exception {
        // Given
        String numeCladire = "Corpul A";
        CladireDTO nullDTO = new CladireDTO();
        nullDTO.setNume(null);
        nullDTO.setAdresa(null);

        when(cladireService.update(anyString(), any(CladireDTO.class))).thenReturn(cladire);

        // When & Then
        mockMvc.perform(put("/api/cladire/update/{numeCladire}", numeCladire)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nullDTO)))
                .andExpect(status().isOk());

        verify(cladireService, times(1)).update(eq(numeCladire), any(CladireDTO.class));
    }

    @Test
    void addCladire_WithLongStrings_ShouldWork() throws Exception {
        // Given
        CladireDTO longStringDTO = new CladireDTO();
        longStringDTO.setNume("A".repeat(100));
        longStringDTO.setAdresa("B".repeat(200));

        when(cladireService.add(any(CladireDTO.class))).thenReturn(cladire);

        // When & Then
        mockMvc.perform(post("/api/cladire/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(longStringDTO)))
                .andExpect(status().isOk());

        verify(cladireService, times(1)).add(any(CladireDTO.class));
    }

    @Test
    void getAllEndpoints_ShouldHaveCorrectMapping() throws Exception {
        // Test că toate endpoint-urile răspund la path-urile corecte

        // Test GET /api/cladire/getAll
        when(cladireService.getAll()).thenReturn(Arrays.asList());
        mockMvc.perform(get("/api/cladire/getAll"))
                .andExpect(status().isOk());

        // Test POST /api/cladire/add
        when(cladireService.add(any(CladireDTO.class))).thenReturn(cladire);
        mockMvc.perform(post("/api/cladire/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cladireDTO)))
                .andExpect(status().isOk());

        // Test PUT /api/cladire/update/{numeCladire}
        when(cladireService.update(anyString(), any(CladireDTO.class))).thenReturn(cladire);
        mockMvc.perform(put("/api/cladire/update/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cladireDTO)))
                .andExpect(status().isOk());

        // Test DELETE /api/cladire/delete/{numeCladire}
        doNothing().when(cladireService).delete(anyString());
        mockMvc.perform(delete("/api/cladire/delete/test"))
                .andExpect(status().isOk());
    }
}