package com.jobmanagement.service.impl;

import com.jobmanagement.model.JobApplication;
import com.jobmanagement.model.JobPosting;
import com.jobmanagement.model.WorkerProfile;
import com.jobmanagement.repository.JobApplicationRepository;
import com.jobmanagement.repository.JobPostingRepository;
import com.jobmanagement.repository.WorkerProfileRepository;
import com.jobmanagement.service.WorkerDashboardService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkerDashboardServiceImpl implements WorkerDashboardService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobPostingRepository jobPostingRepository;
    private final WorkerProfileRepository workerProfileRepository;

    public WorkerDashboardServiceImpl(
            JobApplicationRepository jobApplicationRepository,
            JobPostingRepository jobPostingRepository,
            WorkerProfileRepository workerProfileRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.workerProfileRepository = workerProfileRepository;
    }

    @Override
    public int getActiveApplicationsCount(String username) {
        return (int) jobApplicationRepository.findAll().stream()
                .filter(application -> application.getWorkerUsername().equals(username) && 
                        application.getStatus().equals("PENDING"))
                .count();
    }

    @Override
    public int getApprovedApplicationsCount(String username) {
        return (int) jobApplicationRepository.findAll().stream()
                .filter(application -> application.getWorkerUsername().equals(username) && 
                        application.getStatus().equals("APPROVED"))
                .count();
    }

    @Override
    public int getRejectedApplicationsCount(String username) {
        return (int) jobApplicationRepository.findAll().stream()
                .filter(application -> application.getWorkerUsername().equals(username) && 
                        application.getStatus().equals("REJECTED"))
                .count();
    }

    @Override
    public List<JobApplication> getRecentApplications(String username) {
        return jobApplicationRepository.findAll().stream()
                .filter(application -> application.getWorkerUsername().equals(username))
                .sorted((a1, a2) -> a2.getApplicationDate().compareTo(a1.getApplicationDate()))
                .limit(5)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobPosting> getRecommendedJobs(String username) {
        WorkerProfile profile = workerProfileRepository.findByUsername(username);
        if (profile == null || profile.getSkills() == null || profile.getSkills().isEmpty()) {
            return List.of();
        }

        List<String> workerSkills = profile.getSkills();
        return jobPostingRepository.findAll().stream()
                .filter(posting -> posting.isActive() && 
                        posting.getRequiredSkills().stream()
                                .anyMatch(skill -> workerSkills.contains(skill)))
                .limit(5)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobApplication> getAllApplications(String username) {
        return jobApplicationRepository.findAll().stream()
                .filter(application -> application.getWorkerUsername().equals(username))
                .sorted((a1, a2) -> a2.getApplicationDate().compareTo(a1.getApplicationDate()))
                .collect(Collectors.toList());
    }

    @Override
    public WorkerProfile getWorkerProfile(String username) {
        return workerProfileRepository.findByUsername(username);
    }
} 