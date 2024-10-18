package br.com.alura.ProjetoAlura.registration;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id_registration;

    private String courseCode;

    private String studentEmail;

    private LocalDateTime registrationDate = LocalDateTime.now();

    @Deprecated
    public Registration() {}

    public Registration(String studentEmail, String courseCode) {
        this.studentEmail = studentEmail;
        this.courseCode = courseCode;
    }

    public LocalDateTime getCreatedAt() {
        return registrationDate;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public String getCourseCode() {
        return courseCode;
    }

}
