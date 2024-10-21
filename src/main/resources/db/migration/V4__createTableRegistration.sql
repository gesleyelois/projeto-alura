CREATE TABLE Registration (
    id bigint(20) PRIMARY KEY AUTO_INCREMENT,
    user_id bigint(20),
    course_id bigint(20),
    registration_date DATE,
    FOREIGN KEY (user_id) REFERENCES User(id),
    FOREIGN KEY (course_id) REFERENCES Course(id)
);