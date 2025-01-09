package cs411.ui;

import cs411.models.GPA;
import cs411.models.Student;
import cs411.services.Services;
import cs411.utils.Config;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalculationHistory extends JFrame {
    public CalculationHistory() {
        setTitle("My GPA Calculation History");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setBackground(Config.PRIMARY_COLOR);

        List<GPA> gpas = Services.getInstance().getGPAs();
        Student student = Services.getInstance().getStudent();
        gpas = gpas.stream().filter(gpa -> gpa.getStudentID() == student.getStudentID()).toList();


        String[] columnNames = {"ID", "Calculation Date", "GPA"};
        Object[][] data = {
                {"C001", LocalDate.now().minusDays(4).format(DateTimeFormatter.ofPattern("uuuu/MM/dd")), "3.75"},
                {"C002", LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("uuuu/MM/dd")), "2.75"}
        };

        data = gpas.stream().map(gpa -> new Object[]{gpa.getGpaID(), gpa.getCalculationDate().toLocalDate().format(DateTimeFormatter.ofPattern("uuuu/MM/dd")), gpa.getGpaValue()}).toArray(Object[][]::new);

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

        add(mainPanel);
    }
}
