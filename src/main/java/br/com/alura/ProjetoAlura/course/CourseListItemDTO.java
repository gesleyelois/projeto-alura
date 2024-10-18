package br.com.alura.ProjetoAlura.course;



import java.io.Serializable;

public class CourseListItemDTO implements Serializable {

    private String name;
    private String code;
    private String instructorEmail;
    private String description;
    private Status status;

    public CourseListItemDTO(Course course) {
        this.name = course.getName();
        this.code = course.getCode();
        this.instructorEmail = course.getInstructorEmail();
        this.description = course.getDescription();
        this.status = course.getStatus();
    }

    public String getName() {
        return name;
    }

    public String getCode() { return code;}

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public String getDescription(){ return description; }

    public Status getStatus() {
        return status;
    }
}
