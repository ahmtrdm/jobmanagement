package com.jobmanagement.controller;

import com.jobmanagement.model.JobPosting;
import com.jobmanagement.service.EmployerDashboardService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
        jobPosting.setPostingDate(LocalDate.now());
        jobPosting.setActive(true);
        employerDashboardService.createJobPosting(jobPosting);
        return "redirect:/employer/dashboard";
    }

    @GetMapping("/jobs")
    public String showJobPostings(Authentication authentication, Model model) {
        String username = authentication.getName();
        model.addAttribute("jobPostings", employerDashboardService.getAllJobPostings(username));
        return "employer-job-postings";
    }

    @GetMapping("/applications")
    public String showApplications(Authentication authentication, Model model) {
        String username = authentication.getName();
        model.addAttribute("applications", employerDashboardService.getAllApplications(username));
        return "employer-applications";
    }

    @GetMapping("/applications/{jobId}")
    public String showJobApplications(@PathVariable Long jobId, Authentication authentication, Model model) {
        String username = authentication.getName();
        model.addAttribute("applications", employerDashboardService.getApplicationsByJobId(jobId));
        return "employer-job-applications";
    }

    @GetMapping("/profile")
    public String showProfile(Authentication authentication, Model model) {
        String username = authentication.getName();
        model.addAttribute("employer", employerDashboardService.getEmployerProfile(username));
        return "employer-profile";
    }
} 