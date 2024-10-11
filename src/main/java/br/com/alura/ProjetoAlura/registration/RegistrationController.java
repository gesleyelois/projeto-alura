package br.com.alura.ProjetoAlura.registration;

import br.com.alura.ProjetoAlura.course.Course;
import br.com.alura.ProjetoAlura.course.CourseRepository;
import br.com.alura.ProjetoAlura.course.Status;
import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.user.UserListItemDTO;
import br.com.alura.ProjetoAlura.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RegistrationController {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    public RegistrationController(UserRepository userRepository, RegistrationRepository registrationRepository, CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.registrationRepository = registrationRepository;
        this.courseRepository = courseRepository;
    }

    @PostMapping("/registration/new")
    public ResponseEntity createCourse(@Valid @RequestBody NewRegistrationDTO newRegistration) {
        // TODO: Implementar a Questão 3 - Criação de Matrículas aqui...

        User user = userRepository.findByEmail(newRegistration.getStudentEmail());
        Course course = courseRepository.findByCode(newRegistration.getCourseCode());

        if (course.getStatus().equals(Status.INACTIVE)) {
            return ResponseEntity.badRequest().body("Não é possível se matricular em um curso inativo.");
        }

        if (registrationRepository.existsByUserAndCourse(user, course)) {
            return ResponseEntity.badRequest().body("Você já está matriculado neste curso.");
        }

        Enrollment enrollment = newRegistration.toModel(user, course);

        registrationRepository.save(enrollment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/registration/report")
    public ResponseEntity<List<RegistrationReportItem>> report() {
        List<RegistrationReportItem> items = new ArrayList<>();
        // TODO: Implementar a Questão 4 - Relatório de Cursos Mais Acessados aqui...

        for (Object[] row : courseRepository.findCoursesWithEnrollmentCount()) {
            String courseName = (String) row[0];
            String courseCode = (String) row [1];
            String instructorName = (String) row [2];
            String instructorEmail = (String) row [3];
            Long totalEnrollments = (Long) row [4];

            items.add(new RegistrationReportItem(
                    courseName,
                    courseCode,
                    instructorName,
                    instructorEmail,
                    totalEnrollments
            ));
        }

        return ResponseEntity.ok(items);
    }



}
