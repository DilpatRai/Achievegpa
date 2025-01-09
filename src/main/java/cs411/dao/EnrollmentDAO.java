package cs411.dao;

import cs411.models.Enrollment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {
    private Connection connection;

    public EnrollmentDAO(Connection connection) {
        this.connection = connection;
    }

    public void createEnrollment(Enrollment enrollment) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO enrollment (studentID, courseID, grade) VALUES (?, ?, ?)", new String[]{"enrollmentID"});
            statement.setInt(1, enrollment.getStudentID());
            statement.setInt(2, enrollment.getCourseID());
            statement.setString(3, enrollment.getGrade());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                enrollment.setEnrollmentID(resultSet.getInt(1));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Enrollment> getEnrollments() {
        List<Enrollment> enrollments = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM enrollment");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                enrollments.add(new Enrollment(resultSet.getInt("enrollmentID"), resultSet.getInt("studentID"), resultSet.getInt("courseID"), resultSet.getString("grade")));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollments;
    }

    public void updateEnrollment(Enrollment enrollment) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE enrollment SET grade = ? WHERE studentID = ? AND courseID = ?");
            statement.setString(1, enrollment.getGrade());
            statement.setInt(2, enrollment.getStudentID());
            statement.setInt(3, enrollment.getCourseID());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEnrollment(Enrollment enrollment) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM enrollment WHERE studentID = ? AND courseID = ?");
            statement.setInt(1, enrollment.getStudentID());
            statement.setInt(2, enrollment.getCourseID());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
