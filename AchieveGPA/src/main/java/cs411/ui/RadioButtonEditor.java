package cs411.ui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

class RadioButtonRenderer extends JRadioButton implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setSelected((value != null && (Boolean) value));
        setHorizontalAlignment(SwingConstants.CENTER);
        return this;
    }
}

public class RadioButtonEditor extends DefaultCellEditor {
    private final JRadioButton button;

    public RadioButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JRadioButton();
        button.setHorizontalAlignment(SwingConstants.CENTER);

        button.addActionListener(e -> {
            JTable table = (JTable) SwingUtilities.getAncestorOfClass(JTable.class, button);
            if (table != null) {
                int row = table.getEditingRow();
                clearOtherSelections(table, row);
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

    private void clearOtherSelections(JTable table, int selectedRow) {
        for (int i = 0; i < table.getRowCount(); i++) {
            if (i != selectedRow) {
                table.setValueAt(false, i, 0); // Deselect other rows
            }
        }
    }
}
