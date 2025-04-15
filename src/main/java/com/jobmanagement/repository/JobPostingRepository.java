package com.jobmanagement.repository;

import com.jobmanagement.model.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    List<JobPosting> findByEmployerUsernameAndActiveTrue(String username);
    List<JobPosting> findByEmployerUsernameOrderByPostingDateDesc(String username);
    List<JobPosting> findByActiveTrueOrderByPostingDateDesc();
    
    @Query("SELECT j FROM JobPosting j WHERE j.active = true AND " +
           "(:skills IS NULL OR j.requiredSkills LIKE %:skills%) AND " +
           "(:location IS NULL OR j.location = :location) AND " +
           "(:experienceLevel IS NULL OR j.experienceLevel = :experienceLevel) " +
           "ORDER BY j.postingDate DESC")
    List<JobPosting> findRecommendedJobs(
        @Param("skills") String skills,
        @Param("location") String location,
        @Param("experienceLevel") String experienceLevel
    );

    List<JobPosting> findByEmployerUsername(String employerUsername);
    List<JobPosting> findTop5ByEmployerUsernameOrderByPostingDateDesc(String employerUsername);
    int countByEmployerUsernameAndActiveTrue(String employerUsername);
} 