package com.orar.Backend.Orar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orar.Backend.Orar.dto.ImportResultDTO;
import com.orar.Backend.Orar.dto.StudentDTO;
import com.orar.Backend.Orar.exception.StudentAlreadyExistsException;
import com.orar.Backend.Orar.exception.StudentNotFoundException;
import com.orar.Backend.Orar.model.Student;
import com.orar.Backend.Orar.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Student Controller Tests")
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Student testStudent;
    private StudentDTO testStudentDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
        objectMapper = new ObjectMapper();

        // Crearea unui student de test
        testStudent = new Student();
        testStudent.setId(1);
        testStudent.setCod("ST001");
        testStudent.setNume("Popescu");
        testStudent.setPrenume("Ion");
        testStudent.setAn(2);
        testStudent.setGrupa("30231");

        // Crearea unui StudentDTO de test
        testStudentDTO = new StudentDTO();
        testStudentDTO.setCod("ST001");
        testStudentDTO.setNume("Popescu");
        testStudentDTO.setPrenume("Ion");
        testStudentDTO.setAn(2);
        testStudentDTO.setGrupa("30231");
        testStudentDTO.setUsername("ion.popescu");
        testStudentDTO.setPassword("password123");
        testStudentDTO.setSpecializare("Informatica");
        testStudentDTO.setEmail("ion.popescu@student.ubbcluj.ro");
    }

    @Test
    @DisplayName("GET /getAllStudents - Success")
    void getAllStudents_Success() throws Exception {
        // Given
        List<Student> students = Arrays.asList(testStudent);
        when(studentService.getAll()).thenReturn(students);

        // When & Then
        mockMvc.perform(get("/api/student/getAllStudents"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].cod").value("ST001"))
                .andExpect(jsonPath("$[0].nume").value("Popescu"));

        verify(studentService, times(1)).getAll();
    }

    @Test
    @DisplayName("GET /getAllStudents - No students found")
    void getAllStudents_NotFound() throws Exception {
        // Given
        when(studentService.getAll()).thenThrow(new StudentNotFoundException("Nu s-au găsit studenți"));

        // When & Then
        mockMvc.perform(get("/api/student/getAllStudents"))
                .andExpect(status().isNotFound());

        verify(studentService, times(1)).getAll();
    }

    @Test
    @DisplayName("GET /getAllStudents - Returns empty list when students is null")
    void getAllStudents_ReturnsEmptyListWhenNull() throws StudentNotFoundException {
        // Given
        when(studentService.getAll()).thenReturn(null);

        // When
        ResponseEntity<List<Student>> response = studentController.getAllStudents();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("GET /getByAnAndGrupa/{an}/{grupa} - Success")
    void getStudentByAnAndGrupa_Success() throws Exception {
        // Given
        when(studentService.getByAnAndGrupa(2, "30231")).thenReturn(testStudent);

        // When & Then
        mockMvc.perform(get("/api/student/getByAnAndGrupa/2/30231"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cod").value("ST001"))
                .andExpect(jsonPath("$.an").value(2))
                .andExpect(jsonPath("$.grupa").value("30231"));

        verify(studentService, times(1)).getByAnAndGrupa(2, "30231");
    }

    @Test
    @DisplayName("GET /getByAnAndGrupa/{an}/{grupa} - Not Found")
    void getStudentByAnAndGrupa_NotFound() throws Exception {
        // Given
        when(studentService.getByAnAndGrupa(2, "30231"))
                .thenThrow(new StudentNotFoundException("Student nu a fost găsit"));

        // When & Then
        mockMvc.perform(get("/api/student/getByAnAndGrupa/2/30231"))
                .andExpect(status().isNotFound());

        verify(studentService, times(1)).getByAnAndGrupa(2, "30231");
    }

    @Test
    @DisplayName("GET /getByGrupa/{grupa} - Success")
    void getStudentByGrupa_Success() throws Exception {
        // Given
        List<Student> students = Arrays.asList(testStudent);
        when(studentService.getByGrupa("30231")).thenReturn(students);

        // When & Then
        mockMvc.perform(get("/api/student/getByGrupa/30231"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].grupa").value("30231"))
                .andExpect(jsonPath("$[0].id").value(1));

        verify(studentService, times(1)).getByGrupa("30231");
    }

    @Test
    @DisplayName("GET /getByGrupa/{grupa} - Not Found")
    void getStudentByGrupa_NotFound() throws Exception {
        // Given
        when(studentService.getByGrupa("30231"))
                .thenThrow(new StudentNotFoundException("Nu s-au găsit studenți în grupa specificată"));

        // When & Then
        mockMvc.perform(get("/api/student/getByGrupa/30231"))
                .andExpect(status().isNotFound());

        verify(studentService, times(1)).getByGrupa("30231");
    }

    @Test
    @DisplayName("POST /add - Success")
    void createStudent_Success() throws Exception {
        // Given
        when(studentService.add(any(StudentDTO.class))).thenReturn(testStudent);

        // When & Then
        mockMvc.perform(post("/api/student/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStudentDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cod").value("ST001"))
                .andExpect(jsonPath("$.id").value(1));

        verify(studentService, times(1)).add(any(StudentDTO.class));
    }

    @Test
    @DisplayName("POST /add - Student Already Exists")
    void createStudent_AlreadyExists() throws Exception {
        // Given
        when(studentService.add(any(StudentDTO.class)))
                .thenThrow(new StudentAlreadyExistsException("Studentul exista deja"));

        // When & Then
        mockMvc.perform(post("/api/student/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStudentDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Studentul exista deja"));

        verify(studentService, times(1)).add(any(StudentDTO.class));
    }

    @Test
    @DisplayName("PUT /update/{cod} - Success")
    void updateStudent_Success() throws Exception {
        // Given
        Student updatedStudent = new Student();
        updatedStudent.setId(1);
        updatedStudent.setCod("ST001");
        updatedStudent.setNume("Popescu Updated");
        when(studentService.update(eq("ST001"), any(StudentDTO.class))).thenReturn(updatedStudent);

        // When & Then
        mockMvc.perform(put("/api/student/update/ST001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStudentDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cod").value("ST001"))
                .andExpect(jsonPath("$.nume").value("Popescu Updated"));

        verify(studentService, times(1)).update(eq("ST001"), any(StudentDTO.class));
    }

    @Test
    @DisplayName("DELETE /delete/{numeStudent}/{prenumeStudent} - Success")
    void deleteStudentByName_Success() throws Exception {
        // Given
        doNothing().when(studentService).delete("Popescu", "Ion");

        // When & Then
        mockMvc.perform(delete("/api/student/delete/Popescu/Ion"))
                .andExpect(status().isOk());

        verify(studentService, times(1)).delete("Popescu", "Ion");
    }

    @Test
    @DisplayName("DELETE /delete/{numeStudent}/{prenumeStudent} - Not Found")
    void deleteStudentByName_NotFound() throws Exception {
        // Given
        doThrow(new StudentNotFoundException("Student nu a fost găsit"))
                .when(studentService).delete("Popescu", "Ion");

        // When & Then
        mockMvc.perform(delete("/api/student/delete/Popescu/Ion"))
                .andExpect(status().isNotFound());

        verify(studentService, times(1)).delete("Popescu", "Ion");
    }

    @Test
    @DisplayName("DELETE /delete/{cod} - Success")
    void deleteStudentByCod_Success() throws Exception {
        // Given
        doNothing().when(studentService).deleteByCod("ST001");

        // When & Then
        mockMvc.perform(delete("/api/student/delete/ST001"))
                .andExpect(status().isOk());

        verify(studentService, times(1)).deleteByCod("ST001");
    }

    @Test
    @DisplayName("DELETE /delete/{cod} - Not Found")
    void deleteStudentByCod_NotFound() throws Exception {
        // Given
        doThrow(new StudentNotFoundException("Student nu a fost găsit"))
                .when(studentService).deleteByCod("ST001");

        // When & Then
        mockMvc.perform(delete("/api/student/delete/ST001"))
                .andExpect(status().isNotFound());

        verify(studentService, times(1)).deleteByCod("ST001");
    }

    @Test
    @DisplayName("POST /import - Success")
    void importStudents_Success() throws Exception {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "students.csv",
                "text/csv",
                "cod,nume,prenume,an,grupa,username,password,specializare,email\nST001,Popescu,Ion,2,30231,ion.popescu,password123,Informatica,ion.popescu@student.ubbcluj.ro".getBytes()
        );

        ImportResultDTO result = new ImportResultDTO();
        result.setRow(1);
        result.setSuccess(true);
        result.setMessage("Student importat cu succes");

        List<ImportResultDTO> importResults = Arrays.asList(result);
        when(studentService.importFromCsv(any())).thenReturn(importResults);

        // When & Then
        mockMvc.perform(multipart("/api/student/import")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].row").value(1))
                .andExpect(jsonPath("$[0].success").value(true));

        verify(studentService, times(1)).importFromCsv(any());
    }

    @Test
    @DisplayName("Test Response Entity Methods")
    void testResponseEntityMethods() throws StudentNotFoundException {
        // Given
        when(studentService.getAll()).thenReturn(Arrays.asList(testStudent));

        // When
        ResponseEntity<List<Student>> response = studentController.getAllStudents();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("ST001", response.getBody().get(0).getCod());
        assertEquals(Integer.valueOf(1), response.getBody().get(0).getId());
    }

    @Test
    @DisplayName("Test Direct Controller Methods - getStudentByAnAndGrupa")
    void testDirectControllerCall_getStudentByAnAndGrupa() throws StudentNotFoundException {
        // Given
        when(studentService.getByAnAndGrupa(2, "30231")).thenReturn(testStudent);

        // When
        ResponseEntity<Student> response = studentController.getStudentByAnAndGrupa(2, "30231");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ST001", response.getBody().getCod());
        assertEquals(Integer.valueOf(1), response.getBody().getId());
    }

    @Test
    @DisplayName("Test Direct Controller Methods - createStudent")
    void testDirectControllerCall_createStudent() throws StudentNotFoundException {
        // Given
        when(studentService.add(any(StudentDTO.class))).thenReturn(testStudent);

        // When
        ResponseEntity<?> response = studentController.createStudent(testStudentDTO);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testStudent, response.getBody());
    }
}