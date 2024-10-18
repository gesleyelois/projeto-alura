package br.com.alura.ProjetoAlura.registration;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RegistrationController {

    @PostMapping("/registration/new")
    public ResponseEntity createCourse(@Valid @RequestBody NewRegistrationDTO newRegistration) {
        // TODO: Implementar a Questão 3 - Criação de Matrículas aqui...
         Course course = courseRepository.findByCode(newRegistration.getCourseCode());
        if (course == null || course.getStatus() != CourseStatus.ACTIVE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Curso inválido ou inativo
        }

        // Verifique se o usuário já está matriculado
        if (registrationRepository.existsByUserIdAndCourseCode(newRegistration.getUserId(), newRegistration.getCourseCode())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Conflito, matrícula já existe
        }

        // Criar nova matrícula
        Registration registration = new Registration();
        registration.setUserId(newRegistration.getUserId());
        registration.setCourse(course);
        registration.setRegistrationDate(LocalDateTime.now());

        registrationRepository.save(registration);

        return ResponseEntity.status(HttpStatus.CREATED).build(); 
    }

    @GetMapping("/registration/report")
    public ResponseEntity<List<RegistrationReportItem>> report() {
        List<RegistrationReportItem> items = new ArrayList<>();

        // TODO: Implementar a Questão 4 - Relatório de Cursos Mais Acessados aqui...

        List<CourseReport> reportData = registrationRepository.findTopCourses(); // Suponha que você tenha esse método no repositório

        for (CourseReport report : reportData) {
            items.add(new RegistrationReportItem(
                report.getCourseName(),
                report.getCourseCode(),
                report.getUserName(),
                report.getUserEmail(),
                report.getRegistrationCount()
            ));
        }

        return ResponseEntity.ok(items);
    }
