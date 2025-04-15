package com.jobmanagement.repository;

import com.jobmanagement.model.EmployerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployerProfileRepository extends JpaRepository<EmployerProfile, Long> {
    EmployerProfile findByUsername(String username);
} 