//package com.orar.Backend.Orar.controller;
//
//import com.orar.Backend.Orar.model.Student;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/student")
//@RequiredArgsConstructor
//public class StudentController {
//    private final StudentService studentService;
//
//    @GetMapping("/get/{id}")
//    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
//        return ResponseEntity.ok(studentService.get(id));
//    }
//
//    @GetMapping("/getAll")
//    public ResponseEntity<List<Student>> getAllStudents() {
//        return ResponseEntity.ok(studentService.getAll());
//    }
//
//    @PostMapping("/create")
//    public ResponseEntity<Student> createStudent(@RequestBody  @Valid Student student) {
//        return ResponseEntity.ok(studentService.create(student));
//    }
//
//    @PutMapping("/update")
//    public ResponseEntity<Student> updateStudent(@RequestBody @Valid Student student) {
//        return ResponseEntity.ok(studentService.update(student));
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<Boolean> deleteStudent(@PathVariable Long id) {
//        return ResponseEntity.ok(studentService.delete(id));
//    }
//
//}
