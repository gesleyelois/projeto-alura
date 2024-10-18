package br.com.alura.ProjetoAlura.registration;

import br.com.alura.ProjetoAlura.course.Course;
import br.com.alura.ProjetoAlura.course.CourseRepository;
import br.com.alura.ProjetoAlura.course.Status;
import br.com.alura.ProjetoAlura.user.Role;
import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.user.UserRepository;
import br.com.alura.ProjetoAlura.util.ErrorItemDTO;
import br.com.alura.ProjetoAlura.util.SuccessResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class RegistrationController {

    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public RegistrationController(RegistrationRepository registrationRepository, UserRepository userRepository, CourseRepository courseRepository) {
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @PostMapping("/registration/new")
    public ResponseEntity createRegistration(@Valid @RequestBody NewRegistrationDTO newRegistration) {
        if(registrationRepository.existsByCourseCodeAndStudentEmail(newRegistration.getCourseCode(), newRegistration.getStudentEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("email", "Estudante já matriculado no curso"));
        }

        Optional<User> studentEmail = userRepository.findByEmail(newRegistration.getStudentEmail());

        if (studentEmail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("email", "Estudante não encontrado com o email fornecido"));
        }

        if (studentEmail.get().getRole() != Role.STUDENT) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("email", "O email fornecido não pertence a um aluno"));
        }

        Optional<Course> courseCode = courseRepository.findByCode(newRegistration.getCourseCode());

        if (courseCode.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("code", "Curso não encontrado com o código fornecido"));
        }

        if(courseCode.get().getStatus() != Status.ACTIVE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("code", "Curso não está recebendo matrículas no momento"));
        }

        Registration registration = newRegistration.toModel();

        registrationRepository.save(registration);

        SuccessResponseDTO successResponse = new SuccessResponseDTO(
                "Aluno inscrito no curso com sucesso!"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
    }

    @GetMapping("/registration/report")
    public ResponseEntity<List<RegistrationReportItem>> report() {

        List<Object[]> results = registrationRepository.findCoursesWithMostRegistrations();

        List<RegistrationReportItem> items = new ArrayList<>();

        for (Object[] result : results) {
            String courseName = (String) result[0];
            String courseCode = (String) result[1];
            String instructorName = (String) result[2];
            String instructorEmail = (String) result[3];
            Long totalRegistrations = (Long) result[4];

            items.add(new RegistrationReportItem(courseName, courseCode, instructorName, instructorEmail, totalRegistrations));
        }

        return ResponseEntity.ok(items);
    }

}
