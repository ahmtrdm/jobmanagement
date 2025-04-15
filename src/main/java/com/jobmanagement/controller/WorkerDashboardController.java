package com.jobmanagement.controller;

import com.jobmanagement.model.JobApplication;
import com.jobmanagement.model.JobPosting;
import com.jobmanagement.model.WorkerProfile;
import com.jobmanagement.service.WorkerDashboardService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Arrays;

@Controller
@RequestMapping("/worker")
public class WorkerDashboardController {

    private final WorkerDashboardService workerDashboardService;
    private final ObjectMapper objectMapper;

    public WorkerDashboardController(WorkerDashboardService workerDashboardService) {
        this.workerDashboardService = workerDashboardService;
        this.objectMapper = new ObjectMapper();
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

    @PostMapping("/profile")
    public String updateProfile(
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "skills", required = false) String skillsJson,
            @RequestParam(value = "jobTypes", required = false) String[] jobTypes,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestParam(value = "resume", required = false) MultipartFile resume) {
        
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            WorkerProfile profile = workerDashboardService.getWorkerProfile(username);
            if (profile == null) {
                profile = new WorkerProfile();
                profile.setUsername(username);
            }

            profile.setFullName(fullName);
            profile.setEmail(email);
            profile.setPhone(phone);
            profile.setAddress(address);

            // Parse skills from JSON
            if (skillsJson != null && !skillsJson.isEmpty()) {
                try {
                    List<String> skills = objectMapper.readValue(skillsJson, List.class);
                    profile.setSkills(skills);
                } catch (Exception e) {
                    // If JSON parsing fails, set empty list
                    profile.setSkills(List.of());
                }
            } else {
                profile.setSkills(List.of());
            }

            // Set job types
            if (jobTypes != null && jobTypes.length > 0) {
                profile.setJobTypes(Arrays.asList(jobTypes));
            } else {
                profile.setJobTypes(List.of());
            }

            // Handle file uploads if present
            if (profileImage != null && !profileImage.isEmpty()) {
                // TODO: Implement file upload logic
                // profile.setImageUrl(...);
            }

            if (resume != null && !resume.isEmpty()) {
                // TODO: Implement resume upload logic
                // profile.setResumeUrl(...);
            }

            workerDashboardService.saveWorkerProfile(profile);
            return "redirect:/worker/profile?success=true";
        } catch (Exception e) {
            return "redirect:/worker/profile?error=" + e.getMessage();
        }
    }
} 