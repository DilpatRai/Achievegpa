package cs411.ui;

import cs411.models.Course;
import cs411.models.Enrollment;
import cs411.models.GPA;
import cs411.models.Student;
import cs411.services.Services;
import cs411.utils.Config;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.function.Function;

public class MyResults extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public MyResults() {
        setTitle("My Results");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setBackground(Config.PRIMARY_COLOR);

        List<Enrollment> enrollments = Services.getInstance().getEnrollments();
        Student student = Services.getInstance().getStudent();
        List<Course> courses = Services.getInstance().getCourses();
        List<Enrollment> myEnrollments = enrollments.stream().filter(enrollment -> enrollment.getStudentID() == (student.getStudentID())).toList();

        myEnrollments = myEnrollments.stream().filter(enrollment -> !enrollment.getGrade().isEmpty()).toList();

        Function<JTable, List<Integer>> selectedRow = table -> {
            List<Integer> selectedRows = new ArrayList<>();
            for (int i = 0; i < table.getModel().getRowCount(); i++) {
                System.out.println(table.getModel().getValueAt(i, 6));
                Boolean isSelected = (Boolean) table.getModel().getValueAt(i, 6);
                if (isSelected != null && isSelected) {
                    selectedRows.add(i);
                }
            }
            return selectedRows;
        };


