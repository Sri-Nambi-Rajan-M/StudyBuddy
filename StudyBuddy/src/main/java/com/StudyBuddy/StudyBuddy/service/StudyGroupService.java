package com.StudyBuddy.StudyBuddy.service;

import com.StudyBuddy.StudyBuddy.dto.CreateGroupRequest;
import com.StudyBuddy.StudyBuddy.dto.CreateGroupRequest.StudyGroupResponse;
import com.StudyBuddy.StudyBuddy.entity.Membership;
import com.StudyBuddy.StudyBuddy.entity.Student;
import com.StudyBuddy.StudyBuddy.entity.StudyGroup;
import com.StudyBuddy.StudyBuddy.entity.Subject;
import com.StudyBuddy.StudyBuddy.exception.BussinessRuleException;
import com.StudyBuddy.StudyBuddy.repository.MembershipRepository;
import com.StudyBuddy.StudyBuddy.repository.StudentRepository;
import com.StudyBuddy.StudyBuddy.repository.StudyGroupRepository;
import com.StudyBuddy.StudyBuddy.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudyGroupService {

    private final StudyGroupRepository studyGroupRepository;
    private final MembershipRepository membershipRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;

    public StudyGroupService(StudyGroupRepository studyGroupRepository,
                             MembershipRepository membershipRepository,
                             SubjectRepository subjectRepository,
                             StudentRepository studentRepository) {
        this.studyGroupRepository = studyGroupRepository;
        this.membershipRepository = membershipRepository;
        this.subjectRepository = subjectRepository;
        this.studentRepository = studentRepository;
    }

    // Feature 1: create a group for a subject with a max member limit
    @Transactional
    public StudyGroupResponse createGroup(CreateGroupRequest request) {
        Subject subject = subjectRepository.findById(request.subjectId())
                .orElseThrow(() -> BussinessRuleException.notFound("Subject not found with id " + request.subjectId()));
        Student creator = studentRepository.findById(request.creatorId())
                .orElseThrow(() -> BussinessRuleException.notFound("Student not found with id " + request.creatorId()));

        String name = request.name().trim();
        if (studyGroupRepository.existsBySubjectIdAndNameIgnoreCase(subject.getId(), name)) {
            throw new BussinessRuleException(
                    "A group named '" + name + "' already exists for subject " + subject.getName());
        }

        StudyGroup group = studyGroupRepository.save(new StudyGroup(name, subject, creator, request.maxMembers()));

        // The creator is the first member of the group
        membershipRepository.save(new Membership(group, creator));

        return toResponse(group);
    }

    // Feature 2: student joins a group if it has space
    @Transactional
    public StudyGroupResponse joinGroup(Long groupId, Long studentId) {
        StudyGroup group = studyGroupRepository.findByIdForUpdate(groupId)
                .orElseThrow(() -> BussinessRuleException.notFound("Study group not found with id " + groupId));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> BussinessRuleException.notFound("Student not found with id " + studentId));

        // Rule: a student cannot join the same group twice
        if (membershipRepository.existsByStudyGroupIdAndStudentId(groupId, studentId)) {
            throw new BussinessRuleException("Student " + studentId + " is already a member of this group");
        }

        // Rule: a group cannot accept members beyond its cap
        long current = membershipRepository.countByStudyGroupId(groupId);
        if (current >= group.getMaxMembers()) {
            throw new BussinessRuleException(
                    "Group is full: maximum of " + group.getMaxMembers() + " members reached");
        }

        membershipRepository.save(new Membership(group, student));
        return toResponse(group);
    }

    // Feature 3: leave a group
    @Transactional
    public void leaveGroup(Long groupId, Long studentId) {
        StudyGroup group = studyGroupRepository.findById(groupId)
                .orElseThrow(() -> BussinessRuleException.notFound("Study group not found with id " + groupId));
        Membership membership = membershipRepository.findByStudyGroupIdAndStudentId(groupId, studentId)
                .orElseThrow(() -> BussinessRuleException.notFound(
                        "Student " + studentId + " is not a member of this group"));

        if (group.getCreator().getId().equals(studentId)) {
            throw new BussinessRuleException("The group creator cannot leave their own group");
        }
        membershipRepository.delete(membership);
    }

    // Feature 4: list all groups for a subject with current member count
    @Transactional(readOnly = true)
    public List<StudyGroupResponse> listGroupsBySubject(Long subjectId) {
        if (!subjectRepository.existsById(subjectId)) {
            throw BussinessRuleException.notFound("Subject not found with id " + subjectId);
        }
        return studyGroupRepository.findBySubjectId(subjectId).stream()
                .map(this::toResponse)
                .toList();
    }

    // Feature 5: group creator can remove a member
    @Transactional
    public void removeMember(Long groupId, Long studentId, Long creatorId) {
        StudyGroup group = studyGroupRepository.findById(groupId)
                .orElseThrow(() -> BussinessRuleException.notFound("Study group not found with id " + groupId));

        if (!group.getCreator().getId().equals(creatorId)) {
            throw BussinessRuleException.forbidden("Only the group creator can remove members");
        }
        if (group.getCreator().getId().equals(studentId)) {
            throw new BussinessRuleException("The creator cannot be removed from their own group");
        }

        Membership membership = membershipRepository.findByStudyGroupIdAndStudentId(groupId, studentId)
                .orElseThrow(() -> BussinessRuleException.notFound(
                        "Student " + studentId + " is not a member of this group"));
        membershipRepository.delete(membership);
    }

    private StudyGroupResponse toResponse(StudyGroup group) {
        long current = membershipRepository.countByStudyGroupId(group.getId());
        return new StudyGroupResponse(
                group.getId(),
                group.getName(),
                group.getSubject().getId(),
                group.getSubject().getName(),
                group.getCreator().getId(),
                group.getCreator().getName(),
                group.getMaxMembers(),
                current,
                Math.max(0, group.getMaxMembers() - current),
                group.getCreatedAt());
    }
}