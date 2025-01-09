package cs411.ui;

import cs411.models.Student;
import cs411.services.Services;
import cs411.utils.Config;
import cs411.utils.EncryptionDecryption;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class SignupForm extends JFrame {
    private JTextField nameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JComboBox<String> roleComboBox;
    private JButton signupButton;

    public SignupForm() {
        setTitle("Sign Up");
        setSize(400, 350);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Config.PRIMARY_COLOR);
        Border padding = BorderFactory.createEmptyBorder(20, 20, 20, 20);
        contentPanel.setBorder(padding);
        setContentPane(contentPanel);
        setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        ((JPanel) getContentPane()).setAlignmentX(Component.CENTER_ALIGNMENT);
        ((JPanel) getContentPane()).setAlignmentY(Component.CENTER_ALIGNMENT);


        ImageIcon icon = new ImageIcon("src/main/resources/logo.png");

        icon = new ImageIcon(icon.getImage().getScaledInstance(50, 66, Image.SCALE_SMOOTH));
        JLabel logo = new JLabel("AchieveGPA", icon, JLabel.CENTER);
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Arial", Font.BOLD, 24));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        Container logoContainer = new Container();
        logoContainer.setLayout(new BoxLayout(logoContainer, BoxLayout.X_AXIS));
        logoContainer.add(logo);
        add(logoContainer);
        add(Box.createRigidArea(new Dimension(0, 30)));


        JLabel usernameLabel = new JLabel("Name: ");
        usernameLabel.setForeground(Color.WHITE);
        nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        Container usernameContainer = new Container();
        usernameContainer.setLayout(new BoxLayout(usernameContainer, BoxLayout.X_AXIS));
        usernameContainer.add(usernameLabel);
        usernameContainer.add(Box.createRigidArea(new Dimension(10, 0)));
        usernameContainer.add(nameField);

        JLabel emailLabel = new JLabel("Email: ");
        emailLabel.setForeground(Color.WHITE);
        emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        Container emailContainer = new Container();
        emailContainer.setLayout(new BoxLayout(emailContainer, BoxLayout.X_AXIS));
        emailContainer.add(emailLabel);
        emailContainer.add(Box.createRigidArea(new Dimension(10, 0)));
        emailContainer.add(emailField);

        JLabel passwordLabel = new JLabel("Password: ");
        passwordLabel.setForeground(Color.WHITE);
        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        Container passwordContainer = new Container();
        passwordContainer.setLayout(new BoxLayout(passwordContainer, BoxLayout.X_AXIS));
        passwordContainer.add(passwordLabel);
        passwordContainer.add(Box.createRigidArea(new Dimension(10, 0)));
        passwordContainer.add(passwordField);

        signupButton = new JButton("Sign Up");
        signupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupButton.setBackground(Color.WHITE);
        signupButton.setForeground(Config.PRIMARY_COLOR);
        Container signupButtonContainer = new Container();
        signupButtonContainer.setLayout(new BoxLayout(signupButtonContainer, BoxLayout.X_AXIS));
        signupButtonContainer.add(signupButton);

        JLabel signInLabel = new JLabel("Already have an account? ");
        signInLabel.setForeground(Color.WHITE);
        JLabel signInLink = new JLabel("Login");
        signInLink.setForeground(Color.WHITE);
        signInLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signInLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {


                new LoginForm().setVisible(true);
                dispose();  // Close login window
            }
        });
        Container signInContainer = new Container();
        signInContainer.setLayout(new BoxLayout(signInContainer, BoxLayout.X_AXIS));
        signInContainer.add(signInLabel);
        signInContainer.add(signInLink);

        add(usernameContainer);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(emailContainer);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(passwordContainer);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(signupButtonContainer);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(signInContainer);

        signupButton.addActionListener(e -> {

            if(nameField.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getPassword().length == 0) {
                JOptionPane.showMessageDialog(null, "Please fill all the fields!");
                return;
            }

            if(!emailField.getText().matches("^(.+)@(.+)$")) {
                JOptionPane.showMessageDialog(null, "Invalid email format!");
                return;
            }

            if(!nameField.getText().matches("^[a-zA-Z\\s]*$")) {
                JOptionPane.showMessageDialog(null, "Invalid name format!");
                return;
            }

            if(passwordField.getPassword().length < 8) {
                JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long!");
                return;
            }

            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            EncryptionDecryption encryptionDecryption = new EncryptionDecryption();
            String encryptedPassword;
            try {
                encryptedPassword = encryptionDecryption.encrypt(password);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            if(Services.getInstance().getAdminByEmail(email) != null) {
                JOptionPane.showMessageDialog(null, "Email already exists!");
                return;
            }

            if(Services.getInstance().getStudentByEmail(email) != null) {
                JOptionPane.showMessageDialog(null, "Email already exists!");
                return;
            }

            Student student = Services.getInstance().signUp(name, email, encryptedPassword, "");
            if (student == null) {
                JOptionPane.showMessageDialog(null, "Signup failed!");
                return;
            }

            JOptionPane.showMessageDialog(null, "Signup successful!");
            new LoginForm().setVisible(true);
            dispose();  // Close signup form
        });
    }
}
