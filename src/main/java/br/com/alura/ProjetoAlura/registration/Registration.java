package br.com.alura.ProjetoAlura.registration;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity()
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotNull
    @Email
    private String studentEmail;

    @Column(name="course_code")
    private String code;

    private Date registrationDate;

    public Registration() {}

    public Registration(String studentEmail, String code, Date registrationDate) {
        this.studentEmail = studentEmail;
        this.code = code;
        this.registrationDate = registrationDate;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }
}
