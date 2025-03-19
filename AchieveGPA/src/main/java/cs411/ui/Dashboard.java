package cs411.ui;

import cs411.models.Admin;
import cs411.models.Course;
import cs411.models.Enrollment;
import cs411.models.Student;
import cs411.services.Services;
import cs411.utils.Config;
import cs411.utils.EncryptionDecryption;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


public class Dashboard extends JFrame {
    private final JPanel sidebarPanel;
    private final CardLayout cardLayout;
    private final JPanel contentPanel;
    private Container container;
    private String currentTab = "Courses"; // Default tab

    private final Map<String, Boolean> tabs = new HashMap<>();

    public Dashboard() {


        tabs.put("Courses", false);
        tabs.put("Students", true);
        tabs.put("Enrollments", false);
        tabs.put("Results", false);

        setTitle("AchieveGPA Dashboard");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setBackground(Config.PRIMARY_COLOR);

        getContentPane().setBackground(Config.PRIMARY_COLOR);


        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(Config.PRIMARY_COLOR);

        sidebarPanel.add(Box.createRigidArea(new Dimension(170, 0)));

        sidebarPanel.add(Box.createVerticalGlue());

        ImageIcon icon = new ImageIcon("src/main/resources/logo.png");
        icon = new ImageIcon(icon.getImage().getScaledInstance(50, 66, Image.SCALE_SMOOTH));
        JLabel logo = new JLabel("AchieveGPA", icon, JLabel.CENTER);
        logo.setFont(new Font("Arial", Font.BOLD, 18));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebarPanel.add(logo);

        sidebarPanel.add(Box.createVerticalGlue());

        addSidebarMenu();

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Config.PRIMARY_COLOR);

        tabs.forEach((tab, index) -> {
    switch (tab) {
        case "Courses" -> contentPanel.add(createCoursesTab(), tab);
        case "Students" -> contentPanel.add(createStudentsTab(), tab);
        case "Enrollments" -> contentPanel.add(Services.getInstance().isAdmin() ? createEnrollmentsTabForAdmin() : createEnrollmentsTabForStudent(), tab);
        case "Results" -> contentPanel.add(createResultsTab(), tab);
        default -> System.out.println("Invalid tab: " + tab);
    }
});
        add(contentPanel, BorderLayout.CENTER);


