package cs411.dao;

import cs411.models.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    private Connection connection;

    public StudentDAO(Connection connection) {
        this.connection = connection;
    }

    public void createStudent(Student student) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO student (name, email, password, major) VALUES (?, ?, ?, ?)", new String[]{"studentID"});
            statement.setString(1, student.getName());
            statement.setString(2, student.getEmail());
            statement.setString(3, student.getPassword());
            statement.setString(4, student.getMajor());
            // Execute and get the auto generated id
            statement.executeUpdate();
            // studentID
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                student.setStudentID(resultSet.getInt(1));
            }
            // Close the statement
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Student getStudentByEmail(String email) {
        Student student = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM student WHERE email = ?");
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                student = new Student(resultSet.getInt("studentID"), resultSet.getString("name"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("major"));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }

    public void updateStudent(Student student) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE student SET name = ?, email = ?, password = ?, major = ? WHERE studentID = ?");
            statement.setString(1, student.getName());
            statement.setString(2, student.getEmail());
            statement.setString(3, student.getPassword());
            statement.setString(4, student.getMajor());
            statement.setInt(5, student.getStudentID());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM student");
            while (resultSet.next()) {
                students.add(new Student(resultSet.getInt("studentID"), resultSet.getString("name"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("major")));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public boolean deleteStudent(int studentID) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM student WHERE studentID = ?");
            statement.setInt(1, studentID);
            statement.executeUpdate();
            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
