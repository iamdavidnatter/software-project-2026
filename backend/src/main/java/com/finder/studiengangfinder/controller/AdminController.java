package com.finder.studiengangfinder.controller;

import com.finder.studiengangfinder.dto.AdminDashboardDto;
import com.finder.studiengangfinder.service.AdminDashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminDashboardService adminDashboardService;

    public AdminController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    @GetMapping("/dashboard")
    public AdminDashboardDto getDashboard() {
        return adminDashboardService.getDashboard();
    }
}
