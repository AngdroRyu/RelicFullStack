-- Create users table (SQLite automatically ignores if exists)
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    email TEXT NOT NULL
);

-- Seed data only if empty
INSERT INTO users (name, email)
SELECT 'Alice', 'alice@email.com'
WHERE NOT EXISTS (SELECT 1 FROM users);

INSERT INTO users (name, email)
SELECT 'Bob', 'bob@email.com'
WHERE NOT EXISTS (SELECT 1 FROM users);