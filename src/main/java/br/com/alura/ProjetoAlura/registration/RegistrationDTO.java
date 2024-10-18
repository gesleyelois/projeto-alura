package br.com.alura.ProjetoAlura.registration;

import br.com.alura.ProjetoAlura.course.Course;
import br.com.alura.ProjetoAlura.user.NewStudentUserDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class RegistrationDTO {
    @NotBlank
    @NotNull
    @Email
    private String studentEmail;

    private String code;

    public RegistrationDTO() {}

    public String getStudentEmail() {
        return studentEmail;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public Registration toModel(){
        return new Registration(studentEmail,code,new Date());
    }

}
