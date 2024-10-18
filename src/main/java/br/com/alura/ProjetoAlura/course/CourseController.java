package br.com.alura.ProjetoAlura.course;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

@RestController
public class CourseController {

    @PostMapping("/course/new")
    public ResponseEntity<Void> createCourse(@Valid @RequestBody NewCourseDTO newCourse) {
        // TODO: Implementar a Questão 1 - Cadastro de Cursos aqui...
        if (!isValidCourseCode(newCourse.getCode())) {
        return ResponseEntity.badRequest().build(); 
    }

    if (courseRepository.existsByCode(newCourse.getCode())) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build(); 
    }

    Course course = new Course();
    course.setName(newCourse.getName());
    course.setCode(newCourse.getCode());
    course.setInstructor(newCourse.getInstructor());
    course.setDescription(newCourse.getDescription());
    course.setStatus(CourseStatus.ACTIVE);
    course.setDeactivationDate(null); 

    courseRepository.save(course); 

    return ResponseEntity.status(HttpStatus.CREATED).build(); 
}

    private boolean isValidCourseCode(String code) {
        return code.matches("^[a-zA-Z\\-]{4,10}$");
    }

     @PostMapping("/course/{code}/inactive")
     public ResponseEntity<Void> deactivateCourse(@PathVariable("code") String courseCode) {

        // TODO: Implementar a Questão 2 - Inativação de Curso aqui...
        Course course = courseRepository.findByCode(courseCode);
    if (course == null) {
        return ResponseEntity.notFound().build(); 
    }

    if (course.getStatus() == CourseStatus.INACTIVE) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build(); 
    }

    course.setStatus(CourseStatus.INACTIVE);
    course.setDeactivationDate(LocalDateTime.now()); 

    courseRepository.save(course); 

        return ResponseEntity.ok().build();
    }

}
