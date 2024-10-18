package br.com.alura.ProjetoAlura.course;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Course {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String code;
    private String name;
    private String instructorEmail;
    private String description;
    
    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus;

	private LocalDateTime inactivationDate;
	
	public Course() {}

    public Course(String code, String name, String instructorEmail, String description) {
        this.code = code;
        this.name = name;
        this.instructorEmail = instructorEmail;
        this.description = description;
        this.courseStatus = CourseStatus.ACTIVE;
    }

    
    public String getCode() {
        return code;
    }
    
    public String getName() {
        return name;
    }
    
    public String getInstructorEmail() {
        return instructorEmail;
    }
    
    public String getDescription() {
        return description;
    }
    
    public CourseStatus getCourseStatus() {
        return courseStatus;
    }    
    
	public LocalDateTime getInactivationDate() {
		return inactivationDate;
	}
	
    public void setCourseStatus(CourseStatus courseStatus) {
		this.courseStatus = courseStatus;
	}


	public void setInactivationDate(LocalDateTime inactivationDate) {
		this.inactivationDate = inactivationDate;
	}
}
