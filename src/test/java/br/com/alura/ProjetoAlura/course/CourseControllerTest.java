package br.com.alura.ProjetoAlura.course;

import br.com.alura.ProjetoAlura.ProjetoAluraApplication;
import br.com.alura.ProjetoAlura.user.Role;
import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
@ContextConfiguration(classes = {ProjetoAluraApplication.class})
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void newCourse__should_return_bad_request_when_name_is_blank() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setName("");
        newCourseDTO.setCode("java");
        newCourseDTO.setDescription("everything you need to know!");
        newCourseDTO.setInstructorEmail("nonexistent@email.com");

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("name"))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    void newCourse__should_return_bad_request_when_code_is_blank() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setName("Cecília");
        newCourseDTO.setCode("");
        newCourseDTO.setDescription("everything you need to know!");
        newCourseDTO.setInstructorEmail("nonexistent@email.com");

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("code"))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    void newCourse__should_return_bad_request_when_code_is_invalid() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setName("Java 101");
        newCourseDTO.setCode("jav2");
        newCourseDTO.setDescription("everything you need to know!");
        newCourseDTO.setInstructorEmail("nonexistent@email.com");

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("code"))
                .andExpect(jsonPath("$[0].message").value("O código deve conter apenas letras e hífen, sem espaços ou números"));
    }

    @Test
    void newCourse__should_return_bad_request_when_code_already_exists() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setName("Java 101");
        newCourseDTO.setCode("java");
        newCourseDTO.setDescription("everything you need to know!");
        newCourseDTO.setInstructorEmail("nonexistent@email.com");

        when(courseRepository.existsByCode(newCourseDTO.getCode())).thenReturn(true);

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("code"))
                .andExpect(jsonPath("$.message").value("Código de curso já cadastrado no sistema"));
    }

    @Test
    void newCourse__should_return_bad_request_when_email_is_blank() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setName("Java 101");
        newCourseDTO.setCode("java");
        newCourseDTO.setDescription("everything you need to know!");
        newCourseDTO.setInstructorEmail("");

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("instructorEmail"))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    void newCourse__should_return_bad_request_when_email_is_invalid() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setName("Java 101");
        newCourseDTO.setCode("java");
        newCourseDTO.setDescription("everything you need to know!");
        newCourseDTO.setInstructorEmail("nonexistent");

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("instructorEmail"))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    void newCourse__should_return_bad_request_when_instructor_email_not_found() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setName("Java 101");
        newCourseDTO.setCode("java");
        newCourseDTO.setDescription("everything you need to know!");
        newCourseDTO.setInstructorEmail("nonexistent@email.com");

        when(userRepository.findByEmail(newCourseDTO.getInstructorEmail())).thenReturn(Optional.empty());

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("email"))
                .andExpect(jsonPath("$.message").value("Instrutor com o email informado não encontrado no sistema"));
    }

    @Test
    void newCourse__should_return_bad_request_when_instructor_email_is_not_from_instructor() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setName("Java 101");
        newCourseDTO.setCode("java");
        newCourseDTO.setDescription("everything you need to know!");
        newCourseDTO.setInstructorEmail("nonexistent@email.com");

        User user = new User();
        user.setEmail("student@email.com");
        user.setRole(Role.STUDENT);

        when(userRepository.findByEmail(newCourseDTO.getInstructorEmail())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("email"))
                .andExpect(jsonPath("$.message").value("Email fornecido não pertence a um instrutor"));
    }

    @Test
    void newCourse__should_return_created_when_course_request_is_valid() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setName("Java 101");
        newCourseDTO.setCode("java");
        newCourseDTO.setDescription("everything you need to know!");
        newCourseDTO.setInstructorEmail("nonexistent@email.com");

        User user = new User();
        user.setEmail("instructor@email.com");
        user.setRole(Role.INSTRUCTOR);

        when(courseRepository.existsByCode(newCourseDTO.getCode())).thenReturn(false);
        when(userRepository.findByEmail(newCourseDTO.getInstructorEmail())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Curso criado com sucesso!"));
    }

    @Test
    void inactivateCourse__should_return_not_found_when_course_does_not_exist() throws Exception {
        String courseCode = "java101";

        when(courseRepository.findByCode(courseCode)).thenReturn(Optional.empty());

        mockMvc.perform(post("/course/" + courseCode + "/inactive"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.field").value("code"))
                .andExpect(jsonPath("$.message").value("Curso não encontrado"));
    }

    @Test
    void inactivateCourse__should_return_bad_request_when_course_is_already_inactive() throws Exception {
        String courseCode = "java101";
        Course course = new Course();
        course.setCode(courseCode);
        course.setStatus(Status.INACTIVE);
        when(courseRepository.findByCode(courseCode)).thenReturn(Optional.of(course));

        mockMvc.perform(post("/course/" + courseCode + "/inactive"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("status"))
                .andExpect(jsonPath("$.message").value("O curso já está inativo"));
    }

    @Test
    void inactivateCourse__should_return_ok_when_course_is_inactivated_successfully() throws Exception {
        String courseCode = "java101";
        Course course = new Course();
        course.setCode(courseCode);
        course.setStatus(Status.ACTIVE);

        when(courseRepository.findByCode(courseCode)).thenReturn(Optional.of(course));

        mockMvc.perform(post("/course/" + courseCode + "/inactive"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Curso inativado com sucesso!"));
    }

    @Test
    void listAllCourses__should_list_all_courses() throws Exception {
        Course course1 = new Course("java 101", "java", "instructor1@test.com", Status.ACTIVE, "learn the basics");
        Course course2 = new Course("php 101", "php", "instructor2@test.com", Status.ACTIVE, "learn the basics");

        when(courseRepository.findAll()).thenReturn(Arrays.asList(course1, course2));

        mockMvc.perform(get("/course/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("java 101"))
                .andExpect(jsonPath("$[1].name").value("php 101"));
    }


}