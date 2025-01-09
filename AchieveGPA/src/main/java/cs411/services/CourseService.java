package cs411.services;

import cs411.dao.CourseDAO;
import cs411.models.Course;

import java.sql.Connection;
import java.util.List;

public class CourseService {
    private CourseDAO courseDAO;

    public CourseService(Connection connection) {
        courseDAO = new CourseDAO(connection);
    }

    public void createCourse(Course course) {
        courseDAO.createCourse(course);
    }
    
    public Course getCourseByCode(String courseCode) {
        return courseDAO.getCourseByCode(courseCode);
    }

    public List<Course> getAllCourses() {
        return courseDAO.getAllCourses();
    }

    public List<Course> getCourses() {
        return courseDAO.getAllCourses();
    }

    public void deleteCourse(Course course) {
        courseDAO.deleteCourse(course);
    }

    public void updateCourse(Course course) {
        courseDAO.updateCourse(course);
    }
}
