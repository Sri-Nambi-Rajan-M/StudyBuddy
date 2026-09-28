package com.StudyBuddy.StudyBuddy.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateSubjectRequest(
        @NotBlank(message = "Subject name is required") String name
) {}