package com.StudyBuddy.StudyBuddy.controller;

import com.StudyBuddy.StudyBuddy.dto.CreateStudentRequest;
import com.StudyBuddy.StudyBuddy.entity.Student;
import com.StudyBuddy.StudyBuddy.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> create(@Valid @RequestBody CreateStudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.create(request));
    }

    @GetMapping
    public List<Student> list() {
        return studentService.findAll();
    }
}