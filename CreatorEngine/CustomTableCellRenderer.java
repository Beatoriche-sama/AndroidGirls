package CreatorEngine;

import Producers.Producer;
import Producers.Resource;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        if (value instanceof Resource) {
            value = ((Resource) value).getResourceName();
        } else if (value instanceof Producer) {
            value = ((Producer) value).getProducerName();
        }
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        return this;
    }
}