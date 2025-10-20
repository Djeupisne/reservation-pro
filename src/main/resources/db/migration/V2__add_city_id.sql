-- Insert 15 sample cities
-- Insert 15 sample cities
INSERT INTO city (name, description, image, prix_voyage)
VALUES
    ('Paris', 'The capital of France.', NULL, 500.0),
    ('Tokyo', 'A bustling metropolis with vibrant culture.', NULL, 1200.0),
    ('New York', 'The city that never sleeps.', NULL, 800.0),
    ('London', 'The historic city with Big Ben and the Thames.', NULL, 600.0),
    ('Rome', 'Ancient city with the Colosseum.', NULL, 550.0),
    ('Sydney', 'Famous for the Sydney Opera House.', NULL, 1400.0),
    ('Cape Town', 'Stunning views of Table Mountain.', NULL, 1100.0),
    ('Rio de Janeiro', 'Home to Christ the Redeemer.', NULL, 1000.0),
    ('Dubai', 'City of skyscrapers and luxury.', NULL, 1100.0),
    ('Barcelona', 'Known for Gaudí architecture.', NULL, 500.0),
    ('Bangkok', 'Vibrant street markets and temples.', NULL, 700.0),
    ('Amsterdam', 'City of canals and museums.', NULL, 600.0),
    ('Istanbul', 'Bridge between Europe and Asia.', NULL, 650.0),
    ('Singapore', 'Modern city-state with Gardens by the Bay.', NULL, 950.0),
    ('Vancouver', 'Surrounded by mountains and ocean.', NULL, 850.0)
ON CONFLICT ON CONSTRAINT city_name_key DO NOTHING;
-- Add foreign key constraint to voyage table if it doesn't exist
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints
        WHERE table_name = 'voyage' AND constraint_name = 'fk_city'
    ) THEN
        ALTER TABLE voyage
        ADD CONSTRAINT fk_city FOREIGN KEY (city_id) REFERENCES city(id);
    END IF;
END $$;