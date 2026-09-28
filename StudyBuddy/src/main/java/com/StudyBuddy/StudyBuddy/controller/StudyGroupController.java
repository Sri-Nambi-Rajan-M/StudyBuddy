package com.StudyBuddy.StudyBuddy.controller;

import com.StudyBuddy.StudyBuddy.dto.CreateGroupRequest;
import com.StudyBuddy.StudyBuddy.dto.CreateGroupRequest.StudyGroupResponse;
import com.StudyBuddy.StudyBuddy.service.StudyGroupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class StudyGroupController {

    private final StudyGroupService StudyGroupService;

    public StudyGroupController(StudyGroupService StudyGroupService) {
        this.StudyGroupService = StudyGroupService;
    }

    // 1. Create group
    @PostMapping
    public ResponseEntity<StudyGroupResponse> create(@Valid @RequestBody CreateGroupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(StudyGroupService.createGroup(request));
    }

    // 2. Join group  ->  POST /api/groups/1/join?studentId=2
    @PostMapping("/{groupId}/join")
    public ResponseEntity<StudyGroupResponse> join(@PathVariable Long groupId,
                                                   @RequestParam Long studentId) {
        return ResponseEntity.ok(StudyGroupService.joinGroup(groupId, studentId));
    }

    // 3. Leave group  ->  DELETE /api/groups/1/leave?studentId=2
    @DeleteMapping("/{groupId}/leave")
    public ResponseEntity<Void> leave(@PathVariable Long groupId,
                                      @RequestParam Long studentId) {
        StudyGroupService.leaveGroup(groupId, studentId);
        return ResponseEntity.noContent().build();
    }

    // 4. List groups of a subject with current member count
    @GetMapping("/subject/{subjectId}")
    public List<StudyGroupResponse> listBySubject(@PathVariable Long subjectId) {
        return StudyGroupService.listGroupsBySubject(subjectId);
    }

    // 5. Creator removes a member  ->  DELETE /api/groups/1/members/2?creatorId=1
    @DeleteMapping("/{groupId}/members/{studentId}")
    public ResponseEntity<Void> removeMember(@PathVariable Long groupId,
                                             @PathVariable Long studentId,
                                             @RequestParam Long creatorId) {
        StudyGroupService.removeMember(groupId, studentId, creatorId);
        return ResponseEntity.noContent().build();
    }
}