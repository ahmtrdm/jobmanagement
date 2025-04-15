package com.jobmanagement.repository;

import com.jobmanagement.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByJobPostingEmployerUsername(String username);
    List<JobApplication> findByJobPostingEmployerUsernameAndStatus(String username, String status);
    List<JobApplication> findByJobPostingEmployerUsernameOrderByApplicationDateDesc(String username);
    List<JobApplication> findByWorkerUsernameOrderByApplicationDateDesc(String username);
    List<JobApplication> findTop5ByWorkerUsernameOrderByApplicationDateDesc(String username);
    int countByWorkerUsernameAndStatus(String username, String status);
} 