        if (myEnrollments.isEmpty()) {

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(Config.PRIMARY_COLOR);
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panel.add(Box.createVerticalStrut(200));
            JLabel label = new JLabel("No results found in the database.");
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Arial", Font.BOLD, 16));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.setSize(700, 550);
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panel.add(label);
            panel.add(Box.createVerticalStrut(200));
            add(panel, BorderLayout.CENTER);
        } else {
            String[] columnNames = {"Course ID", "Course Code", "Course Name", "Result", "Edit", "Delete", "Select"};
            Object[][] data = {
                    {"C001", "CS101", "Introduction to Computer Science", "A", new JButton("Edit"), new JButton("Delete"), false},
                    {"C002", "CS201", "Data Structures", "B", new JButton("Edit"), new JButton("Delete"), false}
            };

            data = myEnrollments.stream().map(enrollment -> {
                Course course = courses.stream().filter(c -> c.getCourseID() == enrollment.getCourseID()).findFirst().orElse(null);
                return new Object[]{
                        course.getCourseID(),
                        course.getCourseCode(),
                        course.getCourseName(),
                        enrollment.getGrade(),
                        new JButton("Edit"),
                        new JButton("Delete"),
                        false
                };
            }).toArray(Object[][]::new);

            model = new DefaultTableModel(data, columnNames) {
                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    if (columnIndex == 4 || columnIndex == 5) return JButton.class;
                    if (columnIndex == 6) return Boolean.class;
                    return String.class;
                }
            };
            table = new JTable(model) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 6;
                }

                @Override
                public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                    Component component = super.prepareRenderer(renderer, row, column);
                    if (column < 4) {
                        ((JLabel) component).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

                        ((JLabel) component).setHorizontalAlignment(SwingConstants.LEFT);
                    }
                    return component;
                }
                @Override
                public void doLayout() {
                    TableColumnModel columnModel = getColumnModel();
                    for (int column = 0; column < columnModel.getColumnCount(); column++) {
                        TableColumn tableColumn = columnModel.getColumn(column);
                        int preferredWidth = tableColumn.getMinWidth();
                        int maxWidth = tableColumn.getMaxWidth();

                        for (int row = 0; row < getRowCount(); row++) {
                            TableCellRenderer cellRenderer = getCellRenderer(row, column);
                            Component c = prepareRenderer(cellRenderer, row, column);
                            int width = c.getPreferredSize().width + getIntercellSpacing().width;
                            preferredWidth = Math.max(preferredWidth, width);

                            if (preferredWidth >= maxWidth) {
                                preferredWidth = maxWidth;
                                break;
                            }
                        }
                        tableColumn.setPreferredWidth(preferredWidth);
                    }
                    super.doLayout();
                }
            };
            table.setRowHeight(30);

            for (int i = 0; i < table.getTableHeader().getColumnModel().getColumnCount(); i++) {
                table.getTableHeader().getColumnModel().getColumn(i).setHeaderRenderer((table, value, isSelected, hasFocus, row, column) -> {
                    JLabel label = new JLabel(value.toString());
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
                    label.setFont(new Font("Arial", Font.BOLD, 12));
                    label.setForeground(Color.WHITE);
                    label.setBackground(Color.DARK_GRAY);
                    label.setOpaque(true);
                    return label;
                });
            }

            table.getColumnModel().getColumn(6).setCellRenderer(new CheckboxButtonRenderer());
            table.getColumnModel().getColumn(6).setCellEditor(new CheckboxButtonEditor(new JCheckBox()));

            table.getColumn("Course ID").setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    label.setText(courses.stream().filter(c -> c.getCourseID() == Integer.parseInt(value.toString())).findFirst().orElse(null).getCourseName());
                    return label;
                }
            });

            table.getTableHeader().setReorderingAllowed(false);


            TableColumn tableColumn = table.getColumnModel().getColumn(6);
            tableColumn.setPreferredWidth(50);
            tableColumn.setMaxWidth(50);
            tableColumn.setMinWidth(50);


            table.getColumnModel().getColumn(4).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
                JButton button = (JButton) value;
                button.setFont(new Font("Arial", Font.BOLD, 12));
                button.setForeground(Config.PRIMARY_COLOR);
                return button;
            });

            table.getColumnModel().getColumn(5).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
                JButton button = (JButton) value;
                button.setFont(new Font("Arial", Font.BOLD, 12));
                button.setForeground(Config.PRIMARY_COLOR);
                return button;
            });


            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int column = table.columnAtPoint(e.getPoint());
                    int row = table.rowAtPoint(e.getPoint());
                    if (
                            (column == 4 && row != -1 && table.getValueAt(row, 4) instanceof JButton)
                                    ||
                                    (column == 5 && row != -1 && table.getValueAt(row, 5) instanceof JButton)
                    ) {
                        if(column == 4) showViewEditResultDialog(table, row);
                        else deleteResult(table, row);
                    }
                }
            });

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(new JScrollPane(table), BorderLayout.CENTER);
            panel.setBackground(Config.PRIMARY_COLOR);
            setSize(700, 550);
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            add(panel, BorderLayout.CENTER);
        }

        Container container = new Container();
        container.setLayout(new FlowLayout());
        JButton addButton = new JButton("Add Result");
        addButton.addActionListener(e -> {
            addResult(table);
        });
        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        container.add(addButton);
        JButton visualize = new JButton("Visualize");
        visualize.addActionListener(e -> {
            List<Integer> selectedRows = selectedRow.apply(table);
            if (selectedRows.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please select result(s) to visualize.");
            } else {
                System.out.println("Selected rows: " + selectedRows);
                Map<String, String> grades = new HashMap<>();
                for (int row : selectedRows) {
                    String courseId = (String) table.getValueAt(row, 1);
                    String grade = (String) table.getValueAt(row, 3);
                    grades.put(courseId, grade);
                }
                showVisualizationDialog(grades);
            }
        });
        visualize.setFont(new Font("Arial", Font.BOLD, 16));
        if(!myEnrollments.isEmpty()) container.add(visualize);

        JButton calculateGPA = new JButton("Calculate GPA");
        calculateGPA.addActionListener(event -> {
            List<Integer> selectedRows = selectedRow.apply(table);
            if (selectedRows.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please select result(s) to calculate.");
            } else {
                System.out.println("Selected rows: " + selectedRows);
                Map<String, String> grades = new HashMap<>();
                for (int row : selectedRows) {
                    String courseCode = (String) table.getValueAt(row, 1);
                    String grade = (String) table.getValueAt(row, 3);
                    grades.put(courseCode, grade);
                }
                calculateAndShowGPA(grades);
            }
        });
        calculateGPA.setFont(new Font("Arial", Font.BOLD, 16));
        if(!myEnrollments.isEmpty()) container.add(calculateGPA);

        JButton calculationHistory = new JButton("Calculation History");
        calculationHistory.addActionListener(actionEvent -> {
            List<GPA> gpas = Services.getInstance().getGPAs();
            Student student1 = Services.getInstance().getStudent();
            gpas = gpas.stream().filter(gpa -> gpa.getStudentID() == student1.getStudentID()).toList();
            if (gpas.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No GPA calculation history found.");
                return;
            }
            new CalculationHistory().setVisible(true);
        });
        calculationHistory.setFont(new Font("Arial", Font.BOLD, 16));
        container.add(calculationHistory);

        container.setBackground(Config.PRIMARY_COLOR);


        container.setPreferredSize(new Dimension(660, 40));
        add(container, BorderLayout.SOUTH);

        pack();
    }

    private double getGradePoints(String grade) {
        return switch (grade) {
            case "A" -> 4.0;
            case "B" -> 3.0;
            case "C" -> 2.5;
            case "D" -> 2.0;
            default -> 0.0;
        };
    }


    private void calculateAndShowGPA(Map<String, String> grades) {
        createProcessingDialog(grades);
    }

    private int getCredits(String courseCode) {
        List<Course> courses = Services.getInstance().getCourses();
        return courses.stream()
                .filter(course -> course.getCourseCode().equals(courseCode))
                .map(Course::getCredits)
                .findFirst()
                .orElse(0);
    }


    private void createProcessingDialog(Map<String, String> grades) {

        JPanel dialog = new JPanel();
        dialog.setSize(200, 100);

        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setSize(200, 100);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setAlignmentY(Component.CENTER_ALIGNMENT);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ImageIcon loadingIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/loading.gif")));
        loadingIcon.setImage(loadingIcon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
        JLabel loadingLabel = new JLabel(loadingIcon);
        loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadingLabel.setPreferredSize(new Dimension(50, 50));
        panel.add(loadingLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel loadingText = new JLabel("Processing...");
        loadingText.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(loadingText);

        dialog.add(panel, BorderLayout.CENTER);

        JDialog dialogWindow = new JDialog();
        dialogWindow.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogWindow.setModal(true);
        dialogWindow.setLocationRelativeTo(this);
        dialogWindow.setSize(200, 150);
        dialogWindow.setLayout(new BorderLayout());
        dialogWindow.add(dialog, BorderLayout.CENTER);

        System.out.println("Calculating GPA...");

        double totalPoints = 0;
        int totalCredits = 0;

        for (Map.Entry<String, String> entry : grades.entrySet()) {
            String courseCode = entry.getKey();
            String grade = entry.getValue();

            int credits = getCredits(courseCode);
            double gradePoints = getGradePoints(grade);

            totalPoints += gradePoints * credits;
            totalCredits += credits;
        }

        String result;
        if (totalCredits > 0) {
            double gpa = totalPoints / totalCredits;
            result = String.format(
                    "<html>GPA: %.2f<br>Total Credits: %d<br>Total Points: %.2f</html>",
                    gpa, totalCredits, totalPoints
            );

            GPA gpaObj = new GPA(-1, Services.getInstance().getStudent().getStudentID(), gpa, Date.valueOf(LocalDate.now()));
            Services.getInstance().createGPA(gpaObj);
        } else {
            result = "No courses completed.";
        }
        System.out.println(result);

        Timer timer = new Timer(2000, e -> {
            dialogWindow.dispose();
            showGpaDialog(result);
        });
        SwingUtilities.invokeLater(() -> {
            timer.setRepeats(false);
            timer.start();
        });
        dialogWindow.setVisible(true);
    }

    private void showGpaDialog(String result) {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel resultLabel = new JLabel(result);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(resultLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton exportPdfButton = new JButton("Export to PDF");
        exportPdfButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Exporting to PDF...");
        });
        exportPdfButton.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(exportPdfButton);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton exportExcelButton = new JButton("Export to Excel");
        exportExcelButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Exporting to Excel...");
        });

        exportExcelButton.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(exportExcelButton);

        JOptionPane.showMessageDialog(null, panel, "GPA Calculation Result", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addResult(JTable table) {

        Student currentStudent = Services.getInstance().getStudent();

        List<Enrollment> enrollments = Services.getInstance().getEnrollments();
        List<Enrollment> myEnrollments = enrollments.stream().filter(enrollment -> enrollment.getStudentID() == (currentStudent.getStudentID())).toList();
        List<Student> students = Services.getInstance().getStudents();
        List<Course> courses = Services.getInstance().getCourses();
        courses = courses.stream().filter(course -> myEnrollments.stream().anyMatch(enrollment -> enrollment.getCourseID() == course.getCourseID())).toList();

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
        studentComboBox.setSelectedItem(currentStudent.getStudentID()+" - "+currentStudent.getName());
        studentComboBox.setEnabled(false);

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
            new MyResults().setVisible(true);
        }
    }

    private void deleteResult(JTable table, int row) {
        int result = JOptionPane.showConfirmDialog(table, "Are you sure you want to delete this result?", "Delete Result", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            int courseId = (int) table.getValueAt(row, 0);
            int studentId = Services.getInstance().getStudent().getStudentID();
            List<Enrollment> enrollments = Services.getInstance().getEnrollments();
            Enrollment enrollment = enrollments.stream().filter(e -> e.getStudentID() == studentId && e.getCourseID() == courseId).findFirst().get();
            enrollment.setGrade("");
            Services.getInstance().updateEnrollment(enrollment);
            this.dispose();
            new MyResults().setVisible(true);
        }
    }

    private void showViewEditResultDialog(JTable table, int row) {
        int studentId = Services.getInstance().getStudent().getStudentID();
        int courseId = (int) table.getValueAt(row, 0);
        String grade = (String) table.getValueAt(row, 3);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Student ID:"));
        JTextField studentIdField = new JTextField(String.valueOf(studentId));
        studentIdField.setEditable(false);
        panel.add(studentIdField);
        panel.add(new JLabel("Course ID:"));
        JTextField courseIdField = new JTextField(String.valueOf(courseId));
        courseIdField.setEditable(false);
        panel.add(courseIdField);
        panel.add(new JLabel("Grade:"));
        JComboBox<String> gradeComboBox = new JComboBox<>();
        gradeComboBox.addItem("");
        gradeComboBox.addItem("A");
        gradeComboBox.addItem("B");
        gradeComboBox.addItem("C");
        gradeComboBox.addItem("D");
        gradeComboBox.addItem("F");
        gradeComboBox.setSelectedItem(grade);
        panel.add(gradeComboBox);

        int result = JOptionPane.showConfirmDialog(table, panel, "View/Edit Result", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String newGrade = gradeComboBox.getSelectedItem().toString();
            List<Enrollment> enrollments = Services.getInstance().getEnrollments();
            Enrollment enrollment = enrollments.stream().filter(e -> e.getStudentID() == studentId && e.getCourseID() == courseId).findFirst().get();
            enrollment.setGrade(newGrade);
            Services.getInstance().updateEnrollment(enrollment);
            table.setValueAt(newGrade, row, 2);
        }

        this.dispose();
        new MyResults().setVisible(true);
    }

    private void showVisualizationDialog(Map<String, String> grades) {
        String[] options = {"Bar Chart", "Pie Chart"};
        String choice = (String) JOptionPane.showInputDialog(
                this,
                "Choose Visualization Type",
                "Visualization Options",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice != null) {
            Map<String, Integer> gradeCounts = countGrades(grades);

            if (choice.equals("Bar Chart")) {
                showBarChart(gradeCounts);
            } else if (choice.equals("Pie Chart")) {
                showPieChart(gradeCounts);
            }
        }
    }

    private Map<String, Integer> countGrades(Map<String, String> grades) {
        Map<String, Integer> gradeCounts = new HashMap<>();
        for (String grade : grades.values()) {
            gradeCounts.put(grade, gradeCounts.getOrDefault(grade, 0) + 1);
        }
        return gradeCounts;
    }

    private void showBarChart(Map<String, Integer> gradeCounts) {
        JFrame barChartFrame = new JFrame("Bar Chart");
        barChartFrame.setLayout(new BoxLayout(barChartFrame.getContentPane(), BoxLayout.X_AXIS));
        barChartFrame.setLocationRelativeTo(null);
        barChartFrame.setSize(400, 300);
        barChartFrame.add(Box.createRigidArea(new Dimension(10, 10)));
        barChartFrame.add(new GradeBarChart(gradeCounts));
        barChartFrame.setVisible(true);
    }

    private void showPieChart(Map<String, Integer> gradeCounts) {
        JFrame pieChartFrame = new JFrame("Pie Chart");
        pieChartFrame.setLocationRelativeTo(null);
        pieChartFrame.setSize(400, 300);
        pieChartFrame.add(new GradePieChart(gradeCounts));
        pieChartFrame.setVisible(true);
    }

    static class GradeBarChart extends JPanel {
        private final Map<String, Integer> gradeCounts;

        public GradeBarChart(Map<String, Integer> gradeCounts) {
            super();
            this.gradeCounts = gradeCounts;
            this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth()-20;
            int height = getHeight()-20;

            int barWidth = width / gradeCounts.size();
            int maxCount = gradeCounts.values().stream().max(Integer::compareTo).orElse(1);

            int x = 0;
            for (Map.Entry<String, Integer> entry : gradeCounts.entrySet()) {
                String grade = entry.getKey();
                int count = entry.getValue();

                int barHeight = (int) ((double) count / maxCount * (height - 30));
                g2d.setColor(Config.PRIMARY_COLOR);
                g2d.fillRect(x, height - barHeight - 10, barWidth - 20, barHeight);
                g2d.setColor(Color.BLACK);
                g2d.drawString(grade+" ("+count+")", x + (barWidth / 4), height);

                x += barWidth;
            }
        }
    }

    static class GradePieChart extends JPanel {
        private final Map<String, Integer> gradeCounts;

        public GradePieChart(Map<String, Integer> gradeCounts) {
            this.gradeCounts = gradeCounts;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();

            int total = gradeCounts.values().stream().mapToInt(Integer::intValue).sum();
            int startAngle = 0;

            int centerX = width / 2;
            int centerY = height / 2;
            int radius = Math.min(width, height) / 3;

            Color[] colors = {Config.PRIMARY_COLOR, Color.GREEN, Color.BLUE, Color.RED, Color.ORANGE, Color.YELLOW};
            int colorIndex = 0;
            Function<Color, String> getColorName = color -> {
                if (color.equals(Config.PRIMARY_COLOR)) return "Dark Blue";
                if (color.equals(Color.BLUE)) return "Blue";
                if (color.equals(Color.GREEN)) return "Green";
                if (color.equals(Color.RED)) return "Red";
                if (color.equals(Color.ORANGE)) return "Orange";
                if (color.equals(Color.YELLOW)) return "Yellow";
                return "Unknown";
            };

            for (Map.Entry<String, Integer> entry : gradeCounts.entrySet()) {
                String grade = entry.getKey();
                int count = entry.getValue();

                int angle = (int) ((double) count / total * 360);
                Color color = colors[colorIndex % colors.length];
                g2d.setColor(color);
                g2d.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, startAngle, angle);
                startAngle += angle;

                g2d.setColor(Color.BLACK);
                g2d.drawString(grade+" ("+count+")"+" - "+getColorName.apply(color), 10, 20 + colorIndex * 20);
                colorIndex++;
            }
        }
    }
}
