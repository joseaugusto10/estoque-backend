ALTER TABLE movimento_estoque
ADD COLUMN data_movimento TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

CREATE INDEX idx_movimento_data_movimento ON movimento_estoque (data_movimento);