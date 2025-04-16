-- Insert initial data
INSERT INTO users (username, password, email, role) VALUES
('admin', '$2a$10$X7G3YFv2J5Z5Z5Z5Z5Z5Z.Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z', 'admin@example.com', 'ADMIN')
ON CONFLICT (username) DO NOTHING;

-- USERS
INSERT INTO users (username, password, email, role) VALUES ('employer1', '$2a$10$X7G3Y5H2U6I9K0L1M4N7P8Q9R0S1T2U3V4W5X6Y7Z8A9B0C1D2E3F4G5H6', 'employer1@example.com', 'EMPLOYER') ON CONFLICT (username) DO NOTHING;
INSERT INTO users (username, password, email, role) VALUES ('worker1', '$2a$10$X7G3Y5H2U6I9K0L1M4N7P8Q9R0S1T2U3V4W5X6Y7Z8A9B0C1D2E3F4G5H6', 'worker1@example.com', 'WORKER') ON CONFLICT (username) DO NOTHING;

-- EMPLOYER PROFILES
INSERT INTO employer_profiles (username, first_name, last_name, company_name, email, phone, address, website, description, image_url, company_logo, company_size, company_industry, city, country, postal_code, email_notifications, sms_notifications, application_emails, marketing_emails)
VALUES ('employer1', 'Ali', 'Yılmaz', 'Örnek Şirket', 'employer1@example.com', '5551234567', 'İstanbul, Türkiye', 'https://ornek.com', 'Örnek bir şirket açıklaması', NULL, NULL, '11-50', 'Bilişim', 'İstanbul', 'Türkiye', '34000', TRUE, FALSE, TRUE, FALSE)
ON CONFLICT (username) DO NOTHING;

-- WORKER PROFILES
INSERT INTO worker_profiles (username, full_name, email, phone, address, skills, job_types, preferred_locations, image_url, resume_url)
VALUES ('worker1', 'Ahmet Yılmaz', 'worker1@example.com', '5559876543', 'Ankara, Türkiye', 'Java,Spring Boot,SQL', 'FULL_TIME,REMOTE', 'Ankara,İstanbul', NULL, NULL)
ON CONFLICT (username) DO NOTHING;

-- JOB POSTINGS
INSERT INTO job_postings (title, description, company_name, location, salary, job_type, experience_level, required_skills, posting_date, expiry_date, application_deadline, active, employer_username)
VALUES ('Senior Java Developer', 'Deneyimli Java geliştirici aranıyor', 'Örnek Şirket', 'İstanbul', '25000-35000', 'FULL_TIME', 'SENIOR', 'Java,Spring Boot,Hibernate,PostgreSQL', CURRENT_DATE, CURRENT_DATE + INTERVAL '30 days', CURRENT_DATE + INTERVAL '20 days', TRUE, 'employer1');

-- JOB APPLICATIONS
INSERT INTO job_applications (job_posting_id, worker_username, application_date, status, cover_letter)
VALUES (1, 'worker1', NOW(), 'PENDING', 'Java alanında deneyimliyim, bu pozisyon için başvuruyorum.'); 