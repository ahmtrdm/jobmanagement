package com.jobmanagement.service.impl;

import com.jobmanagement.model.JobApplication;
import com.jobmanagement.model.JobPosting;
import com.jobmanagement.model.EmployerProfile;
import com.jobmanagement.repository.JobApplicationRepository;
import com.jobmanagement.repository.JobPostingRepository;
import com.jobmanagement.repository.EmployerProfileRepository;
import com.jobmanagement.service.EmployerDashboardService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployerDashboardServiceImpl implements EmployerDashboardService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobPostingRepository jobPostingRepository;
    private final EmployerProfileRepository employerProfileRepository;

    public EmployerDashboardServiceImpl(
            JobApplicationRepository jobApplicationRepository,
            JobPostingRepository jobPostingRepository,
            EmployerProfileRepository employerProfileRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.employerProfileRepository = employerProfileRepository;
    }

    @Override
    public int getActiveJobPostingsCount(String username) {
        return jobPostingRepository.findByEmployerUsernameAndActiveTrue(username).size();
    }

    @Override
    public int getTotalApplicationsCount(String username) {
        return jobApplicationRepository.findByJobPostingEmployerUsername(username).size();
    }

    @Override
    public int getPendingApplicationsCount(String username) {
        return jobApplicationRepository.findByJobPostingEmployerUsernameAndStatus(username, "PENDING").size();
    }

    @Override
    public List<JobPosting> getRecentJobPostings(String username) {
        return jobPostingRepository.findByEmployerUsernameOrderByPostingDateDesc(username)
                .stream()
                .limit(5)
                .toList();
    }

    @Override
    public List<JobApplication> getRecentApplications(String username) {
        return jobApplicationRepository.findByJobPostingEmployerUsernameOrderByApplicationDateDesc(username)
                .stream()
                .limit(5)
                .toList();
    }

    @Override
    public List<JobPosting> getAllJobPostings(String username) {
        return jobPostingRepository.findByEmployerUsernameOrderByPostingDateDesc(username);
    }

    @Override
    public List<JobApplication> getAllApplications(String username) {
        return jobApplicationRepository.findByJobPostingEmployerUsernameOrderByApplicationDateDesc(username);
    }

    @Override
    public List<JobApplication> getApplicationsByJobId(Long jobId) {
        return jobApplicationRepository.findByJobPostingIdOrderByApplicationDateDesc(jobId);
    }

    @Override
    public EmployerProfile getEmployerProfile(String username) {
        return employerProfileRepository.findByUsername(username);
    }

    @Override
    public void createJobPosting(JobPosting jobPosting) {
        jobPosting.setActive(true);
        jobPostingRepository.save(jobPosting);
    }
} 