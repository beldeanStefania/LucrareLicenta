package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.ImportResultDTO;
import com.orar.Backend.Orar.dto.ProfesorDTO;
import com.orar.Backend.Orar.exception.ProfesorDoesNotExistException;
import com.orar.Backend.Orar.exception.ProfesorNotFoundException;
import com.orar.Backend.Orar.model.Profesor;
import com.orar.Backend.Orar.model.Rol;
import com.orar.Backend.Orar.model.User;
import com.orar.Backend.Orar.repository.ProfesorRepository;
import com.orar.Backend.Orar.repository.RolRepository;
import com.orar.Backend.Orar.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfesorServiceTest {

    @Mock
    private ProfesorRepository profesorRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private RolRepository rolRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ProfesorService service;

    private Rol profesorRole;
    private ProfesorDTO addDto;

    @BeforeEach
    void setup() {
        profesorRole = new Rol();
        profesorRole.setId(1);
        profesorRole.setName("PROFESOR");

        addDto = new ProfesorDTO();
        addDto.setNume("Popescu");
        addDto.setPrenume("Ion");
        addDto.setUsername("ionp");
        addDto.setPassword("secret");
        addDto.setEmail("ion@example.com");
    }

    @Test
    @DisplayName("getAll should return all profesores")
    void testGetAll() {
        Profesor p = new Profesor();
        when(profesorRepo.findAll()).thenReturn(List.of(p));

        List<Profesor> result = service.getAll();
        assertEquals(1, result.size());
        assertSame(p, result.get(0));
        verify(profesorRepo).findAll();
    }

    @Test
    @DisplayName("add should create a new Profesor with user and role")
    void testAddSuccess() {
        when(rolRepo.findByName("PROFESOR")).thenReturn(Optional.of(profesorRole));
        when(passwordEncoder.encode("secret")).thenReturn("ENC_SECRET");
        // userRepo.save returns the same user
        ArgumentCaptor<User> userCap = ArgumentCaptor.forClass(User.class);
        when(userRepo.save(userCap.capture())).thenAnswer(i -> i.getArgument(0));
        ArgumentCaptor<Profesor> profCap = ArgumentCaptor.forClass(Profesor.class);
        Profesor savedProfesor = new Profesor();
        when(profesorRepo.save(profCap.capture())).thenReturn(savedProfesor);

        Profesor result = service.add(addDto);
        assertSame(savedProfesor, result);

        User createdUser = userCap.getValue();
        assertEquals("ionp", createdUser.getUsername());
        assertEquals("ENC_SECRET", createdUser.getPassword());
        assertEquals(profesorRole, createdUser.getRole());
        assertEquals("ion@example.com", createdUser.getEmail());

        Profesor createdProf = profCap.getValue();
        assertEquals("Popescu", createdProf.getNume());
        assertEquals("Ion", createdProf.getPrenume());
        assertSame(createdUser, createdProf.getUser());
    }

    @Test
    @DisplayName("add should fail if PROFESOR role not found")
    void testAddRoleNotFound() {
        when(rolRepo.findByName("PROFESOR")).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.add(addDto));
        assertTrue(ex.getMessage().contains("Rolul PROFESOR nu există"));
    }

    @Test
    @DisplayName("update should modify existing Profesor and User when no username/password change")
    void testUpdateNoChange() throws Exception {
        Profesor existing = new Profesor();
        existing.setId(10);
        existing.setNume("Old");
        existing.setPrenume("OldPren");
        User user = new User();
        user.setUsername("ionp");
        user.setPassword("ENC_OLD");
        existing.setUser(user);

        when(profesorRepo.findById(10)).thenReturn(Optional.of(existing));
        when(userRepo.save(user)).thenReturn(user);
        when(profesorRepo.save(existing)).thenReturn(existing);

        ProfesorDTO dto = new ProfesorDTO();
        dto.setNume("NewName");
        dto.setPrenume("NewPren");
        dto.setUsername("ionp");  // same
        dto.setPassword("");      // empty => no change
        dto.setEmail("ignored@example.com");

        Profesor result = service.update(10, dto);
        assertSame(existing, result);
        assertEquals("NewName", existing.getNume());
        assertEquals("NewPren", existing.getPrenume());
        // user unchanged
        assertEquals("ionp", user.getUsername());
        assertEquals("ENC_OLD", user.getPassword());
        verify(userRepo).save(user);
        verify(profesorRepo).save(existing);
    }

    @Test
    @DisplayName("update should change username and password when provided")
    void testUpdateWithChanges() throws Exception {
        Profesor existing = new Profesor();
        existing.setId(11);
        existing.setNume("Old");
        existing.setPrenume("OldPren");
        User user = new User();
        user.setUsername("olduser");
        user.setPassword("ENC_OLD");
        existing.setUser(user);

        when(profesorRepo.findById(11)).thenReturn(Optional.of(existing));
        when(passwordEncoder.encode("newpass")).thenReturn("ENC_NEW");
        when(userRepo.save(user)).thenReturn(user);
        when(profesorRepo.save(existing)).thenReturn(existing);

        ProfesorDTO dto = new ProfesorDTO();
        dto.setNume("New");
        dto.setPrenume("NewPren");
        dto.setUsername("newuser");
        dto.setPassword("newpass");
        dto.setEmail("ignored@example.com");

        Profesor result = service.update(11, dto);
        assertSame(existing, result);
        // fields updated
        assertEquals("New", existing.getNume());
        assertEquals("NewPren", existing.getPrenume());
        assertEquals("newuser", user.getUsername());
        assertEquals("ENC_NEW", user.getPassword());
        verify(userRepo).save(user);
        verify(profesorRepo).save(existing);
    }

    @Test
    @DisplayName("update should throw when Profesor not found")
    void testUpdateNotFound() {
        when(profesorRepo.findById(99)).thenReturn(Optional.empty());
        ProfesorDTO dto = new ProfesorDTO();
        assertThrows(ProfesorNotFoundException.class, () -> service.update(99, dto));
    }

    @Test
    @DisplayName("delete should remove existing Profesor")
    void testDeleteSuccess() throws Exception {
        Profesor p = new Profesor();
        when(profesorRepo.findByNumeAndPrenume("Popescu", "Ion")).thenReturn(Optional.of(p));
        service.delete("Popescu", "Ion");
        verify(profesorRepo).delete(p);
    }

    @Test
    @DisplayName("delete should throw when Profesor not found")
    void testDeleteNotFound() {
        when(profesorRepo.findByNumeAndPrenume("X", "Y")).thenReturn(Optional.empty());
        assertThrows(ProfesorDoesNotExistException.class, () -> service.delete("X", "Y"));
    }

    @Test
    @DisplayName("importFromCsv should report successes and failures per row")
    void testImportFromCsvMixed() throws Exception {
        String csv = "nume,prenume,username,password,email\n" +
                "Popescu,Ion,ionp,pass,ion@x.com\n" +
                "Doe,Jane,jane,pass,jane@x.com\n";
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(csv.getBytes()));

        // spy service to stub add
        ProfesorService spySvc = spy(service);
        doAnswer(invocation -> {
            ProfesorDTO dto = invocation.getArgument(0);
            if ("Doe".equals(dto.getNume()) && "Jane".equals(dto.getPrenume())) {
                throw new RuntimeException("User exists");
            }
            return new Profesor();
        }).when(spySvc).add(any(ProfesorDTO.class));

        List<ImportResultDTO> results = spySvc.importFromCsv(file);
        assertEquals(2, results.size());

        ImportResultDTO r1 = results.get(0);
        assertTrue(r1.isSuccess());
        assertEquals("OK", r1.getMessage());
        assertEquals(2, r1.getRow());

        ImportResultDTO r2 = results.get(1);
        assertFalse(r2.isSuccess());
        assertEquals("User exists", r2.getMessage());
        assertEquals(3, r2.getRow());
    }

    @Test
    @DisplayName("importFromCsv should throw RuntimeException on IOException")
    void testImportFromCsvIOException() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenThrow(new IOException("fail"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.importFromCsv(file));
        assertTrue(ex.getMessage().contains("Nu am putut citi fișierul"));
    }
}
