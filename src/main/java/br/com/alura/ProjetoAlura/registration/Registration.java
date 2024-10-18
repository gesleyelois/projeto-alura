package br.com.alura.ProjetoAlura.registration;

import java.time.LocalDateTime;

import br.com.alura.ProjetoAlura.course.Course;
import br.com.alura.ProjetoAlura.user.User;
import jakarta.persistence.*;

@Entity
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;


	@ManyToOne
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;

    private LocalDateTime registrationDate;

    public Registration(User user, Course course) {
        this.user = user;
        this.course = course;
        this.registrationDate = LocalDateTime.now();
    }

    public Course getCourse() {
        return course;
    }

    public User getUser() {
		return user;
	}
    
}