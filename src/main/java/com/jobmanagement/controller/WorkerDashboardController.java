package com.jobmanagement.controller;

import com.jobmanagement.model.JobApplication;
import com.jobmanagement.model.JobPosting;
import com.jobmanagement.model.WorkerProfile;
import com.jobmanagement.service.WorkerDashboardService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/worker")
public class WorkerDashboardController {

    private final WorkerDashboardService workerDashboardService;

    public WorkerDashboardController(WorkerDashboardService workerDashboardService) {
        this.workerDashboardService = workerDashboardService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Get worker profile
            WorkerProfile profile = workerDashboardService.getWorkerProfile(username);
            if (profile == null) {
                return "redirect:/worker/profile";
            }

            // Get statistics
            int activeApplications = workerDashboardService.getActiveApplicationsCount(username);
            int approvedApplications = workerDashboardService.getApprovedApplicationsCount(username);
            int rejectedApplications = workerDashboardService.getRejectedApplicationsCount(username);

            // Get recent applications
            List<JobApplication> recentApplications = workerDashboardService.getRecentApplications(username);

            // Get recommended jobs
            List<JobPosting> recommendedJobs = workerDashboardService.getRecommendedJobs(username);

            // Add attributes to model
            model.addAttribute("user", profile);
            model.addAttribute("activeApplications", activeApplications);
            model.addAttribute("approvedApplications", approvedApplications);
            model.addAttribute("rejectedApplications", rejectedApplications);
            model.addAttribute("recentApplications", recentApplications);
            model.addAttribute("recommendedJobs", recommendedJobs);

            return "worker-dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Dashboard yüklenirken bir hata oluştu: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/jobs")
    public String showJobs(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        model.addAttribute("jobs", workerDashboardService.getRecommendedJobs(username));
        return "worker-jobs";
    }

    @GetMapping("/applications")
    public String showApplications(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        model.addAttribute("applications", workerDashboardService.getAllApplications(username));
        return "worker-applications";
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        WorkerProfile profile = workerDashboardService.getWorkerProfile(username);
        if (profile == null) {
            profile = new WorkerProfile();
            profile.setUsername(username);
            profile.setSkills(List.of());
            profile.setJobTypes(List.of());
            profile.setPreferredLocations(List.of());
            profile.setImageUrl("/images/default-avatar.png");
        }

        model.addAttribute("profile", profile);
        return "worker-profile";
    }
} 