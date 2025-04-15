package com.jobmanagement.repository;

import com.jobmanagement.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByJobPosting_EmployerUsername(String employerUsername);
    List<JobApplication> findTop5ByJobPosting_EmployerUsernameOrderByApplicationDateDesc(String employerUsername);
    List<JobApplication> findByJobPosting_Id(Long jobId);
    int countByJobPosting_EmployerUsername(String employerUsername);
    int countByJobPosting_EmployerUsernameAndStatus(String employerUsername, String status);
    List<JobApplication> findByJobPostingEmployerUsername(String employerUsername);
    List<JobApplication> findByJobPostingEmployerUsernameAndStatus(String employerUsername, String status);
    List<JobApplication> findByJobPostingEmployerUsernameOrderByApplicationDateDesc(String employerUsername);
    List<JobApplication> findByJobPostingIdOrderByApplicationDateDesc(Long jobId);
} 