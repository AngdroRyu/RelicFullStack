CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS relics (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    set_name TEXT,
    piece TEXT,
    slot TEXT,
    main_stat TEXT,
    main_value TEXT,
    image_path TEXT,
    timestamp TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS substats (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    relic_id INTEGER NOT NULL,
    name TEXT NOT NULL,
    value TEXT NOT NULL,

    total_rolls INTEGER,
    low_rolls INTEGER,
    med_rolls INTEGER,
    high_rolls INTEGER,

    FOREIGN KEY (relic_id) REFERENCES relics(id) ON DELETE CASCADE
);