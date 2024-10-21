package br.com.alura.ProjetoAlura.registration;

public interface RegistrationReportItemProjection {
    String getCourseName();
    String getCourseCode();
    String getInstructorName();
    String getInstructorEmail();
    Long getTotalRegistrations();
}
