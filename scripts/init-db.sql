-- Initialize Numa database with required extensions and basic setup

-- Create UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create function to generate random UUIDs (for compatibility)
CREATE OR REPLACE FUNCTION gen_random_uuid() RETURNS uuid AS $$
BEGIN
  RETURN uuid_generate_v4();
END;
$$ LANGUAGE plpgsql;

-- Create function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create indexes for full-text search
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- Grant necessary permissions
GRANT ALL PRIVILEGES ON DATABASE numa_db TO numa_user;
GRANT ALL PRIVILEGES ON SCHEMA public TO numa_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO numa_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO numa_user;

-- Create development user (for dev environment)
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'numa_dev') THEN
        CREATE USER numa_dev WITH ENCRYPTED PASSWORD 'numa_dev_password';
        GRANT ALL PRIVILEGES ON DATABASE numa_dev TO numa_dev;
    END IF;
END
$$;

-- Insert sample data for development (only if in development mode)
-- This will be handled by Liquibase migrations in the application
