package com.StudyBuddy.StudyBuddy.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CreateGroupRequest(
        @NotBlank(message = "Group name is required") String name,
        @NotNull(message = "subjectId is required") @Positive(message = "subjectId must be positive") Long subjectId,
        @NotNull(message = "creatorId is required") @Positive(message = "creatorId must be positive") Long creatorId,
        @NotNull(message = "maxMembers is required") @Min(value = 2, message = "maxMembers must be at least 2") Integer maxMembers
) {

    // Response for a study group (nested here so no extra file is needed)
    public record StudyGroupResponse(
            Long id,
            String name,
            Long subjectId,
            String subjectName,
            Long creatorId,
            String creatorName,
            Integer maxMembers,
            long currentMembers,
            long availableSlots,
            LocalDateTime createdAt
    ) {}
}