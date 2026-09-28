package com.StudyBuddy.StudyBuddy.controller;

import com.StudyBuddy.StudyBuddy.dto.DashboardResponse;
import com.StudyBuddy.StudyBuddy.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public DashboardResponse summary() {
        return dashboardService.getSummary();
    }
}