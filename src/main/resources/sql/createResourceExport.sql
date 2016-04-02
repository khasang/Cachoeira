CREATE TABLE IF NOT EXISTS Resources (
	Id                  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	Name                TEXT                NOT NULL,
	Resource_Type       TEXT                NOT NULL,
	Email               TEXT,
	Description         TEXT
);