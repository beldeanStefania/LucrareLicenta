package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.ContractDTO;
import com.orar.Backend.Orar.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.orar.Backend.Orar.service.StudentContractService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.orar.Backend.Orar.dto.ContractYearRequest;


import java.io.IOException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/api/studentContract")
public class StudentContractController {

    @Autowired
    private StudentContractService contractService;


    @Autowired
    public StudentContractController(StudentContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping("/availableCourses/{studentCod}/{anContract}/{semestru}")
    public ResponseEntity<List<ContractDTO>> getAvailableCourses(
            @PathVariable String studentCod,
            @PathVariable int anContract,
            @PathVariable int semestru) {
        try {
            return ResponseEntity.ok(contractService.getAvailableCoursesForContract(studentCod, anContract, semestru));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/generateContract")
    public ResponseEntity<byte[]> generateContract(@RequestBody ContractYearRequest req) {
        try {
            byte[] pdf = contractService.generateContractFromSelection(
                    req.getStudentCod(),
                    req.getAnContract(),
                    req.getCoduriMaterii()
            );

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"contract_" + req.getStudentCod() + ".pdf\"")
                    .header("Content-Type", "application/pdf")
                    .body(pdf);

        } catch (IllegalArgumentException | ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage().getBytes());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Eroare la generare.".getBytes());
        }
    }



    @PostMapping("/preview")
    public void previewContract(
            @RequestBody ContractYearRequest req,
            HttpServletResponse resp) throws IOException {
        try {
            byte[] pdf = contractService.generateContractPdfWithoutPersist(
                    req.getStudentCod(),
                    req.getAnContract(),
                    req.getCoduriMaterii()
            );
            resp.setContentType("application/pdf");
            resp.setContentLength(pdf.length);
            resp.setHeader("Content-Disposition",
                    "inline; filename=\"preview_contract_" + req.getStudentCod() + ".pdf\"");
            resp.getOutputStream().write(pdf);
            resp.flushBuffer();
        } catch (Exception e) {
            resp.sendError(SC_INTERNAL_SERVER_ERROR,
                    "Eroare la generarea PDF-ului.");
        }
    }

    @GetMapping("/contractCourses/{studentCod}/{anContract}")
    public ResponseEntity<List<ContractDTO>> getContractCourses(
            @PathVariable String studentCod,
            @PathVariable int anContract) {
        try {
            var cursuri = contractService.getContractCourses(studentCod, anContract);
            return ResponseEntity.ok(cursuri);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/exists/{studentCod}/{anContract}")
    public ResponseEntity<Boolean> existsContract(
            @PathVariable String studentCod,
            @PathVariable int anContract
    ) {
        boolean exists = contractService.hasContract(studentCod, anContract);
        return ResponseEntity.ok(exists);
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
