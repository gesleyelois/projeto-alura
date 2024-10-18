ALTER TABLE Registration
DROP FOREIGN KEY FK_UserEmail;

ALTER TABLE Registration
    CHANGE userEmail studentEmail VARCHAR(50) NOT NULL;


ALTER TABLE Registration
    ADD CONSTRAINT FK_StudentEmail FOREIGN KEY (studentEmail) REFERENCES User(email);
