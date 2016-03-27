CREATE TABLE IF NOT EXISTS Project (
	Name                TEXT                NOT NULL,
	Start_Date          TEXT                NOT NULL,
	Finish_Date         TEXT                NOT NULL,
	Description         TEXT
);

CREATE TABLE IF NOT EXISTS Tasks (
	Id                  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	Name                TEXT                NOT NULL,
	Start_Date          TEXT                NOT NULL,
	Finish_Date         TEXT                NOT NULL,
	Duration            INTEGER             NOT NULL,
	Done_Percent        REAL,
	Priority_Type       TEXT                NOT NULL,
	Cost                REAL,
	Description         TEXT,
	Task_Group          TEXT
);

CREATE TABLE IF NOT EXISTS Resources (
	Id                  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	Name                TEXT                NOT NULL,
	Resource_Type       TEXT                NOT NULL,
	Email               TEXT,
	Description         TEXT
);

CREATE TABLE IF NOT EXISTS Resources_By_Task (
	Id                  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	Task_Id             INTEGER             NOT NULL,
	Resource_Id         INTEGER
);

CREATE TABLE IF NOT EXISTS Child_Tasks (
	Id                  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	Task_Id             INTEGER             NOT NULL,
	Child_Task_Id       INTEGER             NOT NULL,
	Dependency_Type     TEXT
);

CREATE TABLE IF NOT EXISTS Parent_Tasks (
	Id                  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	Task_Id             INTEGER             NOT NULL,
	Parent_Task_Id      INTEGER             NOT NULL,
	Dependency_Type     TEXT                NOT NULL
);