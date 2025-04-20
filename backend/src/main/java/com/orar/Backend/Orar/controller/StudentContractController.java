package com.orar.Backend.Orar.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import com.orar.Backend.Orar.dto.MaterieDTO;
import com.orar.Backend.Orar.service.StudentContractService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/studentContract")
public class StudentContractController {

    @Autowired
    private StudentContractService contractService;


    @Autowired
    public StudentContractController(StudentContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping("/availableCourses/{studentCod}/{semestru}")
    public ResponseEntity<List<MaterieDTO>> getAvailableCourses(
            @PathVariable String studentCod,
            @PathVariable int semestru) {
        try {
            List<MaterieDTO> available = contractService.getAvailableCoursesForContract(studentCod, semestru);
            return ResponseEntity.ok(available);
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/generateContract")
    public void generateContract(
            @RequestBody ContractRequest request,
            HttpServletResponse response) {
        try {
            byte[] pdfBytes = contractService.generateContractFromSelection(request.getStudentCod(), request.getSemestru(), request.getCoduriMaterii());
            response.setContentType("application/pdf");
            response.setContentLength(pdfBytes.length);
            response.setHeader("Content-Disposition", "attachment; filename=contract_" + request.getStudentCod() + ".pdf");
            OutputStream os = response.getOutputStream();
            os.write(pdfBytes);
            os.flush();
        } catch (Exception e) {
            response.setStatus(500);
        }
    }

    @GetMapping("/preview")
    public void previewContract(
            @RequestParam String studentCod,
            @RequestParam int semestru,
            @RequestParam(name = "coduriMaterii") List<String> coduriMaterii,
            HttpServletResponse response) {
        try {
            byte[] pdfBytes = contractService.generateContractFromSelection(
                    studentCod, semestru, coduriMaterii
            );

            response.setContentType("application/pdf");
            response.setContentLength(pdfBytes.length);
            response.setHeader(
                    "Content-Disposition",
                    "inline; filename=\"preview_contract_" + studentCod + ".pdf\""
            );

            ServletOutputStream os = response.getOutputStream();
            os.write(pdfBytes);
            os.flush();
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Eroare la generarea PDF-ului."
                );
            } catch (IOException ignored) {}
        }
    }


    public static class ContractRequest {
        private String studentCod;
        private int semestru;
        private List<String> coduriMaterii;

        // Getters și setters
        public String getStudentCod() { return studentCod; }
        public void setStudentCod(String studentCod) { this.studentCod = studentCod; }

        public int getSemestru() { return semestru; }
        public void setSemestru(int semestru) { this.semestru = semestru; }

        public List<String> getCoduriMaterii() { return coduriMaterii; }
        public void setCoduriMaterii(List<String> coduriMaterii) { this.coduriMaterii = coduriMaterii; }
    }
}
