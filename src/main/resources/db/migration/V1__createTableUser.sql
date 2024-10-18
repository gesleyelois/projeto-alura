CREATE TABLE User (
                      id BIGINT(20) NOT NULL AUTO_INCREMENT,
                      createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      name VARCHAR(50) NOT NULL,
                      email VARCHAR(50) NOT NULL,
                      password VARCHAR(100) NOT NULL,
                      role ENUM('STUDENT', 'INSTRUCTOR') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'STUDENT',
                      PRIMARY KEY (id),
                      CONSTRAINT UC_Email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

CREATE TABLE Course (
                        id BIGINT(20) NOT NULL AUTO_INCREMENT,
                        createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        name VARCHAR(50) NOT NULL,
                        code VARCHAR(10) NOT NULL,
                        description VARCHAR(500) NOT NULL,
                        instructorEmail VARCHAR(50) NOT NULL,
                        PRIMARY KEY (id),
                        CONSTRAINT UC_code UNIQUE (code),
                        CONSTRAINT FK_Instructor FOREIGN KEY (instructorEmail) REFERENCES User(email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

CREATE TABLE Registration (
                              id BIGINT(20) NOT NULL AUTO_INCREMENT,
                              course_code VARCHAR(10) NOT NULL,
                              studentEmail VARCHAR(50) NOT NULL,
                              createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              PRIMARY KEY (id),
                              FOREIGN KEY (course_code) REFERENCES Course(code),
                              FOREIGN KEY (studentEmail) REFERENCES User(email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
