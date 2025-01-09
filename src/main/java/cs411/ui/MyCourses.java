package cs411.ui;

import cs411.models.Course;
import cs411.models.Enrollment;
import cs411.models.Student;
import cs411.services.Services;
import cs411.utils.Config;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

import java.util.List;
import java.util.stream.Collectors;

public class MyCourses extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public MyCourses() {
        setTitle("My Courses");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setBackground(Config.PRIMARY_COLOR);

        List<Enrollment> enrollments = Services.getInstance().getEnrollments();
        Student student = Services.getInstance().getStudent();
        List<Course> courses = Services.getInstance().getCourses();
        List<Enrollment> myEnrollments = enrollments.stream().filter(enrollment -> enrollment.getStudentID() == student.getStudentID()).collect(Collectors.toList());


        String[] columnNames = {"Course ID", "Course Code", "Course Name", "Credits"};
        Object[][] data = {
                {"C001", "CS101", "Introduction to Computer Science", 3},
                {"C002", "CS201", "Data Structures", 4}
        };

        data = myEnrollments.stream().map(enrollment -> {
            Course course = courses.stream().filter(c -> c.getCourseID() == enrollment.getCourseID()).findFirst().orElse(null);
            return new Object[]{course.getCourseID(), course.getCourseCode(), course.getCourseName(), course.getCredits()};
        }).toArray(Object[][]::new);

        model = new DefaultTableModel(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 3 ? Integer.class : String.class;
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
                ((JLabel) component).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

                ((JLabel) component).setHorizontalAlignment(SwingConstants.LEFT);
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

        table.getColumn("Course ID").setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setText(courses.stream().filter(c -> c.getCourseID() == (int) value).findFirst().orElse(null).getCourseName());
                return label;
            }
        });

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

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.setBackground(Config.PRIMARY_COLOR);
        setSize(700, 550);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panel, BorderLayout.CENTER);

        JPanel notePanel = new JPanel();
        notePanel.setLayout(new BorderLayout());
        notePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        notePanel.setBackground(Color.LIGHT_GRAY);

        JLabel noteLabel = new JLabel("Note: You are currently enrolled in the above courses. To remove enrollment, contact your Administrator.");
        noteLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        noteLabel.setHorizontalAlignment(SwingConstants.LEFT);
        notePanel.add(noteLabel);

        Container container = new Container();
        container.setLayout(new BorderLayout());
        Component box = Box.createRigidArea(new Dimension(660, 0));
        box.setBackground(Config.PRIMARY_COLOR);
        container.add(box, BorderLayout.NORTH);
        container.add(notePanel, BorderLayout.SOUTH);
        add(container, BorderLayout.SOUTH);

        pack();
    }
}




