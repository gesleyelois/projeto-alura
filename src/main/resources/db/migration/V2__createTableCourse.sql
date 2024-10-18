CREATE TABLE Course (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    code varchar(10) NOT NULL,
    name varchar(50) NOT NULL,
    description text,
    courseStatus enum('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    instructorEmail varchar(50) NOT NULL,
    inactivationDate datetime DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT UC_Code UNIQUE (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;