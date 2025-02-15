package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.StudentDTO;
import com.orar.Backend.Orar.dto.StudentGradeDTO;
import com.orar.Backend.Orar.exception.StudentAlreadyExistsException;
import com.orar.Backend.Orar.exception.StudentNotFoundException;
import com.orar.Backend.Orar.model.Rol;
import com.orar.Backend.Orar.model.Student;
import com.orar.Backend.Orar.model.User;
import com.orar.Backend.Orar.repository.CatalogStudentMaterieRepository;
import com.orar.Backend.Orar.repository.RolRepository;
import com.orar.Backend.Orar.repository.StudentRepository;
import com.orar.Backend.Orar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CatalogStudentMaterieRepository catalogStudentMaterieRepository;

    public List<Student> getAll() throws StudentNotFoundException {
        if (studentRepository.findAll().isEmpty()) {
            throw new StudentNotFoundException("No students found");
        }
        return studentRepository.findAll();
    }

    public Student add(final StudentDTO studentDTO) throws StudentAlreadyExistsException, StudentNotFoundException {
        var newStudent = buildStudent(studentDTO);
        return studentRepository.save(newStudent);
    }

    private Student buildStudent(final StudentDTO studentDTO) throws StudentAlreadyExistsException, StudentNotFoundException {
        checkUniqueStudent(studentDTO);
        return createStudent(studentDTO);
    }

    private Student createStudent(final StudentDTO studentDTO) throws StudentAlreadyExistsException {
        if (studentRepository.findByCod(studentDTO.getCod()).isPresent()) {
            throw new StudentAlreadyExistsException("Codul studentului trebuie să fie unic! Un student cu acest cod există deja.");
        }

        // Recuperăm rolul STUDENT din baza de date
        Rol studentRole = rolRepository.findByName("STUDENT")
                .orElseThrow(() -> new RuntimeException("Rolul STUDENT nu există în baza de date"));

        // Cream un User asociat cu acest Student
        User user = new User();
        user.setUsername(studentDTO.getUsername());
        user.setPassword(passwordEncoder.encode(studentDTO.getPassword()));
        user.setRole(studentRole); // Asociază rolul STUDENT cu utilizatorul
        userRepository.save(user); // Salvează user-ul în baza de date

        // Cream Studentul
        Student student = new Student();
        student.setNume(studentDTO.getNume());
        student.setPrenume(studentDTO.getPrenume());
        student.setAn(studentDTO.getAn());
        student.setGrupa(studentDTO.getGrupa());
        student.setCod(studentDTO.getCod());
        student.setUser(user); // Asociază User-ul cu Studentul

        return student;
    }

    private void checkUniqueStudent(final StudentDTO studentDTO) throws StudentAlreadyExistsException {
        if (studentRepository.findByNumeAndPrenume(studentDTO.getNume(), studentDTO.getPrenume()).isPresent()) {
            throw new StudentAlreadyExistsException("Student already exists");
        }
    }

    public Student update(String cod, final StudentDTO studentDTO) throws StudentNotFoundException {
        var existingStudent = findStudent(cod);
        existingStudent.setNume(studentDTO.getNume());
        existingStudent.setPrenume(studentDTO.getPrenume());
        existingStudent.setAn(studentDTO.getAn());
        existingStudent.setGrupa(studentDTO.getGrupa());
        return studentRepository.save(existingStudent);
    }

    private Student findStudent(final String id) throws StudentNotFoundException {
        return studentRepository.findByCod(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
    }

    public Student getByAnAndGrupa(final int an, final String grupa) throws StudentNotFoundException {
        return studentRepository.findByAnAndGrupa(an, grupa)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
    }

    public void delete(final String numeStudent, String prenumeStudent) throws StudentNotFoundException {
        var student = studentRepository.findByNumeAndPrenume(numeStudent, prenumeStudent)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        studentRepository.delete(student);
    }

    public void deleteByCod(String cod) throws StudentNotFoundException {
        Student student = studentRepository.findByCod(cod).orElseThrow(() -> new StudentNotFoundException("Student not found"));
        studentRepository.delete(student);
    }

    public List<Student> getByGrupa(String grupa) throws StudentNotFoundException {
        return studentRepository.findByGrupa(grupa).orElseThrow(() -> new StudentNotFoundException("Student not found"));
    }
}
