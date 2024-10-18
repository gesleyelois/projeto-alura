package br.com.alura.ProjetoAlura.course;
import br.com.alura.ProjetoAlura.user.Role;
import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.user.UserRepository;
import br.com.alura.ProjetoAlura.util.ErrorItemDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class CourseController {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseController(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/course/new")
    public ResponseEntity createCourse(@Valid @RequestBody CourseDTO newCourse) {
        List<String> errorMessages = new ArrayList<>();
        String field = "";

        User user = userRepository.findByEmail((newCourse.getInstructorEmail()));
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorItemDTO("email", "Instrutor nao cadastrado na base de dados"));
        }

        if (user != null && user.getRole() == Role.STUDENT){
            errorMessages.add("Estudantes não podem criar cursos");
            field = "instructorEmail";
        }

        if (courseRepository.existsByCode(newCourse.getCode())) {
            errorMessages.add("O curso já está cadastrado");
            field = "code";
        }

        if (!errorMessages.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO(errorMessages.size() > 1 ? "code and instructorEmail" : field, String.join(" e ", errorMessages)));
        }

        Course course = newCourse.toModel();
        courseRepository.save(course);

        return ResponseEntity.status(HttpStatus.CREATED).body(course);
    }



    @PostMapping("/course/{code}/inactive")
    public ResponseEntity createCourse(@PathVariable("code") String courseCode) {
        Course course = courseRepository.findByCode(courseCode);
        if(course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Curso nao encontrado");

        }if (course.getStatus() == CourseStatus.INACTIVE){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Curso ja desativado");
        }
        else{
            course.setStatus(CourseStatus.INACTIVE);
            course.setInactiveDate(new Date());
            courseRepository.save(course);
            return ResponseEntity.status(HttpStatus.CREATED).body(course);

        }
    }

}
