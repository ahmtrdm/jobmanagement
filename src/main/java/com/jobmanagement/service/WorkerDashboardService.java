package com.jobmanagement.service;

import com.jobmanagement.model.JobApplication;
import com.jobmanagement.model.JobPosting;
import com.jobmanagement.model.WorkerProfile;

import java.util.List;

public interface WorkerDashboardService {
    int getActiveApplicationsCount(String username);
    int getApprovedApplicationsCount(String username);
    int getRejectedApplicationsCount(String username);
    List<JobApplication> getRecentApplications(String username);
    List<JobPosting> getRecommendedJobs(String username);
    List<JobApplication> getAllApplications(String username);
    WorkerProfile getWorkerProfile(String username);
    WorkerProfile saveWorkerProfile(WorkerProfile profile);
} 