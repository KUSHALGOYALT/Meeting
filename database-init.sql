-- PostgreSQL Database Initialization Script for User Authentication App

-- Create database (run this as superuser)
-- CREATE DATABASE user_auth_db;

-- Connect to the database
-- \c user_auth_db;

-- Create users table (this will be created automatically by Hibernate, but here's the schema)
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_active ON users(is_active);

-- Grant necessary permissions (adjust as needed)
-- GRANT ALL PRIVILEGES ON TABLE users TO your_app_user; 