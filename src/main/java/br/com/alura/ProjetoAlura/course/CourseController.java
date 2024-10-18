package br.com.alura.ProjetoAlura.course;

import br.com.alura.ProjetoAlura.user.Role;
import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.user.UserRepository;
import br.com.alura.ProjetoAlura.util.ErrorItemDTO;
import br.com.alura.ProjetoAlura.util.SuccessResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class CourseController {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseController(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/course/new")
    public ResponseEntity createCourse(@Valid @RequestBody NewCourseDTO newCourse) {
        if(courseRepository.existsByCode(newCourse.getCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("code", "Código de curso já cadastrado no sistema"));
        }

        Optional<User> instructorUser = userRepository.findByEmail(newCourse.getInstructorEmail());

        if (instructorUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("email", "Instrutor com o email informado não encontrado no sistema"));
        }

        if(instructorUser.get().getRole() != Role.INSTRUCTOR){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("email", "Email fornecido não pertence a um instrutor"));
        }

        Course course = newCourse.toModel();
        courseRepository.save(course);

        SuccessResponseDTO successResponse = new SuccessResponseDTO(
                "Curso criado com sucesso!"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
    }

    @PostMapping("/course/{code}/inactive")
    public ResponseEntity<?> inactivateCourse(@PathVariable("code") String courseCode) {
        Optional<Course> optionalCourse = courseRepository.findByCode(courseCode);

        if (optionalCourse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorItemDTO("code", "Curso não encontrado"));
        }

        Course course = optionalCourse.get();

        if (course.getStatus() == Status.INACTIVE) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("status", "O curso já está inativo"));
        }

        course.setStatus(Status.INACTIVE);
        course.setInactivatedAt(LocalDateTime.now());

        courseRepository.save(course);
        SuccessResponseDTO successResponse = new SuccessResponseDTO(
                "Curso inativado com sucesso!"
        );

        return ResponseEntity.ok(successResponse);
    }


    @GetMapping("/course/all")
    public List<CourseListItemDTO> listAllCourses() {
        return courseRepository.findAll().stream().map(CourseListItemDTO::new).toList();
    }

}
