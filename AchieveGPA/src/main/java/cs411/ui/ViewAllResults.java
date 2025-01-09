package cs411.ui;

import cs411.models.Course;
import cs411.models.Enrollment;
import cs411.models.Student;
import cs411.services.Services;
import cs411.utils.Config;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class ViewAllResults extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public ViewAllResults() {
        setTitle("View All Results");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setBackground(Config.PRIMARY_COLOR);

        List<Enrollment> enrollments = Services.getInstance().getEnrollments();
        if(enrollments.isEmpty()) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(Config.PRIMARY_COLOR);
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panel.add(Box.createVerticalGlue());
            JLabel label = new JLabel("No results found in the database.");
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Arial", Font.BOLD, 16));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(label);
            panel.add(Box.createVerticalGlue());
            add(panel);

            JButton addButton = new JButton("Add Result");
            addButton.addActionListener(e -> {
                addResult(table);
            });

            addButton.setFont(new Font("Arial", Font.BOLD, 16));
            addButton.setMinimumSize(new Dimension(Integer.MAX_VALUE, 40));
            addButton.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
            addButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            Container container = new Container();
            container.setLayout(new BorderLayout());
            container.add(addButton, BorderLayout.CENTER);
            container.setSize(new Dimension(Integer.MAX_VALUE, 50));
            add(container, BorderLayout.SOUTH);
            return;
        }

        List<Student> students = Services.getInstance().getStudents();
        List<Course> courses = Services.getInstance().getCourses();

        String[] columnNames = {"Student", "Course", "Result", "Edit", "Delete"};
        Object[][] data = {
                {"C001", "Introduction to Computer Science", "A", new JButton("Edit"), new JButton("Delete")},
                {"C002", "Data Structures", "B", new JButton("Edit"), new JButton("Delete")}
        };

        data = enrollments.stream().map(enrollment -> {
            return new Object[]{enrollment.getStudentID(), enrollment.getCourseID(), enrollment.getGrade(), new JButton("Edit"), new JButton("Delete")};
        }).toArray(Object[][]::new);

        model = new DefaultTableModel(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex >= 2) return JButton.class;
                return String.class;
            }
        };
        table = new JTable(model) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                int studentId = Integer.parseInt(table.getValueAt(row, 0).toString());
                int courseId = Integer.parseInt(table.getValueAt(row, 1).toString());
                Student student = students.stream().filter(s -> s.getStudentID() == studentId).findFirst().get();
                Course course = courses.stream().filter(c -> c.getCourseID() == courseId).findFirst().get();
                Component component = super.prepareRenderer(renderer, row, column);
                if (column < 3) {
                    ((JLabel) component).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
                    if(column == 0) ((JLabel) component).setText(student.getName());
                    if(column == 1) ((JLabel) component).setText(course.getCourseName());
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

        table.getColumnModel().getColumn(3).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JButton button = (JButton) value;
            button.setFont(new Font("Arial", Font.BOLD, 12));
            button.setForeground(Config.PRIMARY_COLOR);
            return button;
        });

        table.getColumnModel().getColumn(4).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JButton button = (JButton) value;
            button.setFont(new Font("Arial", Font.BOLD, 12));
            button.setForeground(Config.PRIMARY_COLOR);
            return button;
        });

        table.getTableHeader().setReorderingAllowed(false);

        for (int i = 0; i < table.getTableHeader().getColumnModel().getColumnCount(); i++) {
            table.getTableHeader().getColumnModel().getColumn(i).setHeaderRenderer(new TableCellRenderer() {
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

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = table.columnAtPoint(e.getPoint());
                int row = table.rowAtPoint(e.getPoint());
                if (
                        (column == 3 && row != -1 && table.getValueAt(row, 3) instanceof JButton)
                                ||
                                (column == 4 && row != -1 && table.getValueAt(row, 4) instanceof JButton)
                ) {
                    if(column == 3) showViewEditResultDialog(table, row);
                    else deleteResult(table, row);
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.setBackground(Config.PRIMARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panel, BorderLayout.CENTER);

        JButton addButton = new JButton("Add Result");
        addButton.addActionListener(e -> {
            addResult(table);
        });
        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        addButton.setMinimumSize(new Dimension(Integer.MAX_VALUE, 40));
        addButton.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        addButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        Container container = new Container();
        container.setLayout(new BorderLayout());
        container.add(addButton, BorderLayout.CENTER);
        container.setSize(new Dimension(Integer.MAX_VALUE, 50));
        add(container, BorderLayout.SOUTH);
    }

    private void addResult(JTable table) {

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
                return;
            } else {
                Enrollment enrollment = new Enrollment(-1, studentId, courseId, grade);
                Services.getInstance().createEnrollment(enrollment);
                enrollments.add(enrollment);
            }
            this.dispose();
            new ViewAllResults().setVisible(true);
        }
    }

    private static void showViewEditResultDialog(JTable table, int row) {
        int studentId = (int) table.getValueAt(row, 0);
        int courseId = (int) table.getValueAt(row, 1);
        String grade = (String) table.getValueAt(row, 2);

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
    }

    private void deleteResult(JTable table, int row) {
        int result = JOptionPane.showConfirmDialog(table, "Are you sure you want to delete this result?", "Delete Result", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            int studentId = (int) table.getValueAt(row, 0);
            int courseId = (int) table.getValueAt(row, 1);
            List<Enrollment> enrollments = Services.getInstance().getEnrollments();
            Enrollment enrollment = enrollments.stream().filter(e -> e.getStudentID() == studentId && e.getCourseID() == courseId).findFirst().get();
            Services.getInstance().deleteEnrollment(enrollment);
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.removeRow(row);
            this.dispose();
            new ViewAllResults().setVisible(true);
        }
    }
}
