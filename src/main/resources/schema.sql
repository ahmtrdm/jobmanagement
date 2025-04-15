-- Kullanıcılar tablosu
CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL
);

-- İşveren profilleri tablosu
CREATE TABLE IF NOT EXISTS employer_profiles (
    username VARCHAR(50) PRIMARY KEY REFERENCES users(username),
    company_name VARCHAR(100),
    phone VARCHAR(20),
    address TEXT,
    description TEXT,
    image_url VARCHAR(255)
);

-- İşçi profilleri tablosu
CREATE TABLE IF NOT EXISTS worker_profiles (
    username VARCHAR(50) PRIMARY KEY REFERENCES users(username),
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    phone VARCHAR(20),
    address TEXT,
    skills TEXT,
    experience TEXT,
    education TEXT,
    image_url VARCHAR(255)
);

-- İş ilanları tablosu
CREATE TABLE IF NOT EXISTS job_postings (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    company_name VARCHAR(100) NOT NULL,
    location VARCHAR(100),
    salary DECIMAL(10,2),
    job_type VARCHAR(50),
    experience_level VARCHAR(50),
    required_skills TEXT,
    posting_date DATE NOT NULL,
    expiry_date DATE,
    application_deadline DATE,
    active BOOLEAN DEFAULT true,
    employer_username VARCHAR(50) REFERENCES users(username)
);

-- İş başvuruları tablosu
CREATE TABLE IF NOT EXISTS job_applications (
    id BIGSERIAL PRIMARY KEY,
    job_posting_id BIGINT REFERENCES job_postings(id),
    worker_username VARCHAR(50) REFERENCES users(username),
    application_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    cover_letter TEXT,
    UNIQUE(job_posting_id, worker_username)
); 