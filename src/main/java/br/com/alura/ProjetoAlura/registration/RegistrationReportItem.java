package br.com.alura.ProjetoAlura.registration;

public class RegistrationReportItem {

    private final String courseName;
    private final String courseCode;
    private final String instructorName;
    private final String instructorEmail;
    private final Long totalRegistrations;

    public RegistrationReportItem(RegistrationReportItemProjection projection) {
        this.courseName = projection.getCourseName();
        this.courseCode = projection.getCourseCode();
        this.instructorName = projection.getInstructorName();
        this.instructorEmail = projection.getInstructorEmail();
        this.totalRegistrations = projection.getTotalRegistrations();
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public Long getTotalRegistrations() {
        return totalRegistrations;
    }
}
