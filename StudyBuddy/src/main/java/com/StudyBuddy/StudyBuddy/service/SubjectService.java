package com.StudyBuddy.StudyBuddy.service;

import com.StudyBuddy.StudyBuddy.dto.CreateSubjectRequest;
import com.StudyBuddy.StudyBuddy.entity.Subject;
import com.StudyBuddy.StudyBuddy.exception.BussinessRuleException;
import com.StudyBuddy.StudyBuddy.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubjectService {

    private final SubjectRepository SubjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.SubjectRepository = subjectRepository;
    }

    @Transactional
    public Subject create(CreateSubjectRequest request) {
        String name = request.name().trim();
        if (SubjectRepository.existsByNameIgnoreCase(name)) {
            throw new BussinessRuleException("Subject '" + name + "' already exists");
        }
        return SubjectRepository.save(new Subject(name));
    }

    @Transactional(readOnly = true)
    public List<Subject> findAll() {
        return SubjectRepository.findAll();
    }
}