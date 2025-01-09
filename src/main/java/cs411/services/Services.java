package cs411.services;

import cs411.models.*;
import cs411.utils.DatabaseManager;
import cs411.utils.EncryptionDecryption;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Services {

    private static Services instance = new Services();

    private GPAService gpaService;
    private EnrollmentService enrollmentService;
    private AdminService adminService;
    private StudentService studentService;
    private CourseService courseService;

    private boolean isLoggedIn = false;
    private String email;
    private boolean isAdmin = false;

    private Services() {
        Connection connection = DatabaseManager.getInstance().getConnection();
        gpaService = new GPAService(connection);
        enrollmentService = new EnrollmentService(connection);
        adminService = new AdminService(connection);
        studentService = new StudentService(connection);
        courseService = new CourseService(connection);
    }

    public static Services getInstance() {
        return instance;
    }

    public Admin getAdmin() {
        return adminService.getAdminByEmail(email);
    }

    public Student getStudent() {
        return studentService.getStudentByEmail(email);
    }

    public void onLoggedIn(String email, boolean isAdmin) {
        this.email = email;
        this.isAdmin = isAdmin;
        isLoggedIn = true;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public Object getUser() {
        if (isAdmin) {
            return adminService.getAdminByEmail(email);
        } else {
            return studentService.getStudentByEmail(email);
        }
    }

    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    public Student signUp(String name, String email, String password, String major) {
        Student student = new Student(-1, name, email, password, major);
        studentService.createStudent(student);
        assert student.getStudentID() != -1;
        return student;
    }

    public Student getStudentByEmail(String email) {
        return studentService.getStudentByEmail(email);
    }

    public Admin loginAsAdmin(String email, String password) throws Exception {
        Admin admin = adminService.getAdminByEmail(email);
        EncryptionDecryption encryptionDecryption = new EncryptionDecryption();
        if (admin != null && Objects.equals(encryptionDecryption.decrypt(admin.getPassword()), password)) {
            onLoggedIn(email, true);
            return admin;
        }
        return null;
    }

    public Student loginAsStudent(String email, String password) throws Exception {
        Student student = studentService.getStudentByEmail(email);
        if(student == null) {
            return null;
        }
        EncryptionDecryption encryptionDecryption = new EncryptionDecryption();
        if (student != null && Objects.equals(encryptionDecryption.decrypt(student.getPassword()), password)) {
            onLoggedIn(email, false);
            return student;
        }
        return null;
    }

    public void updateAdmin(Admin admin) {
        adminService.updateAdmin(admin);
    }

    public void updateStudent(Student student) {
        studentService.updateStudent(student);
    }

    public void logout() {
        isLoggedIn = false;
        email = null;
        isAdmin = false;
    }

    public List<Student> getStudents() {
        return studentService.getStudents();
    }

    public boolean deleteStudent(int studentID) {
        return studentService.deleteStudent(studentID);
    }

    public Admin getAdminByEmail(String email) {
        return adminService.getAdminByEmail(email);
    }

    public List<Enrollment> getEnrollments() {
        return enrollmentService.getEnrollments();
    }

    public List<Course> getCourses() {
        return courseService.getCourses();
    }

    public void updateEnrollment(Enrollment enrollment) {
        enrollmentService.updateEnrollment(enrollment);
    }

    public void createEnrollment(Enrollment enrollment) {
        enrollmentService.createEnrollment(enrollment);
    }

    public void createCourse(Course course) {
        courseService.createCourse(course);
    }

    public void deleteCourse(Course course) {
        courseService.deleteCourse(course);
    }

    public void updateCourse(Course course) {
        courseService.updateCourse(course);
    }

    public void deleteEnrollment(Enrollment enrollment) {
        enrollmentService.deleteEnrollment(enrollment);
    }

    public void createGPA(GPA gpa) {
        gpaService.createGPA(gpa);
    }

    public List<GPA> getGPAs() {
        return gpaService.getGPAs();
    }
}
