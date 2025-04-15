-- USERS
CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL
);

-- EMPLOYER PROFILES
CREATE TABLE IF NOT EXISTS employer_profiles (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL REFERENCES users(username),
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    company_name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    address TEXT,
    website VARCHAR(255),
    description TEXT,
    image_url VARCHAR(255),
    company_logo VARCHAR(255),
    company_size VARCHAR(50),
    company_industry VARCHAR(100),
    city VARCHAR(50),
    country VARCHAR(50),
    postal_code VARCHAR(20),
    email_notifications BOOLEAN DEFAULT FALSE,
    sms_notifications BOOLEAN DEFAULT FALSE,
    application_emails BOOLEAN DEFAULT FALSE,
    marketing_emails BOOLEAN DEFAULT FALSE
);

-- WORKER PROFILES
CREATE TABLE IF NOT EXISTS worker_profiles (
    username VARCHAR(50) PRIMARY KEY REFERENCES users(username),
    full_name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    address TEXT,
    skills TEXT,
    job_types TEXT,
    preferred_locations TEXT,
    image_url VARCHAR(255),
    resume_url VARCHAR(255)
);

-- JOB POSTINGS
CREATE TABLE IF NOT EXISTS job_postings (
    id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    company_name VARCHAR(100) NOT NULL,
    location VARCHAR(100),
    salary VARCHAR(50),
    job_type VARCHAR(50),
    experience_level VARCHAR(50),
    required_skills TEXT,
    posting_date DATE NOT NULL,
    expiry_date DATE,
    application_deadline DATE,
    active BOOLEAN DEFAULT TRUE,
    employer_username VARCHAR(50) NOT NULL REFERENCES employer_profiles(username)
);

-- JOB APPLICATIONS
CREATE TABLE IF NOT EXISTS job_applications (
    id SERIAL PRIMARY KEY,
    job_posting_id INTEGER NOT NULL REFERENCES job_postings(id) ON DELETE CASCADE,
    worker_username VARCHAR(50) NOT NULL REFERENCES worker_profiles(username),
    application_date TIMESTAMP NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    cover_letter TEXT
); 