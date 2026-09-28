package com.StudyBuddy.StudyBuddy.dto;

public record DashboardResponse(
        long totalSubjects,
        long totalStudents,
        long totalGroups,
        long totalMemberships
) {}