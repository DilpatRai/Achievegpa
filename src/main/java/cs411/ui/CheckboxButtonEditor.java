package cs411.ui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;



class CheckboxButtonRenderer extends JCheckBox implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setSelected((value != null && (Boolean) value));
        setHorizontalAlignment(SwingConstants.CENTER);
        return this;
    }
}

public class CheckboxButtonEditor extends DefaultCellEditor {
    private final JCheckBox button;

    public CheckboxButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = checkBox;
        button.setHorizontalAlignment(SwingConstants.CENTER);

        button.addActionListener(e -> {
            JTable table = (JTable) SwingUtilities.getAncestorOfClass(JTable.class, button);
            if (table != null) {
                int row = table.getEditingRow();
            }
            fireEditingStopped();
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        button.setSelected(value != null && (Boolean) value);
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return button.isSelected();
    }
}
