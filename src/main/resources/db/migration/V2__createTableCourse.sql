CREATE TABLE Course (
    id_course BIGINT(20) NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) NOT NULL,
    instructorEmail VARCHAR(50) NOT NULL,
    description TEXT,
    status enum('ACTIVE', 'INACTIVE') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVE',
    inactivatedAt DATETIME DEFAULT NULL,
    createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_course),
    CONSTRAINT FK_InstructorEmail FOREIGN KEY (instructorEmail) REFERENCES User(email),
    CONSTRAINT UC_Code UNIQUE (code)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
