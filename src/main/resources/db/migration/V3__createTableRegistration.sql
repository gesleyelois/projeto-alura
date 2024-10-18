CREATE TABLE Registration (
    id_registration BIGINT(20) NOT NULL AUTO_INCREMENT,
    userEmail VARCHAR(50) NOT NULL,
    courseCode VARCHAR(50) NOT NULL,
    registrationDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_registration),
    CONSTRAINT FK_UserEmail FOREIGN KEY (userEmail) REFERENCES User(email),
    CONSTRAINT FK_CourseCode FOREIGN KEY (courseCode) REFERENCES Course(code),
    CONSTRAINT UC_UserCourse UNIQUE (userEmail, courseCode)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

