package br.com.alura.ProjetoAlura.registration;

import br.com.alura.ProjetoAlura.ProjetoAluraApplication;
import br.com.alura.ProjetoAlura.course.*;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegistrationController.class)
@ContextConfiguration(classes = {ProjetoAluraApplication.class})
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationRepository registrationRepository;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void newRegistration__should_return_bad_request_when_coursecode_is_blank() throws Exception {
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setStudentEmail("email@email");
        newRegistrationDTO.setCourseCode("");

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRegistrationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("courseCode"))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }


    @Test
    void createRegistration__should_return_bad_request_when_course_not_found() throws Exception {
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setStudentEmail("email@email.com");
        newRegistrationDTO.setCourseCode("java");

        User student = new User();
        student.setRole(Role.STUDENT);

        when(userRepository.findByEmail(newRegistrationDTO.getStudentEmail())).thenReturn(Optional.of(student));
        when(courseRepository.findByCode(newRegistrationDTO.getCourseCode())).thenReturn(Optional.empty());

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRegistrationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("code"))
                .andExpect(jsonPath("$.message").value("Curso não encontrado com o código fornecido"));
    }

    @Test
    void createRegistration__should_return_bad_request_when_course_is_not_active() throws Exception {
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setStudentEmail("email@email.com");
        newRegistrationDTO.setCourseCode("java");

        User student = new User();
        student.setRole(Role.STUDENT);

        Course course = new Course();
        course.setStatus(Status.INACTIVE);

        when(userRepository.findByEmail(newRegistrationDTO.getStudentEmail())).thenReturn(Optional.of(student));
        when(courseRepository.findByCode(newRegistrationDTO.getCourseCode())).thenReturn(Optional.of(course));

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRegistrationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("code"))
                .andExpect(jsonPath("$.message").value("Curso não está recebendo matrículas no momento"));
    }

    @Test
    void newRegistration__should_return_bad_request_when_email_is_blank() throws Exception {
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setStudentEmail("");
        newRegistrationDTO.setCourseCode("java");

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRegistrationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("studentEmail"))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    void newRegistration__should_return_bad_request_when_email_is_invalid() throws Exception {
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setStudentEmail("email");
        newRegistrationDTO.setCourseCode("java");

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRegistrationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("studentEmail"))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    void newRegistration__should_return_bad_request_when_student_email_not_found() throws Exception {
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setStudentEmail("email@email");
        newRegistrationDTO.setCourseCode("java");

        when(userRepository.findByEmail(newRegistrationDTO.getStudentEmail())).thenReturn(Optional.empty());

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRegistrationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("email"))
                .andExpect(jsonPath("$.message").value("Estudante não encontrado com o email fornecido"));
    }

    @Test
    void newRegistration__should_return_bad_request_when_student_email_is_not_from_student() throws Exception {
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setStudentEmail("email@email");
        newRegistrationDTO.setCourseCode("java");

        User user = new User();
        user.setEmail("instructor@email.com");
        user.setRole(Role.INSTRUCTOR);

        when(userRepository.findByEmail(newRegistrationDTO.getStudentEmail())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRegistrationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("email"))
                .andExpect(jsonPath("$.message").value("O email fornecido não pertence a um aluno"));
    }

    @Test
    void createRegistration__should_return_bad_request_when_student_already_enrolled() throws Exception {
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setStudentEmail("email@email.com");
        newRegistrationDTO.setCourseCode("java");

        when(registrationRepository.existsByCourseCodeAndStudentEmail(newRegistrationDTO.getCourseCode(), newRegistrationDTO.getStudentEmail()))
                .thenReturn(true);

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRegistrationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("email"))
                .andExpect(jsonPath("$.message").value("Estudante já matriculado no curso"));
    }

    @Test
    void createRegistration__should_return_created_when_registration_successful() throws Exception {
        NewRegistrationDTO newRegistrationDTO = new NewRegistrationDTO();
        newRegistrationDTO.setStudentEmail("email@email.com");
        newRegistrationDTO.setCourseCode("java");

        User student = new User();
        student.setRole(Role.STUDENT);

        Course course = new Course();
        course.setStatus(Status.ACTIVE);

        when(userRepository.findByEmail(newRegistrationDTO.getStudentEmail())).thenReturn(Optional.of(student));
        when(courseRepository.findByCode(newRegistrationDTO.getCourseCode())).thenReturn(Optional.of(course));

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRegistrationDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Aluno inscrito no curso com sucesso!"));
    }

    @Test
    void report__should_return_registration_report_successfully() throws Exception {
        List<Object[]> mockResults = new ArrayList<>();
        mockResults.add(new Object[]{"Java", "JAVA101", "Charles", "charles@email.com", 10L});

        when(registrationRepository.findCoursesWithMostRegistrations()).thenReturn(mockResults);

        mockMvc.perform(get("/registration/report")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseName").value("Java"))
                .andExpect(jsonPath("$[0].courseCode").value("JAVA101"))
                .andExpect(jsonPath("$[0].instructorName").value("Charles"))
                .andExpect(jsonPath("$[0].instructorEmail").value("charles@email.com"))
                .andExpect(jsonPath("$[0].totalRegistrations").value(10));
    }


}