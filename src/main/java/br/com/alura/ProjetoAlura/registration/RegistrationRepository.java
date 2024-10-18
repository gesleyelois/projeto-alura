package br.com.alura.ProjetoAlura.registration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByStudentEmailAndCode(String studentEmail, String code);
    @Query(value = "SELECT c.name AS courseName, c.code AS courseCode, u.name AS instructorName, " +
            "u.email AS instructorEmail, COUNT(r.studentEmail) AS totalRegistrations " +
            "FROM Course c " +
            "JOIN Registration r ON c.code = r.course_code " +
            "JOIN User u ON c.instructorEmail = u.email " +
            "WHERE u.role = 'INSTRUCTOR' " +
            "GROUP BY c.code, u.email " +
            "ORDER BY totalRegistrations DESC",
            nativeQuery = true)
    List<Object[]> findCoursesWithRegistrationCount();

}
