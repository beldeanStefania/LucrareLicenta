//package com.orar.Backend.Orar.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.orar.Backend.Orar.dto.CatalogStudentMaterieDTO;
//import com.orar.Backend.Orar.exception.CatalogStudentMaterieNotFoundException;
//import com.orar.Backend.Orar.exception.MaterieNotFoundException;
//import com.orar.Backend.Orar.exception.StudentNotFoundException;
//import com.orar.Backend.Orar.model.CatalogStudentMaterie;
//import com.orar.Backend.Orar.service.CatalogStudentMaterieService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Arrays;
//import java.util.Collections;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(CatalogStudentMaterieController.class)
//public class CatalogStudentMaterieControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private CatalogStudentMaterieService service;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void testGetAll() throws Exception {
//        CatalogStudentMaterieDTO dto = new CatalogStudentMaterieDTO();
//        Mockito.when(service.getAll()).thenReturn(Collections.singletonList(dto));
//
//        mockMvc.perform(get("/api/catalogStudentMaterie/getAll"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(1));
//    }
//
//    @Test
//    void testGetNoteByStudent() throws Exception {
//        String cod = "S001";
//        CatalogStudentMaterieDTO dto = new CatalogStudentMaterieDTO();
//        Mockito.when(service.getNoteByStudent(cod)).thenReturn(Collections.singletonList(dto));
//
//        mockMvc.perform(get("/api/catalogStudentMaterie/getNote/{studentCod}", cod))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(1));
//    }
//
//    @Test
//    void testAddCatalogSuccess() throws Exception {
//        CatalogStudentMaterieDTO dto = new CatalogStudentMaterieDTO();
//        CatalogStudentMaterie entity = new CatalogStudentMaterie();
//        Mockito.when(service.addOrUpdate(any())).thenReturn(entity);
//
//        mockMvc.perform(post("/api/catalogStudentMaterie/add")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    void testAddCatalogNotFound() throws Exception {
//        CatalogStudentMaterieDTO dto = new CatalogStudentMaterieDTO();
//        Mockito.when(service.addOrUpdate(any())).thenThrow(StudentNotFoundException.class);
//
//        mockMvc.perform(post("/api/catalogStudentMaterie/add")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void testUpdateCatalogSuccess() throws Exception {
//        CatalogStudentMaterieDTO dto = new CatalogStudentMaterieDTO();
//        CatalogStudentMaterie updated = new CatalogStudentMaterie();
//
//        Mockito.when(service.update(eq("S001"), eq("M001"), any())).thenReturn(updated);
//
//        mockMvc.perform(put("/api/catalogStudentMaterie/update/S001/M001")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testUpdateCatalogNotFound() throws Exception {
//        CatalogStudentMaterieDTO dto = new CatalogStudentMaterieDTO();
//        Mockito.when(service.update(eq("S001"), eq("M001"), any())).thenThrow(CatalogStudentMaterieNotFoundException.class);
//
//        mockMvc.perform(put("/api/catalogStudentMaterie/update/S001/M001")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void testDeleteCatalogSuccess() throws Exception {
//        mockMvc.perform(delete("/api/catalogStudentMaterie/delete/S001/M001"))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void testDeleteCatalogNotFound() throws Exception {
//        Mockito.doThrow(new CatalogStudentMaterieNotFoundException("mesaj")).when(service).deleteByStudentAndMaterie("S001", "M001");
//
//        mockMvc.perform(delete("/api/catalogStudentMaterie/delete/S001/M001"))
//                .andExpect(status().isNotFound());
//    }
//}
