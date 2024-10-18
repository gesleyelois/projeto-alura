package br.com.alura.ProjetoAlura.course;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import static br.com.alura.ProjetoAlura.course.CourseStatus.ACTIVE;

@JsonIgnoreProperties(ignoreUnknown = false)
public class CourseDTO {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    @Length(min = 4, max = 10, message="O codigo deve conter entre 4 a 10 caracteres")
    @Pattern(regexp = "^[a-zA-Z]+(-[a-zA-Z]+){0,2}$", message = "O código deve conter apenas letras e pode ser separado por hifen, com um total entre 4 e 10 caracteres")
    private String code;

    private String description;

    @NotNull
    @NotBlank
    @Email
    private String instructorEmail;

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

    public Course toModel(){
        return new Course(name,code,description,instructorEmail,null,ACTIVE);
    }
}
