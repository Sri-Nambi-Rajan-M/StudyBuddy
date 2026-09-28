package com.StudyBuddy.StudyBuddy.repository;

import com.StudyBuddy.StudyBuddy.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByEmailIgnoreCase(String email);
}