package cs411.services;

import cs411.dao.GPADAO;
import cs411.models.GPA;

import java.sql.Connection;
import java.util.List;

public class GPAService {
    private GPADAO gpaDAO;

    public GPAService(Connection connection) {
        gpaDAO = new GPADAO(connection);
    }

    public void createGPA(GPA gpa) {
        gpaDAO.createGPA(gpa);
    }

    public List<GPA> getGPAs() {
        return gpaDAO.getGPAs();
    }
}
