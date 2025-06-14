package com.orar.Backend.Orar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orar.Backend.Orar.dto.ContractDTO;
import com.orar.Backend.Orar.dto.ContractYearRequest;
import com.orar.Backend.Orar.enums.Tip;
import com.orar.Backend.Orar.exception.ValidationException;
import com.orar.Backend.Orar.service.StudentContractService;
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

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StudentContractControllerTest {

    @Mock
    private StudentContractService contractService;

    @InjectMocks
    private StudentContractController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAvailableCourses_Success() throws Exception {
        // Given
        String studentCod = "ST001";
        int anContract = 2024;
        int semestru = 1;

        List<ContractDTO> expectedCourses = Arrays.asList(
                new ContractDTO("MAT101", "Matematica", 6, 1, Tip.OBLIGATORIE, false, null),
                new ContractDTO("INF102", "Informatica", 5, 1, Tip.OBLIGATORIE, false, null)
        );

        when(contractService.getAvailableCoursesForContract(studentCod, anContract, semestru))
                .thenReturn(expectedCourses);

        // When & Then
        mockMvc.perform(get("/api/studentContract/availableCourses/{studentCod}/{anContract}/{semestru}",
                        studentCod, anContract, semestru))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].cod").value("MAT101"))
                .andExpect(jsonPath("$[0].nume").value("Matematica"))
                .andExpect(jsonPath("$[0].credite").value(6))
                .andExpect(jsonPath("$[1].cod").value("INF102"));

        verify(contractService).getAvailableCoursesForContract(studentCod, anContract, semestru);
    }

    @Test
    void testGetAvailableCourses_ServiceException() throws Exception {
        // Given
        String studentCod = "ST001";
        int anContract = 2024;
        int semestru = 1;

        when(contractService.getAvailableCoursesForContract(studentCod, anContract, semestru))
                .thenThrow(new RuntimeException("Database error"));

        // When & Then
        mockMvc.perform(get("/api/studentContract/availableCourses/{studentCod}/{anContract}/{semestru}",
                        studentCod, anContract, semestru))
                .andExpect(status().isInternalServerError());

        verify(contractService).getAvailableCoursesForContract(studentCod, anContract, semestru);
    }

    @Test
    void testGenerateContract_Success() throws Exception {
        // Given
        ContractYearRequest request = new ContractYearRequest();
        request.setStudentCod("ST001");
        request.setAnContract(2024);
        request.setCoduriMaterii(Arrays.asList("MAT101", "INF102"));

        byte[] mockPdf = "Mock PDF content".getBytes();

        when(contractService.generateContractFromSelection(
                request.getStudentCod(),
                request.getAnContract(),
                request.getCoduriMaterii()))
                .thenReturn(mockPdf);

        // When & Then
        mockMvc.perform(post("/api/studentContract/generateContract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition",
                        "attachment; filename=\"contract_ST001.pdf\""))
                .andExpect(header().string("Content-Type", "application/pdf"))
                .andExpect(content().bytes(mockPdf));

        verify(contractService).generateContractFromSelection(
                request.getStudentCod(),
                request.getAnContract(),
                request.getCoduriMaterii());
    }

    @Test
    void testGenerateContract_ValidationException() throws Exception {
        // Given
        ContractYearRequest request = new ContractYearRequest();
        request.setStudentCod("ST001");
        request.setAnContract(2024);
        request.setCoduriMaterii(Arrays.asList("MAT101"));

        when(contractService.generateContractFromSelection(
                request.getStudentCod(),
                request.getAnContract(),
                request.getCoduriMaterii()))
                .thenThrow(new ValidationException("Validation failed"));

        // When & Then
        mockMvc.perform(post("/api/studentContract/generateContract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Validation failed"));

        verify(contractService).generateContractFromSelection(
                request.getStudentCod(),
                request.getAnContract(),
                request.getCoduriMaterii());
    }

    @Test
    void testGenerateContract_IllegalArgumentException() throws Exception {
        // Given
        ContractYearRequest request = new ContractYearRequest();
        request.setStudentCod("ST001");
        request.setAnContract(2024);
        request.setCoduriMaterii(Arrays.asList("INVALID"));

        when(contractService.generateContractFromSelection(
                request.getStudentCod(),
                request.getAnContract(),
                request.getCoduriMaterii()))
                .thenThrow(new IllegalArgumentException("Invalid course code"));

        // When & Then
        mockMvc.perform(post("/api/studentContract/generateContract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid course code"));

        verify(contractService).generateContractFromSelection(
                request.getStudentCod(),
                request.getAnContract(),
                request.getCoduriMaterii());
    }

    @Test
    void testGenerateContract_InternalServerError() throws Exception {
        // Given
        ContractYearRequest request = new ContractYearRequest();
        request.setStudentCod("ST001");
        request.setAnContract(2024);
        request.setCoduriMaterii(Arrays.asList("MAT101"));

        when(contractService.generateContractFromSelection(
                request.getStudentCod(),
                request.getAnContract(),
                request.getCoduriMaterii()))
                .thenThrow(new RuntimeException("Unexpected error"));

        // When & Then
        mockMvc.perform(post("/api/studentContract/generateContract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Eroare la generare."));

        verify(contractService).generateContractFromSelection(
                request.getStudentCod(),
                request.getAnContract(),
                request.getCoduriMaterii());
    }

    @Test
    void testPreviewContract_Success() throws Exception {
        // Given
        ContractYearRequest request = new ContractYearRequest();
        request.setStudentCod("ST001");
        request.setAnContract(2024);
        request.setCoduriMaterii(Arrays.asList("MAT101"));

        byte[] mockPdf = "Mock PDF content".getBytes();

        when(contractService.generateContractPdfWithoutPersist(
                request.getStudentCod(),
                request.getAnContract(),
                request.getCoduriMaterii()))
                .thenReturn(mockPdf);

        // When & Then - folosim MockMvc în loc de mock HttpServletResponse
        mockMvc.perform(post("/api/studentContract/preview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(contractService).generateContractPdfWithoutPersist(
                request.getStudentCod(),
                request.getAnContract(),
                request.getCoduriMaterii());
    }

    @Test
    void testPreviewContract_Exception() throws Exception {
        // Given
        ContractYearRequest request = new ContractYearRequest();
        request.setStudentCod("ST001");
        request.setAnContract(2024);
        request.setCoduriMaterii(Arrays.asList("MAT101"));

        when(contractService.generateContractPdfWithoutPersist(
                request.getStudentCod(),
                request.getAnContract(),
                request.getCoduriMaterii()))
                .thenThrow(new RuntimeException("Service error"));

        // When & Then
        mockMvc.perform(post("/api/studentContract/preview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());

        verify(contractService).generateContractPdfWithoutPersist(
                request.getStudentCod(),
                request.getAnContract(),
                request.getCoduriMaterii());
    }

    @Test
    void testGetContractCourses_Success() throws Exception {
        // Given
        String studentCod = "ST001";
        int anContract = 2024;

        List<ContractDTO> expectedCourses = Arrays.asList(
                new ContractDTO("MAT101", "Matematica", 6, 1, Tip.OBLIGATORIE, true, null),
                new ContractDTO("INF102", "Informatica", 5, 1, Tip.OBLIGATORIE, true, null)
        );

        when(contractService.getContractCourses(studentCod, anContract))
                .thenReturn(expectedCourses);

        // When & Then
        mockMvc.perform(get("/api/studentContract/contractCourses/{studentCod}/{anContract}",
                        studentCod, anContract))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].cod").value("MAT101"))
                .andExpect(jsonPath("$[0].selected").value(true))
                .andExpect(jsonPath("$[1].cod").value("INF102"));

        verify(contractService).getContractCourses(studentCod, anContract);
    }

    @Test
    void testGetContractCourses_Exception() throws Exception {
        // Given
        String studentCod = "ST001";
        int anContract = 2024;

        when(contractService.getContractCourses(studentCod, anContract))
                .thenThrow(new RuntimeException("Database error"));

        // When & Then
        mockMvc.perform(get("/api/studentContract/contractCourses/{studentCod}/{anContract}",
                        studentCod, anContract))
                .andExpect(status().isInternalServerError());

        verify(contractService).getContractCourses(studentCod, anContract);
    }

    @Test
    void testExistsContract_True() throws Exception {
        // Given
        String studentCod = "ST001";
        int anContract = 2024;

        when(contractService.hasContract(studentCod, anContract))
                .thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/studentContract/exists/{studentCod}/{anContract}",
                        studentCod, anContract))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("true"));

        verify(contractService).hasContract(studentCod, anContract);
    }

    @Test
    void testExistsContract_False() throws Exception {
        // Given
        String studentCod = "ST001";
        int anContract = 2024;

        when(contractService.hasContract(studentCod, anContract))
                .thenReturn(false);

        // When & Then
        mockMvc.perform(get("/api/studentContract/exists/{studentCod}/{anContract}",
                        studentCod, anContract))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("false"));

        verify(contractService).hasContract(studentCod, anContract);
    }

    // Unit tests pentru metodele direct din controller (fără MockMvc)
    @Test
    void testGetAvailableCoursesDirectCall_Success() {
        // Given
        String studentCod = "ST001";
        int anContract = 2024;
        int semestru = 1;

        List<ContractDTO> expectedCourses = Arrays.asList(
                new ContractDTO("MAT101", "Matematica", 6, 1, Tip.OBLIGATORIE, false, null)
        );

        when(contractService.getAvailableCoursesForContract(studentCod, anContract, semestru))
                .thenReturn(expectedCourses);

        // When
        ResponseEntity<List<ContractDTO>> response = controller.getAvailableCourses(studentCod, anContract, semestru);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCourses, response.getBody());
        verify(contractService).getAvailableCoursesForContract(studentCod, anContract, semestru);
    }

    @Test
    void testGetAvailableCoursesDirectCall_Exception() {
        // Given
        String studentCod = "ST001";
        int anContract = 2024;
        int semestru = 1;

        when(contractService.getAvailableCoursesForContract(studentCod, anContract, semestru))
                .thenThrow(new RuntimeException("Service error"));

        // When
        ResponseEntity<List<ContractDTO>> response = controller.getAvailableCourses(studentCod, anContract, semestru);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(contractService).getAvailableCoursesForContract(studentCod, anContract, semestru);
    }

    @Test
    void testExistsContractDirectCall() {
        // Given
        String studentCod = "ST001";
        int anContract = 2024;

        when(contractService.hasContract(studentCod, anContract))
                .thenReturn(true);

        // When
        ResponseEntity<Boolean> response = controller.existsContract(studentCod, anContract);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
        verify(contractService).hasContract(studentCod, anContract);
    }
}