package br.com.alura.ProjetoAlura.course;

import br.com.alura.ProjetoAlura.user.Role;
import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.user.UserListItemDTO;
import br.com.alura.ProjetoAlura.user.UserRepository;
import br.com.alura.ProjetoAlura.util.ErrorItemDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.apache.tomcat.util.digester.Rule;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CourseController {

    @Autowired
    private final CourseRepository courseRepository;
    @Autowired
    private final UserRepository userRepository;

    public CourseController(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;

    }

    @PostMapping("/course/new")
    public ResponseEntity createCourse(@Valid @RequestBody NewCourseDTO newCourse) {
        // TODO: Implementar a Questão 1 - Cadastro de Cursos aqui...

        User instructor =
                userRepository.findUserById(newCourse.getInstructorId());

        if (!instructor.getRole().equals((Role.INSTRUCTOR))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("role", "Este usuário não está registrado como instrutor e somente " +
                            "instrutores podem ser " +
                            "vinculados a cursos."));
        }

        if (!newCourse.getCode().matches("[a-z\\-]+") || newCourse.getCode().length() < 4 || newCourse.getCode().length() > 10) {
            return ResponseEntity.badRequest().body("Código de curso inválido. O código do curso deve conter apenas " +
                    "letras e hífen e ter entre 4 e 10 caracteres.");
        }

        if (courseRepository.existsByCode(newCourse.getCode())) {
            return ResponseEntity.badRequest().body("Código já cadastrado no sistema");
        }

        Course course = newCourse.toModel(instructor);

        courseRepository.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/course/{code}/inactive")
    public ResponseEntity createCourse(@PathVariable("code") String courseCode) {
        // TODO: Implementar a Questão 2 - Inativação de Curso aqui...
        Course course = courseRepository.findByCode(courseCode);

        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        course.inactivateCourse();
        courseRepository.save(course);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/course/all")
    public List<CourseListItemDTO> listAllCourses() {
        return courseRepository.findAll().stream().map(CourseListItemDTO::new).toList();
    }

}
