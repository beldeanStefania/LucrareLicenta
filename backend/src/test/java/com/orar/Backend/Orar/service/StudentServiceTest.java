package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.ImportResultDTO;
import com.orar.Backend.Orar.dto.StudentDTO;
import com.orar.Backend.Orar.exception.StudentAlreadyExistsException;
import com.orar.Backend.Orar.exception.StudentNotFoundException;
import com.orar.Backend.Orar.model.Rol;
import com.orar.Backend.Orar.model.Specializare;
import com.orar.Backend.Orar.model.Student;
import com.orar.Backend.Orar.model.User;
import com.orar.Backend.Orar.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileInputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SpecializareRepository specializareRepository;

    @Mock
    private CatalogStudentMaterieRepository catalogStudentMaterieRepository;

    private StudentDTO studentDTO;

    @BeforeEach
    void setup() {
        studentDTO = new StudentDTO();
        studentDTO.setCod("123");
        studentDTO.setNume("Popescu");
        studentDTO.setPrenume("Ion");
        studentDTO.setAn(2);
        studentDTO.setGrupa("214");
        studentDTO.setUsername("ipopescu");
        studentDTO.setPassword("pass123");
        studentDTO.setEmail("ion@upb.ro");
        studentDTO.setSpecializare("CTI");
    }

    @Test
    void testGetAll_WithStudents() throws StudentNotFoundException {
        List<Student> students = List.of(new Student());
        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentService.getAll();
        assertEquals(1, result.size());
    }

    @Test
    void testDeleteByNameAndSurname_Success() throws StudentNotFoundException {
        Student student = new Student();
        when(studentRepository.findByNumeAndPrenume("Popescu", "Ion")).thenReturn(Optional.of(student));

        studentService.delete("Popescu", "Ion");

        verify(studentRepository).delete(student);
    }

    @Test
    void testCreateStudent_DuplicateCod_ThrowsException() {
        when(studentRepository.findByCod("123")).thenReturn(Optional.of(new Student()));

        assertThrows(StudentAlreadyExistsException.class, () -> {
            studentService.add(studentDTO);
        });
    }


    @Test
    void testImportFromCsv_InvalidRowHandled() throws Exception {
        String csv = "cod,nume,prenume,an,grupa,specializare,username,password,email\n" +
                "001,Popescu,Ion,abc,214,CTI,ipopescu,pass123,ion@upb.ro"; // "abc" nu e int

        MockMultipartFile file = new MockMultipartFile("file", "students.csv", "text/csv", csv.getBytes());

        List<ImportResultDTO> result = studentService.importFromCsv(file);
        assertFalse(result.get(0).isSuccess());
        assertTrue(result.get(0).getMessage().contains("For input string: \"abc\""));
    }


    @Test
    void testGetByGrupa_ReturnsStudents() throws StudentNotFoundException {
        List<Student> students = List.of(new Student());
        when(studentRepository.findByGrupa("214")).thenReturn(students);

        List<Student> result = studentService.getByGrupa("214");
        assertEquals(1, result.size());
    }


    @Test
    void testDeleteByNameAndSurname_NotFound() {
        when(studentRepository.findByNumeAndPrenume("Popescu", "Ion")).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.delete("Popescu", "Ion"));
    }



    @Test
    void testAdd_NewStudent_Success() throws Exception {
        when(studentRepository.findByCod("123")).thenReturn(Optional.empty());
        when(studentRepository.findByNumeAndPrenume("Popescu", "Ion")).thenReturn(Optional.empty());
        when(rolRepository.findByName("STUDENT")).thenReturn(Optional.of(new Rol()));
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(specializareRepository.findBySpecializare("CTI")).thenReturn(new Specializare());
        when(studentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Student student = studentService.add(studentDTO);

        assertEquals("Popescu", student.getNume());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testAdd_StudentAlreadyExistsByCod() {
        when(studentRepository.findByCod("123")).thenReturn(Optional.of(new Student()));

        assertThrows(StudentAlreadyExistsException.class, () -> studentService.add(studentDTO));
    }

    @Test
    void testAdd_StudentAlreadyExistsByName() {
        //when(studentRepository.findByCod("123")).thenReturn(Optional.empty());
        when(studentRepository.findByNumeAndPrenume("Popescu", "Ion")).thenReturn(Optional.of(new Student()));

        assertThrows(StudentAlreadyExistsException.class, () -> studentService.add(studentDTO));
    }

    @Test
    void testGetAll_NoStudentsFound_Throws() {
        when(studentRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(StudentNotFoundException.class, () -> studentService.getAll());
    }

    @Test
    void testUpdate_Success() throws StudentNotFoundException {
        Student existing = new Student();
        existing.setCod("123");

        when(studentRepository.findByCod("123")).thenReturn(Optional.of(existing));
        when(studentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Student updated = studentService.update("123", studentDTO);
        assertEquals("Popescu", updated.getNume());
    }

    @Test
    void testUpdate_StudentNotFound() {
        when(studentRepository.findByCod("123")).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> studentService.update("123", studentDTO));
    }

    @Test
    void testDeleteByCod_Success() throws StudentNotFoundException {
        Student student = new Student();
        when(studentRepository.findByCod("123")).thenReturn(Optional.of(student));

        studentService.deleteByCod("123");

        verify(studentRepository).delete(student);
    }

    @Test
    void testDeleteByCod_NotFound() {
        when(studentRepository.findByCod("123")).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> studentService.deleteByCod("123"));
    }

    @Test
    void testImportFromCsv() throws Exception {
        FileInputStream inputStream = new FileInputStream("src/test/resources/students.csv");
        MockMultipartFile file = new MockMultipartFile("file", "students.csv", "text/csv", inputStream);

        when(studentRepository.findByCod(any())).thenReturn(Optional.empty());
        when(studentRepository.findByNumeAndPrenume(any(), any())).thenReturn(Optional.empty());
        when(rolRepository.findByName("STUDENT")).thenReturn(Optional.of(new Rol()));
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(specializareRepository.findBySpecializare(any())).thenReturn(new Specializare());
        when(studentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        List<ImportResultDTO> results = studentService.importFromCsv(file);

        assertEquals(2, results.size());
        assertTrue(results.get(0).isSuccess());
    }

    @Test
    void testGetByAnAndGrupa_Found() throws StudentNotFoundException {
        Student student = new Student();
        when(studentRepository.findByAnAndGrupa(2, "214")).thenReturn(Optional.of(student));

        Student result = studentService.getByAnAndGrupa(2, "214");
        assertNotNull(result);
    }

    @Test
    void testGetByAnAndGrupa_NotFound() {
        when(studentRepository.findByAnAndGrupa(2, "214")).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> studentService.getByAnAndGrupa(2, "214"));
    }
}
