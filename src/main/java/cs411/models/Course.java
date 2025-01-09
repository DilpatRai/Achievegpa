package cs411.models;

public class Course {
    private int courseID;
    private String courseName;
    private String courseCode;
    private int credits;

    public Course(int courseID, String courseName, String courseCode, int credits) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.credits = credits;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
}
