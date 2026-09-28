package com.StudyBuddy.StudyBuddy.service;

import com.StudyBuddy.StudyBuddy.dto.DashboardResponse;
import com.StudyBuddy.StudyBuddy.repository.MembershipRepository;
import com.StudyBuddy.StudyBuddy.repository.StudentRepository;
import com.StudyBuddy.StudyBuddy.repository.StudyGroupRepository;
import com.StudyBuddy.StudyBuddy.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final MembershipRepository membershipRepository;

    public DashboardService(SubjectRepository subjectRepository,
                            StudentRepository studentRepository,
                            StudyGroupRepository studyGroupRepository,
                            MembershipRepository membershipRepository) {
        this.subjectRepository = subjectRepository;
        this.studentRepository = studentRepository;
        this.studyGroupRepository = studyGroupRepository;
        this.membershipRepository = membershipRepository;
    }

    @Transactional(readOnly = true)
    public DashboardResponse getSummary() {
        return new DashboardResponse(
                subjectRepository.count(),
                studentRepository.count(),
                studyGroupRepository.count(),
                membershipRepository.count());
    }
}