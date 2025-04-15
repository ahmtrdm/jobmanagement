package com.jobmanagement.controller;

import com.jobmanagement.model.JobPosting;
import com.jobmanagement.model.EmployerProfile;
import com.jobmanagement.service.EmployerDashboardService;
import com.jobmanagement.service.FileService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Controller
@RequestMapping("/employer")
public class EmployerController {

    private final EmployerDashboardService employerDashboardService;
    private final FileService fileService;

    public EmployerController(EmployerDashboardService employerDashboardService, FileService fileService) {
        this.employerDashboardService = employerDashboardService;
        this.fileService = fileService;
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

    @GetMapping("/create-job")
    public String showCreateJobForm(Model model) {
        model.addAttribute("jobPosting", new JobPosting());
        return "employer-create-job";
    }

    @PostMapping("/create-job")
    public String createJob(@ModelAttribute JobPosting jobPosting, Authentication authentication) {
        String username = authentication.getName();
        jobPosting.setEmployerUsername(username);
        jobPosting.setPostingDate(LocalDate.now());
        jobPosting.setActive(true);
        
        // Şirket adı boşsa hata fırlat
        if (jobPosting.getCompanyName() == null || jobPosting.getCompanyName().trim().isEmpty()) {
            throw new IllegalArgumentException("Şirket adı boş olamaz");
        }
        
        employerDashboardService.createJobPosting(jobPosting);
        return "redirect:/employer/dashboard";
    }

    @GetMapping("/job-postings")
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
    public String showProfile(Model model, Authentication authentication) {
        String username = authentication.getName();
        EmployerProfile profile = employerDashboardService.getEmployerProfile(username);
        if (profile == null) {
            profile = new EmployerProfile();
            profile.setUsername(username);
        }
        model.addAttribute("profile", profile);
        return "employer-profile";
    }

    @GetMapping("/job-postings/{id}/edit")
    public String showEditJobForm(@PathVariable Long id, Authentication authentication, Model model) {
        String username = authentication.getName();
        JobPosting jobPosting = employerDashboardService.getJobPostingById(id);
        if (jobPosting == null || !jobPosting.getEmployerUsername().equals(username)) {
            return "redirect:/employer/job-postings";
        }
        model.addAttribute("jobPosting", jobPosting);
        return "employer-edit-job";
    }

    @PostMapping("/job-postings/{id}/edit")
    public String updateJob(@PathVariable Long id, @ModelAttribute JobPosting jobPosting, Authentication authentication) {
        String username = authentication.getName();
        JobPosting existingJob = employerDashboardService.getJobPostingById(id);
        if (existingJob == null || !existingJob.getEmployerUsername().equals(username)) {
            return "redirect:/employer/job-postings";
        }
        jobPosting.setId(id);
        jobPosting.setEmployerUsername(username);
        jobPosting.setPostingDate(existingJob.getPostingDate());
        jobPosting.setActive(existingJob.isActive());
        employerDashboardService.updateJobPosting(jobPosting);
        return "redirect:/employer/job-postings";
    }

    @DeleteMapping("/job-postings/{id}")
    public String deleteJob(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        JobPosting jobPosting = employerDashboardService.getJobPostingById(id);
        if (jobPosting != null && jobPosting.getEmployerUsername().equals(username)) {
            employerDashboardService.deleteJobPosting(id);
        }
        return "redirect:/employer/job-postings";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute EmployerProfile profile, 
                              @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
                              @RequestParam(value = "companyLogo", required = false) MultipartFile companyLogo,
                              Authentication authentication) {
        String username = authentication.getName();
        profile.setUsername(username);
        
        // Handle profile image upload
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String imageUrl = fileService.saveFile(profileImage, "profile-images");
                profile.setImageUrl(imageUrl);
            } catch (IOException e) {
                // Handle error
            }
        }
        
        // Handle company logo upload
        if (companyLogo != null && !companyLogo.isEmpty()) {
            try {
                String logoUrl = fileService.saveFile(companyLogo, "company-logos");
                profile.setCompanyLogo(logoUrl);
            } catch (IOException e) {
                // Handle error
            }
        }
        
        employerDashboardService.updateEmployerProfile(profile);
        return "redirect:/employer/profile";
    }

    @GetMapping("/job-postings/search")
    public String searchJobs(@RequestParam String query, Authentication authentication, Model model) {
        model.addAttribute("jobPostings", employerDashboardService.searchJobPostings(authentication.getName(), query));
        return "employer-job-postings";
    }
} 