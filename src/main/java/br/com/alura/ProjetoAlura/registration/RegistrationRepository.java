package br.com.alura.ProjetoAlura.registration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    boolean existsByCourseCodeAndStudentEmail(String code, String studentEmail);

    @Query(value = """
            SELECT c.name as courseName, c.code as courseCode, u.name as instructorName, u.email as instructorEmail, COUNT(r.id_registration) as totalRegistrations
            FROM Course c
            JOIN Registration r ON c.code = r.courseCode
            JOIN User u ON c.instructorEmail = u.email
            GROUP BY c.code
            ORDER BY totalRegistrations DESC
            """, nativeQuery = true)
    List<Object[]> findCoursesWithMostRegistrations();

}
