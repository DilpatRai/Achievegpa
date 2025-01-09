package cs411.services;

import cs411.dao.StudentDAO;
import cs411.models.Student;

import java.sql.Connection;
import java.util.List;

public class StudentService {
    private StudentDAO studentDAO;

    public StudentService(Connection connection) {
        studentDAO = new StudentDAO(connection);
    }

    public void createStudent(Student student) {
        studentDAO.createStudent(student);
    }

    public Student getStudentByEmail(String email) {
        return studentDAO.getStudentByEmail(email);
    }

    public void updateStudent(Student student) {
        studentDAO.updateStudent(student);
    }

    public List<Student> getStudents() {
        return studentDAO.getStudents();
    }

    public boolean deleteStudent(int studentID) {
        return studentDAO.deleteStudent(studentID);
    }
}
