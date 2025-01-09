package cs411.models;

public class Admin {
    private int adminID;
    private String name;
    private String email;
    private String password;

    public Admin(int adminID, String name, String email, String password) {
        this.adminID = adminID;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
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
}
