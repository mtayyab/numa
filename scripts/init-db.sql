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

-- Create development database and user (for dev environment)
DO $$
BEGIN
    -- Create development database
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'numa_dev') THEN
        CREATE DATABASE numa_dev;
    END IF;
    
    -- Create development user
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'numa_dev') THEN
        CREATE USER numa_dev WITH ENCRYPTED PASSWORD 'numa_dev_password';
    END IF;
    
    -- Grant privileges on development database
    GRANT ALL PRIVILEGES ON DATABASE numa_dev TO numa_dev;
END
$$;

-- Insert sample data for development (only if in development mode)
-- This will be handled by Liquibase migrations in the application

-- Ensure bill_splits table exists (fallback in case migrations didn't run)
DO $$
BEGIN
    -- Check if bill_splits table exists
    IF NOT EXISTS (
        SELECT FROM information_schema.tables 
        WHERE table_schema = 'public' 
        AND table_name = 'bill_splits'
    ) THEN
        -- Create bill_splits table
        CREATE TABLE bill_splits (
            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
            session_id UUID NOT NULL,
            guest_id UUID NOT NULL,
            split_type VARCHAR(20) NOT NULL DEFAULT 'EQUAL',
            amount DECIMAL(10,2) NOT NULL,
            percentage DECIMAL(5,2),
            payment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
            payment_method VARCHAR(20),
            paid_at TIMESTAMP,
            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
            
            CONSTRAINT fk_bill_splits_session 
                FOREIGN KEY (session_id) REFERENCES dining_sessions(id),
            CONSTRAINT fk_bill_splits_guest 
                FOREIGN KEY (guest_id) REFERENCES session_guests(id)
        );
        
        -- Create indexes
        CREATE INDEX idx_bill_splits_session_id ON bill_splits(session_id);
        CREATE INDEX idx_bill_splits_guest_id ON bill_splits(guest_id);
        CREATE INDEX idx_bill_splits_payment_status ON bill_splits(payment_status);
        
        RAISE NOTICE 'bill_splits table created successfully';
    ELSE
        RAISE NOTICE 'bill_splits table already exists';
    END IF;
END
$$;
