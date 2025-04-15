package com.jobmanagement.service;

import com.jobmanagement.model.JobApplication;
import com.jobmanagement.model.JobPosting;
import com.jobmanagement.model.EmployerProfile;

import java.util.List;

public interface EmployerDashboardService {
    int getActiveJobPostingsCount(String username);
    int getTotalApplicationsCount(String username);
    int getPendingApplicationsCount(String username);
    List<JobPosting> getRecentJobPostings(String username);
    List<JobApplication> getRecentApplications(String username);
    List<JobPosting> getAllJobPostings(String username);
    List<JobApplication> getAllApplications(String username);
    EmployerProfile getEmployerProfile(String username);
    void createJobPosting(JobPosting jobPosting);
} 