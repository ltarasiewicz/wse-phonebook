CREATE TABLE IF NOT EXISTS main.contact_records
(
    id             INTEGER PRIMARY KEY,
    contact_number TEXT NOT NULL UNIQUE,
    name           TEXT NOT NULL,
    surname        TEXT NOT NULL
);
