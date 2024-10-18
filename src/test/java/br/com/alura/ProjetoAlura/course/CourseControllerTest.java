package br.com.alura.ProjetoAlura.course;

import br.com.alura.ProjetoAlura.user.NewInstructorUserDTO;
import br.com.alura.ProjetoAlura.user.NewStudentUserDTO;
import br.com.alura.ProjetoAlura.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(CourseController.class)
public class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseRepository courseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;


    @Test
    void newCourse__should_return_bad_request_when_user_is_not_found() throws Exception {
        NewCourseDTO newCourse = new NewCourseDTO();
        newCourse.setName("javascript");
        newCourse.setCode("javascript");
        newCourse.setDescription("Javascript course");
        newCourse.setInstructorEmail("instructor@alura.com.br");

        when(userRepository.findByEmail(newCourse.getInstructorEmail())).thenReturn(null);
        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourse)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Instrutor nao cadastrado na base de dados"));
    }

    @Test
    void newCourse__should_return_bad_request_when_email_is_not_valid() throws Exception {

        NewStudentUserDTO newStudentUserDTO = new NewStudentUserDTO();
        newStudentUserDTO.setEmail("student@alura.com.br");
        newStudentUserDTO.setName("Charles");
        newStudentUserDTO.setPassword("mudar123");

        NewCourseDTO newCourse = new NewCourseDTO();
        newCourse.setName("java");
        newCourse.setCode("javascript");
        newCourse.setDescription("Javascript course");
        newCourse.setInstructorEmail("student@alura.com.br");

        when(userRepository.findByEmail(newCourse.getInstructorEmail())).thenReturn(newStudentUserDTO.toModel());
        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourse)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Estudantes não podem criar cursos"));
    }

    @Test
    void newCourse__should_return_bad_request_when_course_already_exists() throws Exception {
        NewCourseDTO newCourse = new NewCourseDTO();
        newCourse.setName("java");
        newCourse.setCode("javascript");
        newCourse.setDescription("Javascript course");
        newCourse.setInstructorEmail("instructor@alura.com.br");

        NewInstructorUserDTO newInstr = new NewInstructorUserDTO();
        newInstr.setEmail("instructor@alura.com.br");
        newInstr.setName("Charles");
        newInstr.setPassword("mudar123");

        when(userRepository.findByEmail(newCourse.getInstructorEmail())).thenReturn(newInstr.toModel());
        when(courseRepository.existsByCode(newCourse.getCode())).thenReturn(true);
        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourse)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("O curso já está cadastrado"));
    }

    @Test
    void newCourse__should_return_bad_request_when_course_already_exists_and_invalid_email() throws Exception {
        NewStudentUserDTO newStudentUserDTO = new NewStudentUserDTO();
        newStudentUserDTO.setEmail("student@alura.com.br");
        newStudentUserDTO.setName("Charles");
        newStudentUserDTO.setPassword("mudar123");

        NewCourseDTO newCourse = new NewCourseDTO();
        newCourse.setName("java");
        newCourse.setCode("javascript");
        newCourse.setDescription("Javascript course");
        newCourse.setInstructorEmail("student@alura.com.br");

        when(courseRepository.existsByCode(newCourse.getCode())).thenReturn(true);
        when(userRepository.findByEmail(newCourse.getInstructorEmail())).thenReturn(newStudentUserDTO.toModel());

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourse)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Estudantes não podem criar cursos e O curso já está cadastrado"));
    }

    @Test
    void newCourse__should_return_created_when_create_new_course() throws Exception {
        NewInstructorUserDTO newInstr= new NewInstructorUserDTO();
        newInstr.setEmail("instructor@alura.com.br");
        newInstr.setName("Charles");
        newInstr.setPassword("mudar12345");

        NewCourseDTO newCourse = new NewCourseDTO();
        newCourse.setName("java");
        newCourse.setCode("javascript");
        newCourse.setDescription("Javascript course");
        newCourse.setInstructorEmail("instructor@alura.com.br");

        when(userRepository.findByEmail(newCourse.getInstructorEmail())).thenReturn(newInstr.toModel());

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourse)))
                .andExpect(status().isCreated());
    }

    @Test
    void inactivateCourse__should_return_not_found_when_course_is_not_found() throws Exception {
        NewCourseDTO newCourse = new NewCourseDTO();
        newCourse.setName("javascript");
        newCourse.setCode("javascript");
        newCourse.setDescription("Javascript course");
        newCourse.setInstructorEmail("instructor@alura.com.br");

        when(courseRepository.findByCode(newCourse.getCode())).thenReturn(null);
        mockMvc.perform(post("/course/javascript/inactive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourse)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Curso nao encontrado"));
    }

    @Test
    void inactivateCourse__should_return_bad_request_when_course_already_inactivated() throws Exception {
        NewCourseDTO newCourse = new NewCourseDTO();
        newCourse.setName("javascript");
        newCourse.setCode("javascript");
        newCourse.setDescription("Javascript course");
        newCourse.setInstructorEmail("instructor@alura.com.br");

        Course inactiveCourse = new Course();
        inactiveCourse.setStatus(CourseStatus.INACTIVE);
        inactiveCourse.setCode(newCourse.getCode());
        when(courseRepository.findByCode(newCourse.getCode())).thenReturn(inactiveCourse);
        mockMvc.perform(post("/course/javascript/inactive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourse)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Curso ja desativado"));
    }

    @Test
    void inactivateCourse__should_return_created_when_course_is_inactivated() throws Exception {
        NewCourseDTO newCourse = new NewCourseDTO();
        newCourse.setName("javascript");
        newCourse.setCode("javascript");
        newCourse.setDescription("Javascript course");
        newCourse.setInstructorEmail("instructor@alura.com.br");

        Course course = new Course();
        course.setCode(newCourse.getCode());
        when(courseRepository.findByCode(newCourse.getCode())).thenReturn(course);
        mockMvc.perform(post("/course/javascript/inactive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourse)))
                .andExpect(status().isCreated());
    }
}
