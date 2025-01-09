package cs411.utils;

import java.sql.*;



public class DatabaseManager {
    static DatabaseManager instance = new DatabaseManager();
    public static DatabaseManager getInstance() {
        return instance;
    }

    private Connection connection;
    public Connection getConnection() {
        return connection;
    }

    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            createTablesIfNeeded();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static final String DB_URL = "jdbc:mysql://localhost:3306/achievegpa";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "root";

    private void createTablesIfNeeded() throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS admin (" +
                "adminID INT(10) PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(50)," +
                "email VARCHAR(70) UNIQUE," +
                "password VARCHAR(100)" +
                ")");

        statement.execute("CREATE TABLE IF NOT EXISTS student (" +
                "studentID INT(10) PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(50)," +
                "email VARCHAR(70) UNIQUE," +
                "password VARCHAR(100)," +
                "major VARCHAR(50)" +
                ")");

        statement.execute("CREATE TABLE IF NOT EXISTS course (" +
                "courseID INT(10) PRIMARY KEY AUTO_INCREMENT," +
                "courseName VARCHAR(100)," +
                "courseCode VARCHAR(10) UNIQUE," +
                "credits INT(2)" +
                ")");

        statement.execute("CREATE TABLE IF NOT EXISTS enrollment (" +
                "enrollmentID INT(10) PRIMARY KEY AUTO_INCREMENT," +
                "studentID INT(10)," +
                "courseID INT(10)," +
                "grade VARCHAR(10)," +
                "FOREIGN KEY (studentID) REFERENCES student(studentID)," +
                "FOREIGN KEY (courseID) REFERENCES course(courseID)" +
                ")");

        statement.execute("CREATE TABLE IF NOT EXISTS gpa (" +
                "gpaID INT(10) PRIMARY KEY AUTO_INCREMENT," +
                "studentID INT(10)," +
                "gpaValue DECIMAL(4, 2)," +
                "calculationDate DATE," +
                "FOREIGN KEY (studentID) REFERENCES student(studentID)" +
                ")");

        statement.close();

    }

}