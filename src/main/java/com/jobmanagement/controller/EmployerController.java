package com.jobmanagement.controller;

import com.jobmanagement.model.JobPosting;
import com.jobmanagement.service.EmployerDashboardService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employer")
public class EmployerController {

    private final EmployerDashboardService employerDashboardService;

    public EmployerController(EmployerDashboardService employerDashboardService) {
        this.employerDashboardService = employerDashboardService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        model.addAttribute("activeJobPostings", employerDashboardService.getActiveJobPostingsCount(username));
        model.addAttribute("totalApplications", employerDashboardService.getTotalApplicationsCount(username));
        model.addAttribute("pendingApplications", employerDashboardService.getPendingApplicationsCount(username));
        model.addAttribute("recentJobPostings", employerDashboardService.getRecentJobPostings(username));
        model.addAttribute("recentApplications", employerDashboardService.getRecentApplications(username));
        return "employer-dashboard";
    }

    @GetMapping("/jobs/create")
    public String showCreateJobForm(Model model) {
        model.addAttribute("jobPosting", new JobPosting());
        return "employer-create-job";
    }

    @PostMapping("/jobs/create")
    public String createJob(@ModelAttribute JobPosting jobPosting, Authentication authentication) {
        jobPosting.setEmployerUsername(authentication.getName());
        employerDashboardService.createJobPosting(jobPosting);
        return "redirect:/employer/dashboard";
    }
} 