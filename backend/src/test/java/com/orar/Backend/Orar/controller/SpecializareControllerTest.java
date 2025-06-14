package com.orar.Backend.Orar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orar.Backend.Orar.model.Specializare;
import com.orar.Backend.Orar.service.SpecializareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SpecializareControllerTest {

    @Mock
    private SpecializareService specializareService;

    @InjectMocks
    private SpecializareController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllSpecializari_Success_WithData() throws Exception {
        // Given
        Specializare spec1 = createSpecializare(1, "Informatica");
        Specializare spec2 = createSpecializare(2, "Calculatoare");
        Specializare spec3 = createSpecializare(3, "Matematica");

        List<Specializare> expectedSpecializari = Arrays.asList(spec1, spec2, spec3);

        when(specializareService.getAll()).thenReturn(expectedSpecializari);

        // When & Then
        mockMvc.perform(get("/api/specializare/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].specializare").value("Informatica"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].specializare").value("Calculatoare"))
                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[2].specializare").value("Matematica"));

        verify(specializareService, times(1)).getAll();
    }

    @Test
    void testGetAllSpecializari_Success_EmptyList() throws Exception {
        // Given
        List<Specializare> emptyList = new ArrayList<>();
        when(specializareService.getAll()).thenReturn(emptyList);

        // When & Then
        mockMvc.perform(get("/api/specializare/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(0))
                .andExpect(content().json("[]"));

        verify(specializareService, times(1)).getAll();
    }

    @Test
    void testGetAllSpecializari_Success_SingleItem() throws Exception {
        // Given
        Specializare spec = createSpecializare(1, "Informatica Aplicata");
        List<Specializare> singleItemList = Arrays.asList(spec);

        when(specializareService.getAll()).thenReturn(singleItemList);

        // When & Then
        mockMvc.perform(get("/api/specializare/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].specializare").value("Informatica Aplicata"));

        verify(specializareService, times(1)).getAll();
    }

    // Unit tests pentru apelul direct al metodei din controller (fără MockMvc)
    @Test
    void testGetAllSpecializariDirectCall_Success() {
        // Given
        Specializare spec1 = createSpecializare(1, "Informatica");
        Specializare spec2 = createSpecializare(2, "Calculatoare");
        List<Specializare> expectedSpecializari = Arrays.asList(spec1, spec2);

        when(specializareService.getAll()).thenReturn(expectedSpecializari);

        // When
        ResponseEntity<List<Specializare>> response = controller.getAllSpecializari();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedSpecializari, response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Informatica", response.getBody().get(0).getSpecializare());
        assertEquals("Calculatoare", response.getBody().get(1).getSpecializare());

        verify(specializareService, times(1)).getAll();
    }

    @Test
    void testGetAllSpecializariDirectCall_EmptyList() {
        // Given
        List<Specializare> emptyList = new ArrayList<>();
        when(specializareService.getAll()).thenReturn(emptyList);

        // When
        ResponseEntity<List<Specializare>> response = controller.getAllSpecializari();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emptyList, response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(specializareService, times(1)).getAll();
    }

    @Test
    void testGetAllSpecializariDirectCall_ServiceReturnsNull() {
        // Given
        when(specializareService.getAll()).thenReturn(null);

        // When
        ResponseEntity<List<Specializare>> response = controller.getAllSpecializari();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        verify(specializareService, times(1)).getAll();
    }

    @Test
    void testGetAllSpecializariDirectCall_ServiceThrowsException() {
        // Given
        when(specializareService.getAll()).thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            controller.getAllSpecializari();
        });

        verify(specializareService, times(1)).getAll();
    }

    @Test
    void testGetAllSpecializari_SpecialCharacters() throws Exception {
        // Given
        Specializare spec1 = createSpecializare(1, "Informatică Aplicată");
        Specializare spec2 = createSpecializare(2, "Matematică & Statistică");
        List<Specializare> specializariWithSpecialChars = Arrays.asList(spec1, spec2);

        when(specializareService.getAll()).thenReturn(specializariWithSpecialChars);

        // When & Then
        mockMvc.perform(get("/api/specializare/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].specializare").value("Informatică Aplicată"))
                .andExpect(jsonPath("$[1].specializare").value("Matematică & Statistică"));

        verify(specializareService, times(1)).getAll();
    }

    @Test
    void testGetAllSpecializari_LongSpecializationNames() throws Exception {
        // Given
        String longName = "Specializare cu un nume foarte lung pentru a testa comportamentul cu stringuri mari";
        Specializare spec = createSpecializare(1, longName);
        List<Specializare> longNameList = Arrays.asList(spec);

        when(specializareService.getAll()).thenReturn(longNameList);

        // When & Then
        mockMvc.perform(get("/api/specializare/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].specializare").value(longName));

        verify(specializareService, times(1)).getAll();
    }

    @Test
    void testGetAllSpecializari_HTTPMethodNotAllowed() throws Exception {
        // Given - testăm că POST nu este permis pe acest endpoint

        // When & Then
        mockMvc.perform(post("/api/specializare/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        // Verificăm că service-ul nu a fost apelat
        verify(specializareService, never()).getAll();
    }

    @Test
    void testGetAllSpecializari_HTTPMethodNotAllowedPUT() throws Exception {
        // Given - testăm că PUT nu este permis pe acest endpoint

        // When & Then
        mockMvc.perform(put("/api/specializare/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        // Verificăm că service-ul nu a fost apelat
        verify(specializareService, never()).getAll();
    }

    @Test
    void testGetAllSpecializari_HTTPMethodNotAllowedDELETE() throws Exception {
        // Given - testăm că DELETE nu este permis pe acest endpoint

        // When & Then
        mockMvc.perform(delete("/api/specializare/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        // Verificăm că service-ul nu a fost apelat
        verify(specializareService, never()).getAll();
    }

    @Test
    void testServiceDependencyInjection() {
        // Given & When & Then
        assertNotNull(controller);
        // Verificăm că service-ul a fost injectat (prin reflection sau prin verificarea că nu aruncă NPE)
        assertDoesNotThrow(() -> {
            when(specializareService.getAll()).thenReturn(new ArrayList<>());
            controller.getAllSpecializari();
        });
    }

    // Helper method pentru crearea obiectelor Specializare pentru teste
    private Specializare createSpecializare(Integer id, String nume) {
        Specializare specializare = new Specializare();
        specializare.setId(id);
        specializare.setSpecializare(nume);
        return specializare;
    }
}