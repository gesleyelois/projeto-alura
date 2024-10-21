package br.com.alura.ProjetoAlura.course;

import br.com.alura.ProjetoAlura.user.User;

import java.io.Serializable;

public class CourseListItemDTO implements Serializable {

    private String name;
    private String code;
    private String description;
    private User instructor;
    private Status status;

    public CourseListItemDTO(Course course) {
        this.name = course.getName();
        this.code = course.getCode();
        this.description = course.getDescription();
        this.instructor = course.getInstructor();
        this.status = course.getStatus();
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public User getInstructor() {
        return instructor;
    }

    public Status getStatus() {
        return status;
    }
}
