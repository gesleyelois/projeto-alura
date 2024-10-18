package br.com.alura.ProjetoAlura.course;

import jakarta.persistence.*;

import java.util.Date;


@Entity(name = "Course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String code;

    private String description;

    private String instructorEmail;

    private Date inactiveDate;

    @Enumerated(EnumType.STRING)
    private CourseStatus status;

    public Course() {}

    public Course(String name, String code, String description, String instructorEmail, Date inactiveDate, CourseStatus status) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.instructorEmail = instructorEmail;
        this.inactiveDate = inactiveDate;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    public Date getInactiveDate() {
        return inactiveDate;
    }

    public void setInactiveDate(Date inactiveDate) {
        this.inactiveDate = inactiveDate;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }
}
