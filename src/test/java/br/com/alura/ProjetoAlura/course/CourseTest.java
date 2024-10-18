package br.com.alura.ProjetoAlura.course;

import br.com.alura.ProjetoAlura.user.User;
import org.junit.jupiter.api.Test;

import static br.com.alura.ProjetoAlura.user.Role.*;
import static org.assertj.core.api.Assertions.assertThat;


public class CourseTest {
	
    @Test
    void course_must_be_activated_at_creation() {
        User user = new User("instructor", "instructor1@alura.com", INSTRUCTOR, "password");
        
        Course course = new Course("test1", "test", user.getEmail(), "");
        
        assertThat(course.getCourseStatus())
                .isEqualTo(CourseStatus.ACTIVE);
        
        assertThat(course.getInactivationDate()).isNull();
    }
}