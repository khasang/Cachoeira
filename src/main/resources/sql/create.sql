CREATE TABLE IF NOT EXISTS Resource_Type (
	Id                  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	Type                TEXT                NOT NULL
);

CREATE TABLE IF NOT EXISTS Priority_Type (
	Id                  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	Type                TEXT                NOT NULL
);

CREATE TABLE IF NOT EXISTS Dependency_Type (
	Id                  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	Type                TEXT                NOT NULL
);

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
	Priority_Type_Id    INTEGER             NOT NULL,
	Cost                REAL,
	Description         TEXT,
	Task_Group          TEXT,
	FOREIGN KEY (Priority_Type_Id) REFERENCES Priority_Type(Id)
);

CREATE TABLE IF NOT EXISTS Resources (
	Id                  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	Name                TEXT                NOT NULL,
	Type_Id             INTEGER             NOT NULL,
	Email               TEXT,
	Description         TEXT,
	FOREIGN KEY (Type_Id) REFERENCES Resource_Type(Id)
);

CREATE TABLE IF NOT EXISTS Main_Task_List_Table (
	Id                  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	Task_Id             INTEGER             NOT NULL,
	Parent_Task_Id      INTEGER,
	Child_Task_Id       INTEGER,
	Resource_Id         INTEGER,
	FOREIGN KEY (Task_Id) REFERENCES Tasks(Id),
	FOREIGN KEY (Parent_Task_Id) REFERENCES Parent_Tasks(Id),
	FOREIGN KEY (Child_Task_Id) REFERENCES Child_Tasks(Id),
	FOREIGN KEY (Resource_Id) REFERENCES Resources(Id)
);

CREATE TABLE IF NOT EXISTS Child_Tasks (
	Id                  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	Task_Id             INTEGER             NOT NULL,
	Dependency_Type_Id  INTEGER,
	FOREIGN KEY (Task_Id) REFERENCES Tasks(Id),
	FOREIGN KEY (Dependency_Type_Id) REFERENCES Dependency_Type(Id)
);

CREATE TABLE IF NOT EXISTS Parent_Tasks (
	Id                  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	Task_Id             INTEGER             NOT NULL,
	Dependency_Type_Id  INTEGER             NOT NULL,
	FOREIGN KEY (Task_Id) REFERENCES Tasks(Id),
    FOREIGN KEY (Dependency_Type_Id) REFERENCES Dependency_Type(Id)
);

INSERT INTO Resource_Type(Type) VALUES ('STUFF');
INSERT INTO Resource_Type(Type) VALUES ('TOOL');
INSERT INTO Resource_Type(Type) VALUES ('MATERIAL');

INSERT INTO Priority_Type(Type) VALUES ('High');
INSERT INTO Priority_Type(Type) VALUES ('Normal');
INSERT INTO Priority_Type(Type) VALUES ('Low');

INSERT INTO Dependency_Type(Type) VALUES ('STARTSTART');
INSERT INTO Dependency_Type(Type) VALUES ('STARTFINISH');
INSERT INTO Dependency_Type(Type) VALUES ('FINISHSTART');
INSERT INTO Dependency_Type(Type) VALUES ('FINISHFINISH');

