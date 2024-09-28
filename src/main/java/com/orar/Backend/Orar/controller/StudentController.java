package com.orar.Backend.Orar.controller;

import com.orar.Backend.Orar.dto.StudentDTO;
import com.orar.Backend.Orar.exception.StudentNotFoundException;
import com.orar.Backend.Orar.model.Student;
import com.orar.Backend.Orar.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

//    @GetMapping("/get/{id}")
//    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
//        try {
//            return ResponseEntity.ok(studentService.get(id);
//        } catch (StudentNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Student>> getAllStudents() {
        try {
            return ok(studentService.getAll());
        } catch (StudentNotFoundException e) {
            return notFound().build();
        }
    }
    @PostMapping("/create")
    public ResponseEntity<Student> createStudent(@RequestBody  @Valid StudentDTO studentDTO) {
        try {
            return ok(studentService.add(studentDTO));
        } catch (Exception e) {
            return badRequest().build();
        }
    }
    @PutMapping("/update")
    public ResponseEntity<Student> updateStudent(@RequestBody @Valid Student student) {
        return ok(studentService.update(student));
    }

    @DeleteMapping("/delete/{numeStudent}/{prenumeStudent}")
    public ResponseEntity<Student> deleteStudent(@PathVariable String numeStudent, @PathVariable String prenumeStudent) {
        try {
            studentService.delete(numeStudent, prenumeStudent);
            return ok().build();
        } catch (StudentNotFoundException e) {
            return notFound().build();
        }
    }
}
