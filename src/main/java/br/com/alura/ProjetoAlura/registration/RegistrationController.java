package br.com.alura.ProjetoAlura.registration;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.ProjetoAlura.course.Course;
import br.com.alura.ProjetoAlura.course.CourseRepository;
import br.com.alura.ProjetoAlura.course.CourseStatus;
import br.com.alura.ProjetoAlura.user.Role;
import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.user.UserRepository;
import br.com.alura.ProjetoAlura.util.ErrorItemDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class RegistrationController {

    private final RegistrationRepository registrationRepository;
	private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    

	
	public RegistrationController(RegistrationRepository registrationRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.registrationRepository = registrationRepository;
		this.courseRepository = courseRepository;
        this.userRepository = userRepository;
	}
	
    @PostMapping("/registration/new")
    public ResponseEntity createCourse(@Valid @RequestBody NewRegistrationDTO newRegistration) {
        Course course = courseRepository.findByCode(newRegistration.getCourseCode());

		if(Objects.isNull(course)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorItemDTO("code", "Curso não existe no sistema"));
		}
		
        if (course.getCourseStatus() == CourseStatus.INACTIVE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorItemDTO("status", "O curso já está inativo"));
        }
    	
        User user = userRepository.findByEmail(newRegistration.getStudentEmail());

        if (Objects.isNull(user)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("email", "Usuário não encontrado no sistema"));
        }
        
        if(!Objects.equals(user.getRole(), Role.STUDENT)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Usuário não é estudante");
        }
        
        if (registrationRepository.existsByUserAndCourse(user, course)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já está matriculado neste curso.");
        }

        Registration registration = new Registration(user, course);
        registrationRepository.save(registration);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/registration/report")
    public ResponseEntity<List<RegistrationReportItem>> report() {

        List<RegistrationReportItem> items = registrationRepository.findCoursesWithMostRegistrations();

        // Dados fictícios abaixo que devem ser substituídos
//        items.add(new RegistrationReportItem(
//                "Java para Iniciantes",
//                "java",
//                "Charles",
//                "charles@alura.com.br",
//                10L
//        ));
//
//        items.add(new RegistrationReportItem(
//                "Spring para Iniciantes",
//                "spring",
//                "Charles",
//                "charles@alura.com.br",
//                9L
//        ));
//
//        items.add(new RegistrationReportItem(
//                "Maven para Avançados",
//                "maven",
//                "Charles",
//                "charles@alura.com.br",
//                9L
//        ));

        return ResponseEntity.ok(items);
    }

}
