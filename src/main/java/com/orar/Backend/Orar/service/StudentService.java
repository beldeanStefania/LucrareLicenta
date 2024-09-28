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

    public List<Student> getAllStudents() throws StudentNotFoundException {
        checkStudentExists();
        return studentRepository.findAll();
    }

    private void checkStudentExists() throws StudentNotFoundException {
        if (studentRepository.findAll().isEmpty()) {
            throw new StudentNotFoundException("No students found");
        }
    }

    public Student add(final StudentDTO studentDTO) throws StudentAlreadyExistsException {
        var newStudent = buildStudent(studentDTO);
        return add(newStudent);
    }

    private Student buildStudent(final StudentDTO studentDTO) throws StudentAlreadyExistsException {
        checkUniqueStudent(studentDTO);
        return createStudent(studentDTO);
    }

    private void checkUniqueStudent(final StudentDTO studentDTO) throws StudentAlreadyExistsException {
        studentRepository.findByNume(studentDTO.getNume())
                .orElseThrow(() -> new StudentAlreadyExistsException("Student already exists"));
    }

    private Student createStudent(final StudentDTO studentDTO) {
        Student student = new Student();
        student.setNume(studentDTO.getNume());
        student.setGrupa(studentDTO.getGrupa());
        student.setAn(studentDTO.getAn());
        return student;
    }

    private Student add(final Student student) {
        return studentRepository.save(student);
    }

    public Student update(final StudentDTO studentDTO) throws StudentNotFoundException {
        Student existingStudent = findStudent(studentDTO);
        var updatedStudent = updateStudent(studentDTO, existingStudent);
        return update(updatedStudent);
    }

    private Student findStudent(final StudentDTO studentDTO) throws StudentNotFoundException {
        return studentRepository.findByNume(studentDTO.getNume())
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
    }

    private Student updateStudent(final StudentDTO studentDTO, final Student existingStudent) {
        existingStudent.setGrupa(studentDTO.getGrupa());
        existingStudent.setAn(studentDTO.getAn());
        return existingStudent;
    }

    public Student update(final Student student) {
        return studentRepository.save(student);
    }

    public void delete(final String numeStudent) throws StudentNotFoundException {
        var student = studentRepository.findByNume(numeStudent)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        studentRepository.delete(student);
    }

}
