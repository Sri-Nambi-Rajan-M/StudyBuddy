package com.StudyBuddy.StudyBuddy.controller;

import com.StudyBuddy.StudyBuddy.dto.CreateSubjectRequest;
import com.StudyBuddy.StudyBuddy.entity.Subject;
import com.StudyBuddy.StudyBuddy.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping
    public ResponseEntity<Subject> create(@Valid @RequestBody CreateSubjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subjectService.create(request));
    }

    @GetMapping
    public List<Subject> list() {
        return subjectService.findAll();
    }
}