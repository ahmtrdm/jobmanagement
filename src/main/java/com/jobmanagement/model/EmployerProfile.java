package com.jobmanagement.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "employer_profiles")
public class EmployerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "title")
    private String title;

    @Column(name = "address")
    private String address;

    @Column(name = "website")
    private String website;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "company_logo")
    private String companyLogo;

    @Column(name = "company_size")
    private String companySize;

    @Column(name = "company_industry")
    private String companyIndustry;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "email_notifications")
    private boolean emailNotifications;

    @Column(name = "sms_notifications")
    private boolean smsNotifications;

    @Column(name = "application_emails")
    private boolean applicationEmails;

    @Column(name = "marketing_emails")
    private boolean marketingEmails;

    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL)
    private List<JobPosting> jobPostings;
} 