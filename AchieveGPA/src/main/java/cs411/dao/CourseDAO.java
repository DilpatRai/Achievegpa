package cs411.dao;

import cs411.models.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    private Connection connection;

    public CourseDAO(Connection connection) {
        this.connection = connection;
    }

    public void createCourse(Course course) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO course (courseName, courseCode, credits) VALUES (?, ?, ?)", new String[]{"courseID"});
            statement.setString(1, course.getCourseName());
            statement.setString(2, course.getCourseCode());
            statement.setInt(3, course.getCredits());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                course.setCourseID(resultSet.getInt(1));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Course getCourseByCode(String courseCode) {
        Course course = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM course WHERE courseCode = ?");
            statement.setString(1, courseCode);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                course = new Course(resultSet.getInt("courseID"), resultSet.getString("courseName"), resultSet.getString("courseCode"), resultSet.getInt("credits"));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return course;
    }

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM course");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                courses.add(new Course(resultSet.getInt("courseID"), resultSet.getString("courseName"), resultSet.getString("courseCode"), resultSet.getInt("credits")));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    public void deleteCourse(Course course) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM course WHERE courseID = ?");
            statement.setInt(1, course.getCourseID());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCourse(Course course) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE course SET courseName = ?, courseCode = ?, credits = ? WHERE courseID = ?");
            statement.setString(1, course.getCourseName());
            statement.setString(2, course.getCourseCode());
            statement.setInt(3, course.getCredits());
            statement.setInt(4, course.getCourseID());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
