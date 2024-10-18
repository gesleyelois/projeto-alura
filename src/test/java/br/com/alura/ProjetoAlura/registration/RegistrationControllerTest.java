package br.com.alura.ProjetoAlura.registration;

import br.com.alura.ProjetoAlura.course.Course;
import br.com.alura.ProjetoAlura.course.CourseRepository;
import br.com.alura.ProjetoAlura.course.CourseStatus;
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

import javax.sql.DataSource;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ActiveProfiles("test")

@WebMvcTest(RegistrationController.class)
public class RegistrationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private RegistrationRepository registrationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private DataSource dataSource;


    @Test
    void newRegistration__should_return_not_found_when_course_is_not_found() throws Exception {
        RegistrationDTO newReg = new RegistrationDTO();
        newReg.setCode("javascript");
        newReg.setStudentEmail("student@email.com");

        when(courseRepository.findByCode(newReg.getCode())).thenReturn(null);
        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newReg)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Curso nao encontrado"));
    }

    @Test
    void newRegistration__should_return_not_found_when_student_is_not_found() throws Exception {

        RegistrationDTO newRegistration = new RegistrationDTO();
        newRegistration.setCode("javascript");
        newRegistration.setStudentEmail("student@email.com");

        Course course = new Course();
        course.setCode(newRegistration.getCode());
        when(courseRepository.findByCode(newRegistration.getCode())).thenReturn(course);
        when(userRepository.findByEmail(newRegistration.getStudentEmail())).thenReturn(null);
        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRegistration)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Estudante nao cadastrado na plataforma"));
    }

    @Test
    void newRegistration__should_return_bad_request_when_student_is_already_registrated() throws Exception {
        NewStudentUserDTO user = new NewStudentUserDTO();
        user.setEmail("student@email.com");
        user.setName("student");
        user.setPassword("12345678");

        RegistrationDTO newRegistration = new RegistrationDTO();
        newRegistration.setCode("javascript");
        newRegistration.setStudentEmail("student@email.com");

        Course course = new Course();
        course.setCode(newRegistration.getCode());

        when(courseRepository.findByCode(newRegistration.getCode())).thenReturn(course);
        when(userRepository.findByEmail(newRegistration.getStudentEmail())).thenReturn(user.toModel());
        when(registrationRepository.existsByStudentEmailAndCode(newRegistration.getStudentEmail(), newRegistration.getCode()))
                .thenReturn(true);

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRegistration)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Estudante ja cadastrado neste curso"));
    }

    @Test
    void newRegistration__should_return_bad_request_when_course_is_inactive() throws Exception {
        NewStudentUserDTO user = new NewStudentUserDTO();
        user.setEmail("student@email.com");
        user.setName("student");
        user.setPassword("12345678");

        RegistrationDTO newRegistration = new RegistrationDTO();
        newRegistration.setCode("javascript");
        newRegistration.setStudentEmail("student@email.com");

        Course course = new Course();
        course.setStatus(CourseStatus.INACTIVE);
        course.setCode(newRegistration.getCode());

        when(courseRepository.findByCode(newRegistration.getCode())).thenReturn(course);
        when(userRepository.findByEmail(newRegistration.getStudentEmail())).thenReturn(user.toModel());
        when(registrationRepository.existsByStudentEmailAndCode(newRegistration.getStudentEmail(), newRegistration.getCode()))
                .thenReturn(false);

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRegistration)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Curso desativado"));
    }

    @Test
    void newRegistration__should_return_created_when_registration() throws Exception {
        NewStudentUserDTO user = new NewStudentUserDTO();
        user.setEmail("student@email.com");
        user.setName("student");
        user.setPassword("12345678");

        RegistrationDTO newRegistration = new RegistrationDTO();
        newRegistration.setCode("javascript");
        newRegistration.setStudentEmail("student@email.com");

        Course course = new Course();
        course.setStatus(CourseStatus.ACTIVE);
        course.setCode(newRegistration.getCode());

        when(courseRepository.findByCode(newRegistration.getCode())).thenReturn(course);
        when(userRepository.findByEmail(newRegistration.getStudentEmail())).thenReturn(user.toModel());
        when(registrationRepository.existsByStudentEmailAndCode(newRegistration.getStudentEmail(), newRegistration.getCode()))
                .thenReturn(false);

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRegistration)))
                .andExpect(status().isCreated());
    }

    @Test
    void registrationReport__should_return_course_report() throws Exception {
        Object[] reportRow1 = {"Java", "java", "Steve", "Steve@example.com", 10L};
        Object[] reportRow2 = {"Javascript", "javascript", "Liv", "Liv@example.com", 15L};

        when(registrationRepository.findCoursesWithRegistrationCount()).thenReturn(Arrays.asList(reportRow1, reportRow2));

        mockMvc.perform(get("/registration/report")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseName").value("Java"))
                .andExpect( jsonPath("$[0].courseCode").value("java"))
                .andExpect(jsonPath("$[0].instructorName").value("Steve"))
                .andExpect( jsonPath("$[0].instructorEmail").value("Steve@example.com"))
                .andExpect(jsonPath("$[0].totalRegistrations").value(10))
                .andExpect(jsonPath("$[1].courseName").value("Javascript"))
                .andExpect(jsonPath("$[1].courseCode").value("javascript"))
                .andExpect(jsonPath("$[1].instructorName").value("Liv"))
                .andExpect(jsonPath("$[1].instructorEmail").value("Liv@example.com"))
                .andExpect(jsonPath("$[1].totalRegistrations").value(15));
    }
}



