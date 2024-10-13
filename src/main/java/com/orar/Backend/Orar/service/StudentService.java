package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.dto.StudentDTO;
import com.orar.Backend.Orar.exception.StudentAlreadyExistsException;
import com.orar.Backend.Orar.exception.StudentNotFoundException;
import com.orar.Backend.Orar.model.Student;
import com.orar.Backend.Orar.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;


    public List<Student> getAll() throws StudentNotFoundException {
        checkStudentExists();
        return studentRepository.findAll();
    }

    private void checkStudentExists() throws StudentNotFoundException {
        if (studentRepository.findAll().isEmpty()) {
            throw new StudentNotFoundException("No students found");
        }
    }

    public Student add(final StudentDTO studentDTO) throws StudentAlreadyExistsException, StudentNotFoundException {
        var newStudent = buildStudent(studentDTO);
        return studentRepository.save(newStudent);
    }

    private Student buildStudent(final StudentDTO studentDTO) throws StudentAlreadyExistsException, StudentNotFoundException {
        checkUniqueStudent(studentDTO);
        return createStudent(studentDTO);
    }

    private void checkUniqueStudent(final StudentDTO studentDTO) throws StudentAlreadyExistsException {
        if (studentRepository.findByNumeAndPrenume(studentDTO.getNume(), studentDTO.getPrenume()).isPresent()){
            throw new StudentAlreadyExistsException("Student already exists");
        }
    }


    private Student createStudent(final StudentDTO studentDTO) {
        Student student = new Student();
        student.setNume(studentDTO.getNume());
        student.setPrenume(studentDTO.getPrenume());
        student.setAn(studentDTO.getAn());
        student.setGrupa(studentDTO.getGrupa());
        return student;
    }

    public Student update(int id, final StudentDTO studentDTO) throws StudentNotFoundException {
        //Student existingStudent = findStudent(studentDTO);
        var existingStudent = findStudent(id);
        Student student = new Student();
        student.setId(id);
        student.setNume(studentDTO.getNume());
        student.setPrenume(studentDTO.getPrenume());
        student.setAn(studentDTO.getAn());
        student.setGrupa(existingStudent.getGrupa());
        return studentRepository.save(student);
    }

    private Student findStudent(final int id) throws StudentNotFoundException {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
    }

    public void delete(final String numeStudent, String prenumeStudent) throws StudentNotFoundException {
        var student = studentRepository.findByNumeAndPrenume(numeStudent, prenumeStudent)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        studentRepository.delete(student);
    }

}
