package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.StudentDTO;
import com.orar.Backend.Orar.exception.StudentAlreadyExistsException;
import com.orar.Backend.Orar.exception.StudentNotFoundException;
import com.orar.Backend.Orar.model.Student;
import com.orar.Backend.Orar.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;


//    @Operation(summary = "Obtine toti studentii")
//    @GetMapping("/getAll")
//    public ResponseEntity<List<Student>> getAllStudents() {
//        try {
//            return ok(studentService.getAll());
//        } catch (StudentNotFoundException e) {
//            return notFound().build();
//        }
//    }

    @GetMapping("/getAllStudents")
    public ResponseEntity<List<Student>> getAllStudents() {
        try{
            List<Student> students = studentService.getAll();
            return ResponseEntity.ok(students != null ? students : new ArrayList<>());
        } catch (StudentNotFoundException e) {
            return notFound().build();
        }

    }


    @GetMapping("/getByAnAndGrupa/{an}/{grupa}")
    public ResponseEntity<Student> getStudentByAnAndGrupa(@PathVariable Integer an, @PathVariable String grupa) {
        try {
            return ok(studentService.getByAnAndGrupa(an, grupa));
        } catch (StudentNotFoundException e) {
            return notFound().build();
        }
    }

    @GetMapping("/getByGrupa/{grupa}")
    public ResponseEntity<List<Student>> getStudentByNumeAndPrenume(@PathVariable String grupa) {
        try {
            return ok(studentService.getByGrupa(grupa));
        } catch (StudentNotFoundException e) {
            return notFound().build();
        }
    }

    @Operation(summary = "Adauga student")
    @PostMapping("/add")
    public ResponseEntity<?> createStudent(@RequestBody StudentDTO studentDTO) {
        try {
            return ok(studentService.add(studentDTO));
        } catch (StudentAlreadyExistsException e) {
            return ResponseEntity.status(409).body("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("{\"error\": \"Eroare la adăugarea studentului: " + e.getMessage() + "\"}");
        }
    }

    @Operation(summary = "Modifica student")
    @PutMapping("/update/{cod}")
    public ResponseEntity<Student> updateStudent(@PathVariable String cod, @RequestBody StudentDTO studentDTO) throws StudentNotFoundException {
        return ok(studentService.update(cod, studentDTO));
    }

    @Operation(summary = "Sterge student")
    @DeleteMapping("/delete/{numeStudent}/{prenumeStudent}")
    public ResponseEntity<Student> deleteStudent(@PathVariable String numeStudent, @PathVariable String prenumeStudent) {
        try {
            studentService.delete(numeStudent, prenumeStudent);
            return ok().build();
        } catch (StudentNotFoundException e) {
            return notFound().build();
        }
    }

    @DeleteMapping("/delete/{cod}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String cod) {
        try {
            studentService.deleteByCod(cod);
            return ok().build();
        } catch (StudentNotFoundException e) {
            return notFound().build();
        }
    }

}
