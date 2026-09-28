package com.StudyBuddy.StudyBuddy.repository;

import com.StudyBuddy.StudyBuddy.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    long countByStudyGroupId(Long studyGroupId);

    boolean existsByStudyGroupIdAndStudentId(Long studyGroupId, Long studentId);

    Optional<Membership> findByStudyGroupIdAndStudentId(Long studyGroupId, Long studentId);
}