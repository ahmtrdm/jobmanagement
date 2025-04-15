package com.jobmanagement.repository;

import com.jobmanagement.model.WorkerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerProfileRepository extends JpaRepository<WorkerProfile, Long> {
    WorkerProfile findByUsername(String username);
} 