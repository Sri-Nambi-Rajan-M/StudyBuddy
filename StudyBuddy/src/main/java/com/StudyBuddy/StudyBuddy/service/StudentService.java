package com.StudyBuddy.StudyBuddy.service;

import com.StudyBuddy.StudyBuddy.dto.CreateStudentRequest;
import com.StudyBuddy.StudyBuddy.entity.Student;
import com.StudyBuddy.StudyBuddy.exception.BussinessRuleException;
import com.StudyBuddy.StudyBuddy.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public Student create(CreateStudentRequest request) {
        String email = request.email().trim().toLowerCase();
        if (studentRepository.existsByEmailIgnoreCase(email)) {
            throw new BussinessRuleException("A student with email '" + email + "' already exists");
        }
        return studentRepository.save(new Student(request.name().trim(), email));
    }

    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return studentRepository.findAll();
    }
}