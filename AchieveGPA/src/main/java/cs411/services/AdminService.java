package cs411.services;

import cs411.dao.AdminDAO;
import cs411.models.Admin;

import java.sql.Connection;

public class AdminService {
    private AdminDAO adminDAO;

    public AdminService(Connection connection) {
        adminDAO = new AdminDAO(connection);
    }

    public void createAdmin(Admin admin) {
        adminDAO.createAdmin(admin);
    }

    public Admin getAdminByEmail(String email) {
        return adminDAO.getAdminByEmail(email);
    }

    public void updateAdmin(Admin admin) {
        adminDAO.updateAdmin(admin);
    }
}
