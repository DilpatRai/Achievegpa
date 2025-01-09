package cs411.dao;

import cs411.models.GPA;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GPADAO {
    private Connection connection;

    public GPADAO(Connection connection) {
        this.connection = connection;
    }

    public void createGPA(GPA gpa) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO gpa (studentID, gpaValue, calculationDate) VALUES (?, ?, ?)", new String[]{"gpaID"});
            statement.setInt(1, gpa.getStudentID());
            statement.setDouble(2, gpa.getGpaValue());
            statement.setDate(3, gpa.getCalculationDate());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                gpa.setGpaID(resultSet.getInt(1));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<GPA> getGPAs() {
        List<GPA> gpas = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM gpa");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                gpas.add(new GPA(resultSet.getInt("gpaID"), resultSet.getInt("studentID"), resultSet.getDouble("gpaValue"), resultSet.getDate("calculationDate")));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gpas;
    }
}
