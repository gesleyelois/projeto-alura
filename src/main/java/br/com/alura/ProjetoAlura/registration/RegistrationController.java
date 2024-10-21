package br.com.alura.ProjetoAlura.registration;

import br.com.alura.ProjetoAlura.course.Course;
import br.com.alura.ProjetoAlura.course.CourseRepository;
import br.com.alura.ProjetoAlura.course.Status;
import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.user.UserRepository;
import br.com.alura.ProjetoAlura.util.ErrorItemDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("course-inactive", "Não é possível se matricular em um curso inativo."));
        }

        if (registrationRepository.existsByUserAndCourse(user, course)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("registration", "Você já está matriculado neste curso."));
        }

        Registration registration = newRegistration.toModel(user, course);

        registrationRepository.save(registration);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/registration/report")
    public ResponseEntity<List<RegistrationReportItemProjection>> report() {
        List<RegistrationReportItemProjection> items = courseRepository.findCoursesWithEnrollmentCount();
        return ResponseEntity.ok(items);
    }



}
