package br.com.alura.ProjetoAlura.registration;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.alura.ProjetoAlura.course.Course;
import br.com.alura.ProjetoAlura.user.User;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
   
	boolean existsByUserAndCourse(User user, Course course);
    
    @Query("SELECT new br.com.alura.ProjetoAlura.registration.RegistrationReportItem(" +
            "c.name, c.code, u.name, c.instructorEmail, COUNT(r.id)) " +
            "FROM Registration r " +
            "JOIN r.course c " +
            "JOIN User u ON u.email = c.instructorEmail " +
            "GROUP BY c.id, c.name, c.code, c.instructorEmail, u.name " +
            "ORDER BY COUNT(r.id) DESC")
    List<RegistrationReportItem> findCoursesWithMostRegistrations();

}
