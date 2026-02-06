CREATE TABLE IF NOT EXISTS transactions (
    id SERIAL PRIMARY KEY,
    amount INTEGER NOT NULL CHECK (amount >= 0),
    merchant_name VARCHAR(255) NOT NULL,
    tenpist_name VARCHAR(255) NOT NULL,
    transaction_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_transactions_tenpist ON transactions(tenpist_name);
CREATE INDEX IF NOT EXISTS idx_transactions_date ON transactions(transaction_date DESC);
