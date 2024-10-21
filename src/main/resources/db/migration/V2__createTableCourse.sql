CREATE TABLE Course (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    name varchar(100) NOT NULL,
    code varchar(10) UNIQUE NOT NULL CHECK (code REGEXP '^[a-zA-Z]+(-[a-zA-Z]+)*$'),
    instructor_id bigint(20),
    description varchar(100),
    status enum('ACTIVE', 'INACTIVE') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVE',
    inactivation_date datetime,
    PRIMARY KEY (id),
    FOREIGN KEY (instructor_id) REFERENCES User(id)
);