package br.com.alura.ProjetoAlura.course;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.ProjetoAlura.user.Role;
import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.user.UserRepository;
import br.com.alura.ProjetoAlura.util.ErrorItemDTO;

@RestController
public class CourseController {
	
	private final CourseRepository courseRepository;
    private final UserRepository userRepository;

	
	public CourseController(CourseRepository courseRepository, UserRepository userRepository) {
		this.courseRepository = courseRepository;
        this.userRepository = userRepository;

	}
	
    @Transactional
    @PostMapping("/course/new")
    public ResponseEntity createCourse(@Valid @RequestBody NewCourseDTO newCourseDTO) {

    	if(courseRepository.existsByCode(newCourseDTO.getCode())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorItemDTO("code", "Código já cadastrado no sistema"));
		}
		    		
        User user = userRepository.findByEmail(newCourseDTO.getInstructorEmail());

        if(user == null) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorItemDTO("email", "Não há usuário com esse email cadastrado"));
        }
        
        if(!Objects.equals(user.getRole(), Role.INSTRUCTOR )) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorItemDTO("email", "Usuário deve ser instrutor para cadastrar um curso"));

        }
        
		Course course = newCourseDTO.toModel();
		courseRepository.save(course);
    		
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Transactional
    @PostMapping("/course/{code}/inactive")
    public ResponseEntity createCourse(@PathVariable("code") String courseCode) {
		
		Course course = courseRepository.findByCode(courseCode);
		
		if(Objects.isNull(course)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorItemDTO("code", "Curso não existe no sistema"));
		}
		
        if (course.getCourseStatus() == CourseStatus.INACTIVE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O curso já está inativo");
        }

        course.setCourseStatus(CourseStatus.INACTIVE);
        course.setInactivationDate(LocalDateTime.now());
		
        return ResponseEntity.ok().build();
    }

}
