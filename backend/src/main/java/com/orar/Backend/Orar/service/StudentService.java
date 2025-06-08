package com.orar.Backend.Orar.service;

import com.opencsv.exceptions.CsvValidationException;
import com.orar.Backend.Orar.dto.StudentDTO;
import com.orar.Backend.Orar.exception.StudentAlreadyExistsException;
import com.orar.Backend.Orar.exception.StudentNotFoundException;
import com.orar.Backend.Orar.model.Rol;
import com.orar.Backend.Orar.model.Specializare;
import com.orar.Backend.Orar.model.Student;
import com.orar.Backend.Orar.model.User;
import com.orar.Backend.Orar.repository.CatalogStudentMaterieRepository;
import com.orar.Backend.Orar.repository.RolRepository;
import com.orar.Backend.Orar.repository.SpecializareRepository;
import com.orar.Backend.Orar.repository.StudentRepository;
import com.orar.Backend.Orar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVReader;
import com.orar.Backend.Orar.dto.ImportResultDTO;
import org.springframework.web.multipart.MultipartFile;
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
    private SpecializareRepository specializareRepository;

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

        Rol studentRole = rolRepository.findByName("STUDENT")
                .orElseThrow(() -> new RuntimeException("Rolul STUDENT nu există în baza de date"));

        Specializare specializare = specializareRepository.findBySpecializare(studentDTO.getSpecializare());

        User user = new User();
        user.setUsername(studentDTO.getUsername());
        user.setPassword(passwordEncoder.encode(studentDTO.getPassword()));
        user.setRole(studentRole);
        user.setEmail(studentDTO.getEmail());
        userRepository.save(user);

        Student student = new Student();
        student.setNume(studentDTO.getNume());
        student.setPrenume(studentDTO.getPrenume());
        student.setAn(studentDTO.getAn());
        student.setGrupa(studentDTO.getGrupa());
        student.setCod(studentDTO.getCod());
        student.setSpecializare(specializare);
        student.setUser(user);

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
        return studentRepository.findByGrupa(grupa);
    }

    public List<ImportResultDTO> importFromCsv(MultipartFile file) {
        List<ImportResultDTO> results = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] header = reader.readNext(); // cod,nume,prenume,an,grupa,specializare,username,password,email
            String[] line;
            int row = 1;
            while ((line = reader.readNext()) != null) {
                row++;
                try {
                    StudentDTO dto = new StudentDTO();
                    dto.setCod(line[0]);
                    dto.setNume(line[1]);
                    dto.setPrenume(line[2]);
                    dto.setAn(Integer.parseInt(line[3]));
                    dto.setGrupa(line[4]);
                    dto.setSpecializare(line[5]);
                    dto.setUsername(line[6]);
                    dto.setPassword(line[7]);
                    dto.setEmail(line[8]);
                    this.add(dto);
                    results.add(new ImportResultDTO(row, true, "OK"));
                } catch (Exception ex) {
                    results.add(new ImportResultDTO(row, false, ex.getMessage()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Nu am putut citi fișierul: " + e.getMessage());
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return results;
    }
}
