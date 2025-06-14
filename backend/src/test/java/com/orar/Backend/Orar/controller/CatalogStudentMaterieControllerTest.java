package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.CatalogStudentMaterieDTO;
import com.orar.Backend.Orar.exception.CatalogStudentMaterieNotFoundException;
import com.orar.Backend.Orar.exception.MaterieNotFoundException;
import com.orar.Backend.Orar.exception.StudentNotFoundException;
import com.orar.Backend.Orar.model.CatalogStudentMaterie;
import com.orar.Backend.Orar.service.CatalogStudentMaterieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
class CatalogStudentMaterieControllerTest {

    @Mock
    private CatalogStudentMaterieService catalogStudentMaterieService;

    @InjectMocks
    private CatalogStudentMaterieController controller;

    @Test
    void getAll_ShouldReturnList() {
        List<CatalogStudentMaterieDTO> expected = new ArrayList<>();
        when(catalogStudentMaterieService.getAll()).thenReturn(expected);

        List<CatalogStudentMaterieDTO> actual = controller.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void getNoteByStudent_ShouldReturnList() {
        List<CatalogStudentMaterieDTO> expected = new ArrayList<>();
        when(catalogStudentMaterieService.getNoteByStudent("123")).thenReturn(expected);

        ResponseEntity<List<CatalogStudentMaterieDTO>> response = controller.getNoteByStudent("123");

        assertEquals(OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void addCatalog_ShouldReturnCreated() throws StudentNotFoundException, MaterieNotFoundException {
        CatalogStudentMaterieDTO dto = new CatalogStudentMaterieDTO();
        CatalogStudentMaterie expected = new CatalogStudentMaterie();

        when(catalogStudentMaterieService.addOrUpdate(dto)).thenReturn(expected);

        ResponseEntity<CatalogStudentMaterie> response = controller.addCatalog(dto);

        assertEquals(CREATED, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void addCatalog_ShouldReturnNotFound() throws StudentNotFoundException, MaterieNotFoundException {
        CatalogStudentMaterieDTO dto = new CatalogStudentMaterieDTO();
        when(catalogStudentMaterieService.addOrUpdate(dto)).thenThrow(StudentNotFoundException.class);

        ResponseEntity<CatalogStudentMaterie> response = controller.addCatalog(dto);

        assertEquals(NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateCatalog_ShouldReturnOk() throws CatalogStudentMaterieNotFoundException, StudentNotFoundException, MaterieNotFoundException {
        CatalogStudentMaterieDTO dto = new CatalogStudentMaterieDTO();
        CatalogStudentMaterie expected = new CatalogStudentMaterie();

        when(catalogStudentMaterieService.update("123", "MAT01", dto)).thenReturn(expected);

        ResponseEntity<CatalogStudentMaterie> response = controller.updateCatalog("123", "MAT01", dto);

        assertEquals(OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void updateCatalog_ShouldReturnNotFound() throws CatalogStudentMaterieNotFoundException, StudentNotFoundException, MaterieNotFoundException {
        CatalogStudentMaterieDTO dto = new CatalogStudentMaterieDTO();
        when(catalogStudentMaterieService.update("123", "MAT01", dto)).thenThrow(CatalogStudentMaterieNotFoundException.class);

        ResponseEntity<CatalogStudentMaterie> response = controller.updateCatalog("123", "MAT01", dto);

        assertEquals(NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteCatalog_ShouldReturnNoContent() throws CatalogStudentMaterieNotFoundException {
        ResponseEntity<Void> response = controller.deleteCatalog("123", "MAT01");

        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(catalogStudentMaterieService, times(1)).deleteByStudentAndMaterie("123", "MAT01");
    }

    @Test
    void deleteCatalog_ShouldReturnNotFound() throws CatalogStudentMaterieNotFoundException {
        doThrow(CatalogStudentMaterieNotFoundException.class)
                .when(catalogStudentMaterieService).deleteByStudentAndMaterie("123", "MAT01");

        ResponseEntity<Void> response = controller.deleteCatalog("123", "MAT01");

        assertEquals(NOT_FOUND, response.getStatusCode());
    }
}
