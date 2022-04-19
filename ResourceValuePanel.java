package CreatorEngine;

import Producers.Resource;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.util.Map;
import java.util.function.Supplier;

public class ResourceValuePanel extends JPanel {
    private final String[] columnNames;
    private final LinkedCombobox.ComboBoxMember resourceBox;
    private final Supplier<Map<Resource, Integer>> mapSupplier;
    private final CustomJTable table;
    private DefaultTableModel tableModel;

    public ResourceValuePanel(String[] columnNames,
                              LinkedCombobox.ComboBoxMember resourceBox,
                              Supplier<Map<Resource, Integer>> mapSupplier) {
        this.columnNames = columnNames;
        this.resourceBox = resourceBox;
        this.mapSupplier = mapSupplier;

        table = new CustomJTable();

        JButton addResourceButton = new JButton("Add resource");
        addResourceButton.addActionListener(e -> {
            int currentRowNumber = table.getRowCount();
            tableModel.setNumRows(currentRowNumber + 1);
        });
        JButton removeResourceButton = new JButton("Remove resource");
        removeResourceButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            mapSupplier.get().remove((Resource) table.getValueAt(selectedRow, 0));
            tableModel.removeRow(selectedRow);
        });

        setLayout(new MigLayout("flowy"));
        add(addResourceButton, "split 2, flowx");
        add(removeResourceButton);
        add(new JScrollPane(table));
    }

    public CustomJTable getTable() {
        return table;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    class CustomJTable extends JTable {

        CustomJTable() {
            tableModel = new DefaultTableModel(0, 2);
            tableModel.setColumnIdentifiers(columnNames);
            setModel(tableModel);

            TableColumn sourceColumn = getColumnModel().getColumn(0);
            sourceColumn.setCellRenderer(new CustomTableCellRenderer());

            sourceColumn.setCellEditor(new DefaultCellEditor(resourceBox));

            TableColumn valueColumn = getColumnModel().getColumn(1);
            valueColumn.setCellEditor(new DefaultCellEditor(new JFormattedTextField(ObjectsCreator.numberFormatter)));
        }

        public void updateTableData(){
            tableModel.setRowCount(0);
            mapSupplier.get().forEach((source, value)->
                    tableModel.addRow(new Object[]{source, value}));
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            Map<Resource, Integer> sourcesMap = mapSupplier.get();
            Resource resource = (Resource) getValueAt(row, 0);
            if (column == 0) {
                Object value = getValueAt(row, 1);
                int intValue = value == null ? 0 : Integer.parseInt((String) value);
                sourcesMap.remove(resource);
                sourcesMap.put((Resource) aValue, intValue);
            } else if (column == 1) {
                sourcesMap.replace(resource, Integer.parseInt((String) aValue));
            }
            super.setValueAt(aValue, row, column);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            Object keyObject = getValueAt(row, 0);
            return keyObject != null || column == 0;
        }

    }

}
