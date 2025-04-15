-- Örnek admin kullanıcısı
INSERT INTO users (username, password, email, role)
VALUES ('admin', '$2a$10$X7G3Y5H2U6I9K0L1M4N7P8Q9R0S1T2U3V4W5X6Y7Z8A9B0C1D2E3F4G5H6', 'admin@example.com', 'ADMIN')
ON CONFLICT (username) DO NOTHING;

-- Örnek işveren kullanıcısı
INSERT INTO users (username, password, email, role)
VALUES ('employer', '$2a$10$X7G3Y5H2U6I9K0L1M4N7P8Q9R0S1T2U3V4W5X6Y7Z8A9B0C1D2E3F4G5H6', 'employer@example.com', 'EMPLOYER')
ON CONFLICT (username) DO NOTHING;

-- Örnek işçi kullanıcısı
INSERT INTO users (username, password, email, role)
VALUES ('worker', '$2a$10$X7G3Y5H2U6I9K0L1M4N7P8Q9R0S1T2U3V4W5X6Y7Z8A9B0C1D2E3F4G5H6', 'worker@example.com', 'WORKER')
ON CONFLICT (username) DO NOTHING;

-- Örnek işveren profili
INSERT INTO employer_profiles (username, company_name, phone, address, description)
VALUES ('employer', 'Örnek Şirket', '5551234567', 'İstanbul, Türkiye', 'Örnek bir şirket açıklaması')
ON CONFLICT (username) DO NOTHING;

-- Örnek işçi profili
INSERT INTO worker_profiles (username, first_name, last_name, phone, address, skills, experience, education)
VALUES ('worker', 'Ahmet', 'Yılmaz', '5559876543', 'Ankara, Türkiye', 'Java, Spring Boot, SQL', '3 yıl yazılım geliştirme', 'Bilgisayar Mühendisliği')
ON CONFLICT (username) DO NOTHING;

-- Örnek iş ilanı
INSERT INTO job_postings (title, description, company_name, location, salary, job_type, experience_level, required_skills, posting_date, expiry_date, application_deadline, active, employer_username)
VALUES (
    'Senior Java Developer',
    'Deneyimli Java geliştirici aranıyor',
    'Örnek Şirket',
    'İstanbul',
    25000.00,
    'FULL_TIME',
    'SENIOR',
    'Java, Spring Boot, Hibernate, PostgreSQL',
    CURRENT_DATE,
    CURRENT_DATE + INTERVAL '30 days',
    CURRENT_DATE + INTERVAL '20 days',
    true,
    'employer'
); 