        sidebarPanel.setSize(new Dimension(300, Integer.MAX_VALUE));
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.WHITE));
        add(sidebarPanel, BorderLayout.WEST);

        sidebarPanel.add(Box.createVerticalGlue());

        JButton profileButton = new JButton("Profile");
        profileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        profileButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        profileButton.addActionListener(e -> openProfile());
        profileButton.setOpaque(false);
        profileButton.setContentAreaFilled(false);
        profileButton.setBorderPainted(true);
        profileButton.setForeground(Color.WHITE);
        profileButton.setBackground(Config.PRIMARY_COLOR);
        sidebarPanel.add(profileButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton logoutButton = new JButton("Logout");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logout
                Services.getInstance().logout();
                dispose();
                new LoginForm().setVisible(true);
            }
        });
        logoutButton.setOpaque(false);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setBorderPainted(true);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(Config.PRIMARY_COLOR);
        sidebarPanel.add(logoutButton);

        sidebarPanel.add(Box.createVerticalGlue());


        cardLayout.show(contentPanel, currentTab);

        contentPanel.requestFocus();

        pack();
    }

    public void switchToTab(String tab) {
        currentTab = tab;
        cardLayout.show(contentPanel, tab);
    }

    private void addSidebarMenu() {
        for (String item : tabs.keySet()) {
            if (tabs.get(item) && !Services.getInstance().isAdmin()) continue;
            JButton button = new JButton(item);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            button.setPreferredSize(new Dimension(0, 30));
            button.setMinimumSize(new Dimension(Integer.MAX_VALUE, 50));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ev) {
                    for (Component component : contentPanel.getComponents()) {
                        if (component.getName().equals(currentTab)) {
                            contentPanel.remove(component);
                            break;
                        }
                    }
                    switch (item) {
                        case "Courses" -> contentPanel.add(createCoursesTab(), item);
                        case "Students" -> contentPanel.add(createStudentsTab(), item);
                        case "Enrollments" -> contentPanel.add(Services.getInstance().isAdmin() ? createEnrollmentsTabForAdmin() : createEnrollmentsTabForStudent(), item);
                        case "Results" -> contentPanel.add(createResultsTab(), item);
                        default -> System.out.println("Invalid tab: " + item);
                    }
                    currentTab = item;
                    cardLayout.show(contentPanel, item);
                }
            });
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            button.setBorderPainted(true);
            button.setBorder(BorderFactory.createLineBorder(Color.WHITE));

            Container container = new Container();
            container.setLayout(new BorderLayout());
            container.setBackground(Config.PRIMARY_COLOR);
            container.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            button.setForeground(Color.WHITE);
            button.setBackground(Config.PRIMARY_COLOR);
            container.add(button, BorderLayout.CENTER);


            sidebarPanel.add(container);
            sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }

    private void openProfile() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Config.PRIMARY_COLOR);
        panel.setSize(300, 300);
        panel.setMinimumSize(new Dimension(300, 150));
        panel.setMaximumSize(new Dimension(300, 150));
        panel.setPreferredSize(new Dimension(300, 150));

        Object user = Services.getInstance().getUser();

        Container nameContainer = new Container();
        nameContainer.setLayout(new BoxLayout(nameContainer, BoxLayout.X_AXIS));
        JLabel nameLabel = new JLabel("Name: ");
        nameLabel.setForeground(Color.WHITE);
        nameContainer.add(nameLabel);
        nameContainer.add(Box.createRigidArea(new Dimension(10, 0)));
        JTextField nameField = new JTextField(Services.getInstance().isAdmin() ? ((Admin) user).getName() : ((Student) user).getName());
        nameContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        nameContainer.add(nameField);
        panel.add(nameContainer);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        Container usernameContainer = new Container();
        usernameContainer.setLayout(new BoxLayout(usernameContainer, BoxLayout.X_AXIS));
        JLabel emailLabel = new JLabel("Email: ");
        emailLabel.setForeground(Color.WHITE);
        usernameContainer.add(emailLabel);
        usernameContainer.add(Box.createRigidArea(new Dimension(10, 0)));
        JTextField usernameField = new JTextField(Services.getInstance().isAdmin() ? ((Admin) user).getEmail() : ((Student) user).getEmail());
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        usernameField.setSize(new Dimension(200, 25));
        usernameContainer.add(usernameField);
        usernameField.setEditable(false);
        panel.add(usernameContainer);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        Container passwordContainer = new Container();
        passwordContainer.setLayout(new BoxLayout(passwordContainer, BoxLayout.X_AXIS));
        JLabel passwordLabel = new JLabel("Password: ");
        passwordLabel.setForeground(Color.WHITE);
        passwordContainer.add(passwordLabel);
        passwordContainer.add(Box.createRigidArea(new Dimension(10, 0)));
        JTextField passwordField = new JTextField("");
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        passwordContainer.add(passwordField);
        panel.add(passwordContainer);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        Container roleContainer = new Container();
        roleContainer.setLayout(new BoxLayout(roleContainer, BoxLayout.X_AXIS));
        JLabel roleLabel = new JLabel("Role: ");
        roleLabel.setForeground(Color.WHITE);
        roleContainer.add(roleLabel);
        roleContainer.add(Box.createRigidArea(new Dimension(10, 0)));
        JTextField roleField = new JTextField(Services.getInstance().isAdmin() ? "Admin" : "Student");
        roleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        roleField.setEnabled(false);
        roleContainer.add(roleField);
        panel.add(roleContainer);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        JDialog dialog = new JDialog(this, "Profile", true);
        dialog.setLayout(new BorderLayout());

        dialog.add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Config.PRIMARY_COLOR);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());

        JButton updateButton = new JButton("Update");
        updateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateButton.setForeground(Config.PRIMARY_COLOR);
        updateButton.addActionListener(e -> {
            if (passwordField.getText().trim().length() < 8) {
                JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long");
                return;
            }

            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Name cannot be empty");
                return;
            }

            EncryptionDecryption encryptionDecryption = new EncryptionDecryption();
            try {
                if (Services.getInstance().isAdmin()) {
                    assert user instanceof Admin;
                    Admin admin = (Admin) user;
                    admin.setEmail(usernameField.getText().trim());
                    admin.setName(nameField.getText().trim());
                    admin.setPassword(encryptionDecryption.encrypt(passwordField.getText().trim()));
                    Services.getInstance().updateAdmin(admin);
                } else {
                    Student student = (Student) user;
                    student.setEmail(usernameField.getText().trim());
                    student.setName(nameField.getText().trim());
                    student.setPassword(encryptionDecryption.encrypt(passwordField.getText().trim()));
                    Services.getInstance().updateStudent(student);
                }
                JOptionPane.showMessageDialog(null, "Profile updated successfully");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to update profile");
            }
            dialog.dispose();
        });
        buttonPanel.add(updateButton);

        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelButton.setForeground(Config.PRIMARY_COLOR);
        cancelButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(cancelButton);

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JPanel createCoursesTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Config.PRIMARY_COLOR);

        panel.add(Box.createVerticalGlue());

        JButton myCoursesButton = new JButton("My Courses");
        myCoursesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        myCoursesButton.setMinimumSize(new Dimension(250, 40));
        myCoursesButton.setPreferredSize(new Dimension(250, 40));
        myCoursesButton.setMaximumSize(new Dimension(250, 40));
        myCoursesButton.addActionListener(e -> {
            List<Enrollment> enrollments = Services.getInstance().getEnrollments();
            Student student = Services.getInstance().getStudent();
            List<Course> courses = Services.getInstance().getCourses();
            List<Enrollment> myEnrollments = enrollments.stream().filter(enrollment -> enrollment.getStudentID() == student.getStudentID()).collect(Collectors.toList());

            if(myEnrollments.isEmpty()) {
                JOptionPane.showMessageDialog(null, "You are not enrolled in any courses.");
                return;
            }
            new MyCourses().setVisible(true);
        });
        myCoursesButton.setOpaque(false);
        myCoursesButton.setContentAreaFilled(false);
        myCoursesButton.setBorderPainted(true);
        myCoursesButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        myCoursesButton.setForeground(Color.WHITE);
        myCoursesButton.setBackground(Config.PRIMARY_COLOR);
        if (!Services.getInstance().isAdmin()) {
            panel.add(myCoursesButton);
            panel.add(Box.createRigidArea(new Dimension(0, 30)));
        }

        JButton viewAllCoursesButton = new JButton("View All Courses");
        viewAllCoursesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewAllCoursesButton.setMinimumSize(new Dimension(250, 40));
        viewAllCoursesButton.setPreferredSize(new Dimension(250, 40));
        viewAllCoursesButton.setMaximumSize(new Dimension(250, 40));

        viewAllCoursesButton.addActionListener(e -> {
            new ViewAllCourses().setVisible(true);
        });
        viewAllCoursesButton.setOpaque(false);
        viewAllCoursesButton.setContentAreaFilled(false);
        viewAllCoursesButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        viewAllCoursesButton.setBorderPainted(true);
        viewAllCoursesButton.setForeground(Color.WHITE);
        viewAllCoursesButton.setBackground(Config.PRIMARY_COLOR);
        if(Services.getInstance().isAdmin()) {
            panel.add(viewAllCoursesButton);
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        panel.add(Box.createVerticalGlue());

        panel.setName("Courses");
        return panel;
    }


    private JPanel createStudentsTab() {
        List<Student> students = Services.getInstance().getStudents();

        if(students.isEmpty()) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(Config.PRIMARY_COLOR);
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panel.add(Box.createVerticalGlue());
            JLabel label = new JLabel("No students found in the database.");
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Arial", Font.BOLD, 16));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(label);
            panel.add(Box.createVerticalGlue());
            panel.setName("Students");
            return panel;
        }

        String[] columnNames = {"Select", "Student ID", "Name", "Major"};

        Object[][] data = {
                {false, "S001", "John Doe", "Engineering"},
                {false, "S002", "Jane Smith", "Science"}
        };

        data = students.stream().map(student -> new Object[]{false, student.getStudentID(), student.getName(), student.getMajor()}).toArray(Object[][]::new);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Config.PRIMARY_COLOR);

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Boolean.class;
                return String.class;
            }
        };
        JTable dataTable = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                if (column > 0) {
                    ((JLabel) component).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
                }
                return component;
            }
        };

        dataTable.getColumn("Student ID").setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setText(value.toString());
                return label;
            }
        });

        dataTable.setRowHeight(30);

        for (int i = 0; i < dataTable.getTableHeader().getColumnModel().getColumnCount(); i++) {
            dataTable.getTableHeader().getColumnModel().getColumn(i).setHeaderRenderer(new TableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel label = new JLabel(value.toString());
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
                    label.setFont(new Font("Arial", Font.BOLD, 12));
                    label.setForeground(Color.WHITE);
                    label.setBackground(Color.DARK_GRAY);
                    label.setOpaque(true);
                    return label;
                }
            });
        }

        dataTable.getColumnModel().getColumn(0).setCellRenderer(new RadioButtonRenderer());
        dataTable.getColumnModel().getColumn(0).setCellEditor(new RadioButtonEditor(new JCheckBox()));

        dataTable.getTableHeader().setReorderingAllowed(false);

        TableColumn column = dataTable.getColumnModel().getColumn(0);
        column.setPreferredWidth(50);
        column.setMaxWidth(50);
        column.setMinWidth(50);

        panel.add(new JScrollPane(dataTable), BorderLayout.CENTER);

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Config.PRIMARY_COLOR);
        mainPanel.add(panel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(Config.PRIMARY_COLOR);
        buttonPanel.add(Box.createHorizontalGlue());

        Function<JTable, Integer> selectedRow = table -> {
            for (int i = 0; i < table.getModel().getRowCount(); i++) {
                System.out.println(table.getModel().getValueAt(i, 0));
                Boolean isSelected = (Boolean) table.getModel().getValueAt(i, 0);
                if (isSelected != null && isSelected) {
                    return i;
                }
            }
            return -1;
        };

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            addStudent(dataTable);
        });
        buttonPanel.add(addButton);

        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton viewButton = new JButton("View/Edit");
        viewButton.addActionListener(e -> {
            if (selectedRow.apply(dataTable) == -1) {
                JOptionPane.showMessageDialog(null, "Please select a student to view/edit");
                return;
            }
            showViewEditStudentDialog(dataTable, selectedRow.apply(dataTable));
        });
        buttonPanel.add(viewButton);

        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            if (selectedRow.apply(dataTable) == -1) {
                JOptionPane.showMessageDialog(null, "Please select a student to delete");
                return;
            }
            if(deleteStudent(dataTable, selectedRow.apply(dataTable))) {
                DefaultTableModel model1 = (DefaultTableModel) dataTable.getModel();
                model1.removeRow(selectedRow.apply(dataTable));
            }
        });
        buttonPanel.add(deleteButton);

        buttonPanel.add(Box.createHorizontalGlue());

        mainPanel.add(buttonPanel);

        mainPanel.setName("Students");
        return mainPanel;


    }

    private boolean deleteStudent(JTable dataTable, int row) {
        int studentID = (int) dataTable.getModel().getValueAt(row, 1);
        if (Services.getInstance().deleteStudent(studentID)) {
            JOptionPane.showMessageDialog(null, "Student deleted successfully");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Failed to delete student");
            return false;
        }
    }

    private void addStudent(JTable table) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        JTextField passwordField = new JTextField();
        panel.add(passwordField);
        panel.add(new JLabel("Major:"));
        JTextField majorField = new JTextField();
        panel.add(majorField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Student", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String major = majorField.getText();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || major.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all the fields");
                return;
            }

            EncryptionDecryption encryptionDecryption = new EncryptionDecryption();
            Student studentAdded = Services.getInstance().signUp(name, email, encryptionDecryption.encrypt(password), major);

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.addRow(new Object[]{false, studentAdded.getStudentID(), name, major});
        }
    }

    private JPanel createEnrollmentsTabForStudent() {
        List<Enrollment> enrollments = Services.getInstance().getEnrollments();
        Student student = Services.getInstance().getStudent();
        List<Course> courses = Services.getInstance().getCourses();

        List<Enrollment> myEnrollments = enrollments.stream().filter(enrollment -> enrollment.getStudentID() == student.getStudentID()).collect(Collectors.toList());

        if(myEnrollments.isEmpty()) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(Config.PRIMARY_COLOR);
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panel.add(Box.createVerticalGlue());
            JLabel label = new JLabel("You are not enrolled in any courses.");
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Arial", Font.BOLD, 16));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(label);
            panel.add(Box.createVerticalGlue());
            panel.setName("Enrollments");
            return panel;
        }

        String[] columnNames = {"Enrolled Course", "Grade"};
        Object[][] data = {
                {"C001", "A"},
                {"C002", "B"}
        };

        data = enrollments.stream().filter(enrollment -> enrollment.getStudentID() == student.getStudentID()).map(enrollment -> {
            Course course = courses.stream().filter(c -> c.getCourseID() == enrollment.getCourseID()).findFirst().orElse(null);
            return new Object[]{course.getCourseID(), enrollment.getGrade()};
        }).toArray(Object[][]::new);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Config.PRIMARY_COLOR);

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable dataTable = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                ((JLabel) component).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
                return component;
            }
        };

        dataTable.getColumn("Enrolled Course").setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setText(courses.stream().filter(c -> c.getCourseID() == Integer.parseInt(value.toString())).findFirst().orElse(null).getCourseName());
                return label;
            }
        });

        dataTable.setRowHeight(30);

        for (int i = 0; i < dataTable.getTableHeader().getColumnModel().getColumnCount(); i++) {
            dataTable.getTableHeader().getColumnModel().getColumn(i).setHeaderRenderer(new TableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel label = new JLabel(value.toString());
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
                    label.setFont(new Font("Arial", Font.BOLD, 12));
                    label.setForeground(Color.WHITE);
                    label.setBackground(Color.DARK_GRAY);
                    label.setOpaque(true);
                    return label;
                }
            });
        }

        dataTable.getTableHeader().setReorderingAllowed(false);

        panel.add(new JScrollPane(dataTable), BorderLayout.CENTER);

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Config.PRIMARY_COLOR);
        mainPanel.add(panel);
        mainPanel.setName("Enrollments");
        return mainPanel;


    }

    private JPanel createEnrollmentsTabForAdmin() {
        List<Enrollment> enrollments = Services.getInstance().getEnrollments();
        JTable dataTable = null;

        if(enrollments.isEmpty()) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(Config.PRIMARY_COLOR);
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panel.add(Box.createVerticalGlue());
            JLabel label = new JLabel("No enrollments found in the database.");
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Arial", Font.BOLD, 16));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(label);
            panel.add(Box.createVerticalGlue());
            panel.setName("Enrollments");
            JButton addButton = new JButton("Add");
            JTable finalDataTable1 = dataTable;
            addButton.addActionListener(e -> {
                addEnrollment(finalDataTable1);
            });
            panel.add(addButton);
            return panel;
        }

        List<Course> courses = Services.getInstance().getCourses();
        List<Student> students = Services.getInstance().getStudents();
        String[] columnNames = {"Select", "Student", "Enrolled Course", "Grade"};
        Object[][] data = {
                {false, "S001", "C001", "A"},
                {false, "S002", "C002", "B"}
        };

        data = enrollments.stream().map(enrollment -> {
            Student student = students.stream().filter(s -> s.getStudentID() == enrollment.getStudentID()).findFirst().orElse(null);
            Course course = courses.stream().filter(c -> c.getCourseID() == enrollment.getCourseID()).findFirst().orElse(null);
//            if (student == null || course == null) return null;
            return new Object[]{false, student.getStudentID(), course.getCourseID(), enrollment.getGrade()};
        }).toArray(Object[][]::new);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Config.PRIMARY_COLOR);

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Boolean.class;
                return String.class;
            }
        };
        JTable finalDataTable = dataTable;
        dataTable = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                if (column > 0) {
                    ((JLabel) component).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

                }
                return component;
            }
        };

            dataTable.getColumn("Student").setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    label.setText(students.stream().filter(s -> s.getStudentID() == Integer.parseInt(value.toString())).findFirst().orElse(null).getName());
                    return label;
                }
            });

            dataTable.getColumn("Enrolled Course").setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    label.setText(courses.stream().filter(c -> c.getCourseID() == Integer.parseInt(value.toString())).findFirst().orElse(null).getCourseName());
                    return label;
                }
            });

        dataTable.setRowHeight(30);

        for (int i = 0; i < dataTable.getTableHeader().getColumnModel().getColumnCount(); i++) {
            dataTable.getTableHeader().getColumnModel().getColumn(i).setHeaderRenderer(new TableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel label = new JLabel(value.toString());
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
                    label.setFont(new Font("Arial", Font.BOLD, 12));
                    label.setForeground(Color.WHITE);
                    label.setBackground(Color.DARK_GRAY);
                    label.setOpaque(true);
                    return label;
                }
            });
        }

        dataTable.getColumnModel().getColumn(0).setCellRenderer(new RadioButtonRenderer());
        dataTable.getColumnModel().getColumn(0).setCellEditor(new RadioButtonEditor(new JCheckBox()));

        dataTable.getTableHeader().setReorderingAllowed(false);

        TableColumn column = dataTable.getColumnModel().getColumn(0);
        column.setPreferredWidth(50);
        column.setMaxWidth(50);
        column.setMinWidth(50);

        panel.add(new JScrollPane(dataTable), BorderLayout.CENTER);

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Config.PRIMARY_COLOR);
        mainPanel.add(panel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(Config.PRIMARY_COLOR);
        buttonPanel.add(Box.createHorizontalGlue());

        Function<JTable, Integer> selectedRow = table -> {
            for (int i = 0; i < table.getModel().getRowCount(); i++) {
                System.out.println(table.getModel().getValueAt(i, 0));
                Boolean isSelected = (Boolean) table.getModel().getValueAt(i, 0);
                if (isSelected != null && isSelected) {
                    return i;
                }
            }
            return -1;
        };

        JButton addButton = new JButton("Add");
        JTable finalDataTable1 = dataTable;
        addButton.addActionListener(e -> {
            addEnrollment(finalDataTable1);
        });
        buttonPanel.add(addButton);

        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton viewButton = new JButton("View/Edit");
        JTable finalDataTable2 = dataTable;
        JTable finalDataTable3 = dataTable;
        viewButton.addActionListener(e -> {
            if (selectedRow.apply(finalDataTable2) == -1) {
                JOptionPane.showMessageDialog(null, "Please select a student to view/edit");
                return;
            }
            showViewEditEnrollmentDialog(finalDataTable2, selectedRow.apply(finalDataTable3));
        });
        buttonPanel.add(viewButton);

        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton deleteButton = new JButton("Delete");
        JTable finalDataTable4 = dataTable;
        JTable finalDataTable5 = dataTable;
        deleteButton.addActionListener(e -> {
            if (selectedRow.apply(finalDataTable4) == -1) {
                JOptionPane.showMessageDialog(null, "Please select a student to delete");
                return;
            }
            deleteEnrollment(finalDataTable4, selectedRow.apply(finalDataTable5));
        });
        buttonPanel.add(deleteButton);

        buttonPanel.add(Box.createHorizontalGlue());

        mainPanel.add(buttonPanel);

        mainPanel.setName("Enrollments");
        return mainPanel;


    }

    private void deleteEnrollment(JTable dataTable, int selectedRow) {
        int studentID = (int) dataTable.getModel().getValueAt(selectedRow, 1);
        int courseID = (int) dataTable.getModel().getValueAt(selectedRow, 2);
        List<Enrollment> enrollments = Services.getInstance().getEnrollments();
        Enrollment enrollment = enrollments.stream().filter(e -> e.getStudentID() == studentID && e.getCourseID() == courseID).findFirst().orElse(null);
        Services.getInstance().deleteEnrollment(enrollment);
        this.dispose();
        Dashboard dashboard = new Dashboard();
        dashboard.setVisible(true);
        dashboard.switchToTab("Enrollments");
    }

    private void addEnrollment(JTable dataTable) {
        List<Student> students = Services.getInstance().getStudents();
        List<Course> courses = Services.getInstance().getCourses();

        if(courses.isEmpty() || students.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No students or courses found. Please add students and courses first.");
            return;
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Student:"));
        JComboBox<String> studentComboBox = new JComboBox<>();
        for (Student student : students) {
            studentComboBox.addItem(student.getStudentID()+" - "+student.getName());
        }
        panel.add(studentComboBox);
        panel.add(new JLabel("Course:"));
        JComboBox<String> courseComboBox = new JComboBox<>();
        for (Course course : courses) {
            courseComboBox.addItem(course.getCourseID()+" | "+course.getCourseName());
        }
        panel.add(courseComboBox);
        panel.add(new JLabel("Grade:"));
        JComboBox<String> gradeComboBox = new JComboBox<>();
        gradeComboBox.addItem("");
        gradeComboBox.addItem("A");
        gradeComboBox.addItem("B");
        gradeComboBox.addItem("C");
        gradeComboBox.addItem("D");
        gradeComboBox.addItem("F");
        panel.add(gradeComboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Result", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int studentId = Integer.parseInt(Objects.requireNonNull(studentComboBox.getSelectedItem()).toString().split("-")[0].trim());
            int courseId = Integer.parseInt(Objects.requireNonNull(courseComboBox.getSelectedItem()).toString().split("\\|")[0].trim());
            String grade = Objects.requireNonNull(gradeComboBox.getSelectedItem()).toString();

            boolean found = false;
            List<Enrollment> enrollments = Services.getInstance().getEnrollments();
            for (Enrollment enrollment : enrollments) {
                if (enrollment.getStudentID() == (studentId) && enrollment.getCourseID() == (courseId)) {
                    found = true;
                    break;
                }
            }

            if(found) {
                Enrollment enrollment = enrollments.stream().filter(e -> e.getStudentID() == studentId && e.getCourseID() == courseId).findFirst().get();
                enrollment.setGrade(grade);
                Services.getInstance().updateEnrollment(enrollment);
            } else {
                Enrollment enrollment = new Enrollment(-1, studentId, courseId, grade);
                Services.getInstance().createEnrollment(enrollment);
                enrollments.add(enrollment);
            }
            this.dispose();
            Dashboard dashboard = new Dashboard();
            dashboard.setVisible(true);
            dashboard.switchToTab("Enrollments");

        }
    }

    private JPanel createResultsTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Config.PRIMARY_COLOR);

        panel.add(Box.createVerticalGlue());

        JButton myResultsButton = new JButton("My Results");
        myResultsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        myResultsButton.setMinimumSize(new Dimension(250, 40));
        myResultsButton.setPreferredSize(new Dimension(250, 40));
        myResultsButton.setMaximumSize(new Dimension(250, 40));
        myResultsButton.addActionListener(e -> {
            List<Enrollment> enrollments = Services.getInstance().getEnrollments();
            Student student = Services.getInstance().getStudent();
            List<Course> courses = Services.getInstance().getCourses();
            List<Enrollment> myEnrollments = enrollments.stream().filter(enrollment -> enrollment.getStudentID() == (student.getStudentID())).toList();

            if(myEnrollments.isEmpty()) {
                JOptionPane.showMessageDialog(null, "You are not enrolled in any courses");
                return;
            }
            new MyResults().setVisible(true);
        });
        myResultsButton.setOpaque(false);
        myResultsButton.setContentAreaFilled(false);
        myResultsButton.setBorderPainted(true);
        myResultsButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        myResultsButton.setForeground(Color.WHITE);
        myResultsButton.setBackground(Config.PRIMARY_COLOR);
        if (!Services.getInstance().isAdmin()) {
            panel.add(myResultsButton);
            panel.add(Box.createRigidArea(new Dimension(0, 30)));
        }

        JButton viewALlResultsButton = new JButton("View All Results");
        viewALlResultsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewALlResultsButton.setMinimumSize(new Dimension(250, 40));
        viewALlResultsButton.setPreferredSize(new Dimension(250, 40));
        viewALlResultsButton.setMaximumSize(new Dimension(250, 40));

        viewALlResultsButton.addActionListener(e -> {
            new ViewAllResults().setVisible(true);
        });
        viewALlResultsButton.setOpaque(false);
        viewALlResultsButton.setContentAreaFilled(false);
        viewALlResultsButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        viewALlResultsButton.setBorderPainted(true);
        viewALlResultsButton.setForeground(Color.WHITE);
        viewALlResultsButton.setBackground(Config.PRIMARY_COLOR);
        if(Services.getInstance().isAdmin()) {
            panel.add(viewALlResultsButton);
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        panel.add(Box.createVerticalGlue());
        panel.setName("Results");
        return panel;
    }


    private void showViewEditEnrollmentDialog(JTable table, int row) {
        int studentID = (int) table.getValueAt(row, 1);
        int courseID = (int) table.getValueAt(row, 2);
        String grade = (String) table.getValueAt(row, 3);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Student ID:"));
        JTextField studentIDField = new JTextField(String.valueOf(studentID));
        studentIDField.setEditable(false);
        panel.add(studentIDField);
        panel.add(new JLabel("Course ID:"));
        JTextField courseIDField = new JTextField(String.valueOf(courseID));
        courseIDField.setEditable(false);
        panel.add(courseIDField);
        panel.add(new JLabel("Grade:"));
        JComboBox<String> gradeField = new JComboBox<>(new String[]{"", "A", "B", "C", "D", "F"});
        if(grade != null && !grade.isEmpty()) {
            gradeField.setSelectedItem(grade);
        } else {
            gradeField.setSelectedItem("");
        }
        panel.add(gradeField);

        int result = JOptionPane.showConfirmDialog(table, panel, "View/Edit Enrollment", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String newStudentID = studentIDField.getText();
            String newCourseID = courseIDField.getText();
            String newGrade = gradeField.getSelectedItem().toString();

            if (newGrade == null || newGrade.isEmpty()) {
                newGrade = "";
            }

            List<Enrollment> enrollments = Services.getInstance().getEnrollments();
            Enrollment enrollment = enrollments.stream().filter(e -> e.getStudentID() == Integer.parseInt(newStudentID) && e.getCourseID() == Integer.parseInt(newCourseID)).findFirst().orElse(null);
            if (enrollment == null) {
                JOptionPane.showMessageDialog(null, "Enrollment not found");
                return;
            }
            enrollment.setGrade(newGrade);
            Services.getInstance().updateEnrollment(enrollment);

            this.dispose();
            Dashboard dashboard = new Dashboard();
            dashboard.setVisible(true);
            dashboard.switchToTab("Enrollments");
        }
    }

    private void showViewEditStudentDialog(JTable table, int row) {

        List<Student> students = Services.getInstance().getStudents();

        int studentID = Integer.parseInt(table.getValueAt(row, 1).toString());
        String name = (String) table.getValueAt(row, 2);
        String major = (String) table.getValueAt(row, 3);
        Student student = students.stream().filter(s -> s.getStudentID() == studentID).findFirst().orElse(null);
        assert student != null;
        EncryptionDecryption encryptionDecryption = new EncryptionDecryption();
        String password = encryptionDecryption.decrypt(student.getPassword());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Student ID:"));
        JTextField studentIDField = new JTextField(String.valueOf(studentID));
        studentIDField.setEditable(false);
        panel.add(studentIDField);
        panel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField(name);
        panel.add(nameField);
        panel.add(new JLabel("Password:"));
        JTextField passwordField = new JTextField(password);
        panel.add(passwordField);
        panel.add(new JLabel("Major:"));
        JTextField majorField = new JTextField(major);
        panel.add(majorField);

        int result = JOptionPane.showConfirmDialog(table, panel, "View/Edit Student", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {

            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Name cannot be empty");
                return;
            }

            if (passwordField.getText().trim().length() < 8) {
                JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long");
                return;
            }

            if (Services.getInstance().isAdmin()) {
                String newStudentID = studentIDField.getText();
                String newName = nameField.getText();
                String newMajor = majorField.getText();
                String newPassword = passwordField.getText();

                student.setStudentID(Integer.parseInt(newStudentID));
                student.setName(newName);
                student.setMajor(newMajor);
                student.setPassword(encryptionDecryption.encrypt(newPassword));
                Services.getInstance().updateStudent(student);

                this.dispose();
                Dashboard dashboard = new Dashboard();
                dashboard.setVisible(true);
                dashboard.switchToTab("Students");
            }
        }
    }

}

