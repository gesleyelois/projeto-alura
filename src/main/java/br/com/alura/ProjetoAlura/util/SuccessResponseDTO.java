package br.com.alura.ProjetoAlura.util;

public class SuccessResponseDTO {

        private String message;
        private String courseCode;

        public SuccessResponseDTO() {}

        public SuccessResponseDTO(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCourseCode() {
            return courseCode;
        }

        public void setCourseCode(String courseCode) {
            this.courseCode = courseCode;
        }

        @Override
        public String toString() {
            return "message=" + message ;
        }
}
