package cs411.models;

import java.sql.Date;

public class GPA {
    private int gpaID;
    private int studentID;
    private double gpaValue;
    private Date calculationDate;

    public GPA(int gpaID, int studentID, double gpaValue, Date calculationDate) {
        this.gpaID = gpaID;
        this.studentID = studentID;
        this.gpaValue = gpaValue;
        this.calculationDate = calculationDate;
    }

    public int getGpaID() {
        return gpaID;
    }

    public void setGpaID(int gpaID) {
        this.gpaID = gpaID;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public double getGpaValue() {
        return gpaValue;
    }

    public void setGpaValue(double gpaValue) {
        this.gpaValue = gpaValue;
    }

    public Date getCalculationDate() {
        return calculationDate;
    }

    public void setCalculationDate(Date calculationDate) {
        this.calculationDate = calculationDate;
    }
}
