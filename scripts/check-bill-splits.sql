-- Check if bill_splits table exists and create if missing
-- This is a fallback script in case migrations didn't run properly

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
