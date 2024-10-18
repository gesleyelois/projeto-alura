package br.com.alura.ProjetoAlura.course;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.ProjetoAlura.user.NewInstructorUserDTO;
import br.com.alura.ProjetoAlura.user.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class CourseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CourseRepository courseRepository; 
    
    @Autowired
    private UserRepository userRepository; 
    
    @Test
    void instructor_should_be_able_to_create_course() throws Exception {
        NewInstructorUserDTO newInstructorUserDTO = new NewInstructorUserDTO();
        newInstructorUserDTO.setEmail("instructor@alura.com");
        newInstructorUserDTO.setName("instructor");
        newInstructorUserDTO.setPassword("password");

        mockMvc.perform(post("/user/newInstructor")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newInstructorUserDTO)));

        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setCode("test");
        newCourseDTO.setName("test course");
        newCourseDTO.setInstructorEmail("instructor@alura.com");
        newCourseDTO.setDescription("");

        mockMvc.perform(post("/course/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isCreated());
    }
    
    @Test
    void course_code_must_be_unique() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setCode("test");
        newCourseDTO.setName("test course");
        newCourseDTO.setInstructorEmail("instructor@alura.com");
        newCourseDTO.setDescription("");

        mockMvc.perform(post("/course/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void course_code_must_have_proper_chars() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setCode("test!");
        newCourseDTO.setName("test course");
        newCourseDTO.setInstructorEmail("instructor@alura.com");
        newCourseDTO.setDescription("");

        mockMvc.perform(post("/course/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void course_should_be_inactivated_when_requested() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setCode("test-test");
        newCourseDTO.setName("test course");
        newCourseDTO.setInstructorEmail("instructor@alura.com");
        newCourseDTO.setDescription("");

        mockMvc.perform(post("/course/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/course/test-test/inactive")).andExpect(status().isOk());

        Course updatedCourse = courseRepository.findByCode("test-test");

        assertThat(updatedCourse.getCourseStatus()).isEqualTo(CourseStatus.INACTIVE);

        assertThat(updatedCourse.getInactivationDate()).isNotNull();    
    }
}
