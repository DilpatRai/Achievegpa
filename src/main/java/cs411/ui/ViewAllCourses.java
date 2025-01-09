package cs411.ui;

import cs411.models.Course;
import cs411.services.Services;
import cs411.utils.Config;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ViewAllCourses extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public ViewAllCourses() {
        setTitle("View All Courses");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Config.PRIMARY_COLOR);

        List<Course> courses = Services.getInstance().getCourses();

        if(courses.isEmpty()) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(Config.PRIMARY_COLOR);
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panel.add(Box.createVerticalGlue());
            JLabel label = new JLabel("No courses found in the database.");
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Arial", Font.BOLD, 16));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(label);
            panel.add(Box.createVerticalGlue());
            add(panel);

            JButton addButton = new JButton("Add Course");
            addButton.addActionListener(e -> {
                addCourse(table);
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


        String[] columnNames = {"Course ID", "Course Code", "Course Name", "Credits", "Edit", "Delete"};
        Object[][] data = {
                {"1","C001", "Introduction to Computer Science", 3, new JButton("Edit"), new JButton("Delete")},
                {"2","C002", "Data Structures", 4, new JButton("Edit"), new JButton("Delete")}
        };

        data = courses.stream().map(course -> new Object[]{course.getCourseID(), course.getCourseCode(), course.getCourseName(), course.getCredits(), new JButton("Edit"), new JButton("Delete")}).toArray(Object[][]::new);

        model = new DefaultTableModel(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex > 3) return JButton.class;
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
                Component component = super.prepareRenderer(renderer, row, column);
                if (column < 4) {
                    ((JLabel) component).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
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
                        (column == 4 && row != -1 && table.getValueAt(row, 4) instanceof JButton)
                                ||
                                (column == 5 && row != -1 && table.getValueAt(row, 5) instanceof JButton)
                ) {
                    if(column == 4) showViewEditCourseDialog(table, row);
                    else deleteCourse(table, row);
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.setBackground(Config.PRIMARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panel, BorderLayout.CENTER);

        JButton addButton = new JButton("Add Course");
        addButton.addActionListener(e -> {
            addCourse(table);
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

    private void addCourse(JTable table) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Course Code:"));
        JTextField courseCodeField = new JTextField();
        panel.add(courseCodeField);
        panel.add(new JLabel("Course Name:"));
        JTextField courseNameField = new JTextField();
        panel.add(courseNameField);
        panel.add(new JLabel("Credits:"));
        JTextField creditsField = new JTextField();
        panel.add(creditsField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Course", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String courseName = courseNameField.getText();
            String courseCode = courseCodeField.getText();
            if (courseCode.length() > 10) {
                JOptionPane.showMessageDialog(null, "Course code must be less than 10 characters.");
                return;
            }

            if (courseName.length() > 50) {
                JOptionPane.showMessageDialog(null, "Course name must be less than 50 characters.");
                return;
            }

            if (courseCode.isEmpty() || courseName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter valid data.");
                return;
            }

            if (creditsField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Course code must be less than 10 characters.");
                return;
            }

            try {
                Integer.parseInt(creditsField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Credits must be a number.");
                return;
            }

            int credits = Integer.parseInt(creditsField.getText());

            if (courseName.isEmpty() || courseCode.isEmpty() || credits <= 0) {
                JOptionPane.showMessageDialog(null, "Please enter valid data.");
                return;
            }

            List<Course> courses = Services.getInstance().getCourses();
            for (Course course : courses) {
                if (course.getCourseCode().equals(courseCode)) {
                    JOptionPane.showMessageDialog(null, "Course with this code already exists.");
                    return;
                }
            }



            Course course = new Course(-1, courseName, courseCode, credits);
            Services.getInstance().createCourse(course);
            String courseID = String.valueOf(course.getCourseID());

            dispose();
            new ViewAllCourses().setVisible(true);
        }
    }

    private static void showViewEditCourseDialog(JTable table, int row) {
        int courseID = (int) table.getValueAt(row, 0);
        String courseCode = (String) table.getValueAt(row, 1);
        String courseName = (String) table.getValueAt(row, 2);
        int credits = Integer.parseInt(table.getValueAt(row, 3).toString());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Course Code:"));
        JTextField courseCodeField = new JTextField(courseCode);
        panel.add(courseCodeField);
        panel.add(new JLabel("Course Name:"));
        JTextField courseNameField = new JTextField(courseName);
        panel.add(courseNameField);
        panel.add(new JLabel("Credits:"));
        JTextField creditsField = new JTextField(String.valueOf(credits));
        panel.add(creditsField);

        int result = JOptionPane.showConfirmDialog(table, panel, "View/Edit Course", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String newCourseCode = courseCodeField.getText();
            String newCourseName = courseNameField.getText();
            int newCredits = Integer.parseInt(creditsField.getText());

            if (newCourseName.isEmpty() || newCourseCode.isEmpty() || newCredits <= 0) {
                JOptionPane.showMessageDialog(null, "Please enter valid data.");
                return;
            }

            List<Course> courses = Services.getInstance().getCourses();
            Course course = courses.stream().filter(c -> c.getCourseID() == courseID).findFirst().orElse(null);
            if (course == null) {
                JOptionPane.showMessageDialog(null, "Course not found.");
                return;
            }

            course.setCourseCode(newCourseCode);
            course.setCourseName(newCourseName);
            course.setCredits(newCredits);
            Services.getInstance().updateCourse(course);

            table.setValueAt(newCourseCode, row, 1);
            table.setValueAt(newCourseName, row, 2);
            table.setValueAt(newCredits, row, 3);
        }
    }

    private void deleteCourse(JTable table, int row) {
        int result = JOptionPane.showConfirmDialog(table, "Are you sure you want to delete this course?", "Delete Course", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            int courseID = (int) table.getValueAt(row, 0);
            List<Course> courses = Services.getInstance().getCourses();
            for (Course course : courses) {
                if (course.getCourseID()==(courseID)) {
                    Services.getInstance().deleteCourse(course);
                    break;
                }
            }
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.removeRow(row);

            this.dispose();
            new ViewAllCourses().setVisible(true);
        }
    }
}



