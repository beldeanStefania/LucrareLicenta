package com.orar.Backend.Orar.service;

import com.orar.Backend.Orar.model.Student;
import com.orar.Backend.Orar.repository.StudentRepository;
import jakarta.transaction.Transactional;
import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StudentService implements IService<Student>{
    private final StudentRepository studentRepo;
    @Override
    public Student create(Student entity) {
        log.info("Creating a new student {}", entity.getNume());
        return studentRepo.save(entity);
    }

    @Override
    public Student get(Long id) {
        log.info("Getting student with ID {}", id);
        return studentRepo.findById(id).orElse(null);
    }

    @Override
    public List<Student> getAll() {
        log.info("Listing all students");
        return studentRepo.findAll();
    }

    @Override
    public Student update(Student entity) {
        log.info("Updating student: {}", entity.getNume());
        return studentRepo.save(entity);
    }

    @Override
    public Boolean delete(Long id) {
        log.info("Deleting student with ID {}", id);
        studentRepo.deleteById(id);
        return true;
    }
}
