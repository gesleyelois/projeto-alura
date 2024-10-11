package br.com.alura.ProjetoAlura.course;

import br.com.alura.ProjetoAlura.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.time.LocalDateTime;

@Entity
@Table(name = "course")
public class Course {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    private String description;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private User instructor;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;
    @Column(name = "inactivation_date")
    private LocalDateTime inactivationDate = null;

    @Deprecated
    public Course() {}

    public Course(String name, String code, String description, User instructor) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.instructor = instructor;
    }

    public String getName() { return name; }

    public String getCode() { return code; }

    public String getDescription() { return description; }

    public Status getStatus() { return status; }

    public User getInstructor() {
        return instructor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setInstructor(User instructor) {
        this.instructor = instructor;
    }

    public void setInactivationDate(LocalDateTime inactivationDate) {
        this.inactivationDate = inactivationDate;
    }

    public LocalDateTime getInactivationDate() { return inactivationDate; }

    public void inactivateCourse() {
        this.status = Status.INACTIVE;
        this.inactivationDate = LocalDateTime.now();
    }
}
