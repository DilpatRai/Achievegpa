package cs411.services;

import cs411.dao.EnrollmentDAO;
import cs411.models.Enrollment;

import java.sql.Connection;
import java.util.List;

public class EnrollmentService {
    private EnrollmentDAO enrollmentDAO;

    public EnrollmentService(Connection connection) {
        enrollmentDAO = new EnrollmentDAO(connection);
    }

    public void createEnrollment(Enrollment enrollment) {
        enrollmentDAO.createEnrollment(enrollment);
    }

    public List<Enrollment> getEnrollments() {
        return enrollmentDAO.getEnrollments();
    }

    public void updateEnrollment(Enrollment enrollment) {
        enrollmentDAO.updateEnrollment(enrollment);
    }

    public void deleteEnrollment(Enrollment enrollment) {
        enrollmentDAO.deleteEnrollment(enrollment);
    }
}
