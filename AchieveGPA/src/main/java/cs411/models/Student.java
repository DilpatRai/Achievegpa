package cs411.models;

public class Student {
    private int studentID;
    private String name;
    private String email;
    private String password;
    private String major;

    public Student(int studentID, String name, String email, String password, String major) {
        this.studentID = studentID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.major = major;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
