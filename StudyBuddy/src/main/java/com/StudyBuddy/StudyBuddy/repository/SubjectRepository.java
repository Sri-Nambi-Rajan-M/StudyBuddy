package com.StudyBuddy.StudyBuddy.repository;

import com.StudyBuddy.StudyBuddy.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsByNameIgnoreCase(String name);
}