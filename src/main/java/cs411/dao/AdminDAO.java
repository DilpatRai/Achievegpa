package cs411.dao;

import cs411.models.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {
    private Connection connection;

    public AdminDAO(Connection connection) {
        this.connection = connection;
    }

    public void createAdmin(Admin admin) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO admin (name, email, password) VALUES (?, ?, ?)");
            statement.setString(1, admin.getName());
            statement.setString(2, admin.getEmail());
            statement.setString(3, admin.getPassword());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Admin getAdminByEmail(String email) {
        Admin admin = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM admin WHERE email = ?");
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                admin = new Admin(resultSet.getInt("adminID"), resultSet.getString("name"), resultSet.getString("email"), resultSet.getString("password"));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
    }

    public void updateAdmin(Admin admin) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE admin SET name = ?, email = ?, password = ? WHERE adminID = ?");
            statement.setString(1, admin.getName());
            statement.setString(2, admin.getEmail());
            statement.setString(3, admin.getPassword());
            statement.setInt(4, admin.getAdminID());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
