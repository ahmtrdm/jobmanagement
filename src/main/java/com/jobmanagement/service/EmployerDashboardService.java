package com.jobmanagement.service;

import com.jobmanagement.model.JobApplication;
import com.jobmanagement.model.JobPosting;
import com.jobmanagement.model.EmployerProfile;
import com.jobmanagement.repository.JobPostingRepository;
import com.jobmanagement.repository.JobApplicationRepository;
import com.jobmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EmployerDashboardService {
    int getActiveJobPostingsCount(String employerUsername);
    int getTotalApplicationsCount(String employerUsername);
    int getPendingApplicationsCount(String employerUsername);
    List<JobPosting> getRecentJobPostings(String employerUsername);
    List<JobApplication> getRecentApplications(String employerUsername);
    void createJobPosting(JobPosting jobPosting);
    List<JobPosting> getAllJobPostings(String employerUsername);
    List<JobApplication> getAllApplications(String employerUsername);
    List<JobApplication> getApplicationsByJobId(Long jobId);
    EmployerProfile getEmployerProfile(String username);
} 