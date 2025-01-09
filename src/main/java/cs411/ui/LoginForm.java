package cs411.ui;

import cs411.models.Admin;
import cs411.models.Student;
import cs411.services.Services;
import cs411.utils.Config;
import cs411.utils.EncryptionDecryption;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;

    public LoginForm() {
        setTitle("Login");
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

        JLabel usernameLabel = new JLabel("Email: ");
        usernameLabel.setForeground(Color.WHITE);
        emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        Container usernameContainer = new Container();
        usernameContainer.setLayout(new BoxLayout(usernameContainer, BoxLayout.X_AXIS));
        usernameContainer.add(usernameLabel);
        usernameContainer.add(Box.createRigidArea(new Dimension(10, 0)));
        usernameContainer.add(emailField);

        JLabel passwordLabel = new JLabel("Password: ");
        passwordLabel.setForeground(Color.WHITE);
        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        Container passwordContainer = new Container();
        passwordContainer.setLayout(new BoxLayout(passwordContainer, BoxLayout.X_AXIS));
        passwordContainer.add(passwordLabel);
        passwordContainer.add(Box.createRigidArea(new Dimension(10, 0)));
        passwordContainer.add(passwordField);

        JLabel forgotPassword = new JLabel("Forgot Password?");
        forgotPassword.setForeground(Color.WHITE);
        forgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();  // Close login window
                new ForgotPasswordForm().setVisible(true);
            }
        });
        Container forgotPasswordContainer = new Container();
        forgotPasswordContainer.setLayout(new BoxLayout(forgotPasswordContainer, BoxLayout.X_AXIS));
        forgotPasswordContainer.add(Box.createHorizontalGlue());
        forgotPasswordContainer.add(forgotPassword);



        loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setBackground(Color.WHITE);
        loginButton.setForeground(Config.PRIMARY_COLOR);
        Container loginButtonContainer = new Container();
        loginButtonContainer.setLayout(new BoxLayout(loginButtonContainer, BoxLayout.X_AXIS));
        loginButtonContainer.add(loginButton);

        JLabel signupLabel = new JLabel("Don't have an account? ");
        signupLabel.setForeground(Color.WHITE);
        JLabel signupLink = new JLabel("Signup");
        signupLink.setForeground(Color.WHITE);
        signupLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new SignupForm().setVisible(true);
                dispose();  // Close login window
            }
        });
        Container signupContainer = new Container();
        signupContainer.setLayout(new BoxLayout(signupContainer, BoxLayout.X_AXIS));
        signupContainer.add(signupLabel);
        signupContainer.add(signupLink);

        add(usernameContainer);
        add(Box.createRigidArea(new Dimension(0, 15)));

        add(passwordContainer);
        add(Box.createRigidArea(new Dimension(0, 15)));

        add(forgotPasswordContainer);
        add(Box.createRigidArea(new Dimension(0, 15)));

        add(loginButtonContainer);
        add(Box.createRigidArea(new Dimension(0, 30)));

        add(signupContainer);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!emailField.getText().contains("@") || !emailField.getText().contains(".")) {
                    JOptionPane.showMessageDialog(null, "Invalid email format!");
                    return;
                }

                if (passwordField.getPassword().length < 8) {
                    JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long!");
                    return;
                }

                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                EncryptionDecryption encryptionDecryption = new EncryptionDecryption();
                String encryptedPassword;
                try {
                    encryptedPassword = encryptionDecryption.encrypt(password);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                Admin admin = null;
                try {
                    admin = Services.getInstance().loginAsAdmin(email, password);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                if (admin != null) {
                    JOptionPane.showMessageDialog(null, "Admin Login successful!");
                    dispose();  // Close login window
                    new Dashboard().setVisible(true);
                } else {
                    Student student = null;
                    try {
                        student = Services.getInstance().loginAsStudent(email, password);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    if (student != null) {
                        JOptionPane.showMessageDialog(null, "Student Login successful!");
                        dispose();  // Close login window
                        new Dashboard().setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid login credentials!");
                    }
                }
            }
        });

    }

    private class ForgotPasswordForm extends JFrame {
        private JTextField emailField;
        private JButton resetButton;

        public ForgotPasswordForm() {
            setTitle("Forgot Password");
            setSize(400, 200);
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

            JLabel emailLabel = new JLabel("Email: ");
            emailLabel.setForeground(Color.WHITE);
            emailField = new JTextField();
            emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
            Container emailContainer = new Container();
            emailContainer.setLayout(new BoxLayout(emailContainer, BoxLayout.X_AXIS));
            emailContainer.add(emailLabel);
            emailContainer.add(Box.createRigidArea(new Dimension(10, 0)));
            emailContainer.add(emailField);

            resetButton = new JButton("Reset Password");
            resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            resetButton.setBackground(Color.WHITE);
            resetButton.setForeground(Config.PRIMARY_COLOR);
            Container resetButtonContainer = new Container();
            resetButtonContainer.setLayout(new BoxLayout(resetButtonContainer, BoxLayout.X_AXIS));
            resetButtonContainer.add(resetButton);

            add(emailContainer);
            add(Box.createRigidArea(new Dimension(0, 15)));
            add(resetButtonContainer);

            resetButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!emailField.getText().contains("@") || !emailField.getText().contains(".")) {
                        JOptionPane.showMessageDialog(null, "Invalid email format!");
                        return;
                    }

                    String email = emailField.getText();
                    Student student = Services.getInstance().getStudentByEmail(email);
                    if (student != null) {
                        EncryptionDecryption encryptionDecryption = new EncryptionDecryption();
                        student.setPassword(encryptionDecryption.encrypt("1234abcd"));
                        Services.getInstance().updateStudent(student);
                        JOptionPane.showMessageDialog(null, "Password has been reset to 1234abcd");
                    } else {
                        JOptionPane.showMessageDialog(null, "No account found with this email!");
                    }

                    dispose();  // Close forgot password window
                    new LoginForm().setVisible(true);

                }
            });
        }
    }
}
