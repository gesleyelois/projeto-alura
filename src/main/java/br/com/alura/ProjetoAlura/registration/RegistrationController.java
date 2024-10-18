package br.com.alura.ProjetoAlura.registration;

import br.com.alura.ProjetoAlura.course.Course;
import br.com.alura.ProjetoAlura.course.CourseRepository;
import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.user.UserRepository;
import br.com.alura.ProjetoAlura.util.ErrorItemDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static br.com.alura.ProjetoAlura.course.CourseStatus.INACTIVE;

@RestController
public class RegistrationController {
    private final CourseRepository courseRepository;
    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;

    public RegistrationController(CourseRepository courseRepository, RegistrationRepository registrationRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
    }
    @PostMapping("/registration/new")
    public ResponseEntity newRegistration(@Valid @RequestBody RegistrationDTO newRegistration) {
        Course course = courseRepository.findByCode(newRegistration.getCode());
        User user = userRepository.findByEmail(newRegistration.getStudentEmail());
        if(course == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorItemDTO("code", "Curso nao encontrado"));
        }
        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorItemDTO("code", "Estudante nao cadastrado na plataforma"));
        }
        if(registrationRepository.existsByStudentEmailAndCode(newRegistration.getStudentEmail(), newRegistration.getCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("email", "Estudante ja cadastrado neste curso"));
        }
        if(course.getStatus() == INACTIVE){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("code", "Curso desativado"));
        }

        Registration registration = newRegistration.toModel();
        registrationRepository.save(registration);
        return ResponseEntity.status(HttpStatus.CREATED).body(registration);
    }

    @GetMapping("/registration/report")
    public ResponseEntity<List<RegistrationReportItem>> report() {
        List<RegistrationReportItem> items = new ArrayList<>();

        List<Object[]> results = registrationRepository.findCoursesWithRegistrationCount();
        List<RegistrationReportItem> courses = new ArrayList<>();

        for (Object[] result : results) {
            String courseName = (String) result[0];
            String courseCode = (String) result[1];
            String instructorName = (String) result[2];
            String instructorEmail = (String) result[3];
            Long totalRegistrations = (Long) result[4];

            courses.add(new RegistrationReportItem(courseName, courseCode, instructorName, instructorEmail, totalRegistrations));
        }

        return ResponseEntity.ok(courses);
    }

}
