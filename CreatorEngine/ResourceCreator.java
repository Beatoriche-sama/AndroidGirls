package CreatorEngine;

import Producers.Resource;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ResourceCreator extends JPanel {
    private ArrayList<Resource> localResources;
    private Resource selectedResource;

    private LinkedCombobox linkedCombobox;
    private final LinkedCombobox.ComboBoxMember selectedResourceBox, sourcesBox;

    private ResourceValuePanel.CustomJTable sourceTable;
    private final DefaultTableModel sourceTableModel;
    private final JPanel settingsPanel;

    protected ResourceCreator(LinkedCombobox linkedCombobox,
                              ProducerCreator producerCreator) {
        localResources = new ArrayList<>();
        this.linkedCombobox = linkedCombobox;

        setLayout(new MigLayout());
        setBorder(ObjectsCreator.createTitledBorder("Resource Creation"));

        settingsPanel = new JPanel();
        settingsPanel.setLayout(new MigLayout());

        JTextField resNameField = new JTextField(30);
        resNameField.addActionListener(e -> {
            selectedResource.setResourceName(resNameField.getText());
            linkedCombobox.updateUIData();
            producerCreator.updatePanelUI();
            sourceTable.updateUI();
        });


        JFormattedTextField resValueField = new JFormattedTextField(ObjectsCreator.numberFormatter);
        resValueField.setColumns(30);
        resValueField.addActionListener(e ->
                selectedResource.setValue((Integer) resValueField.getValue()));

        JFormattedTextField resDifficultyField = new JFormattedTextField(ObjectsCreator.numberFormatter);
        resDifficultyField.setColumns(30);
        resDifficultyField.addActionListener(e ->
                selectedResource.setDifficulty((Integer) resDifficultyField.getValue()));

        selectedResourceBox = linkedCombobox.createBox();

        sourcesBox = linkedCombobox.createBox();
        Supplier<Map<Resource, Integer>> supplier = () -> selectedResource.getSourcePairs();
        ResourceValuePanel tablePane = new ResourceValuePanel(new String[]{"Source name", "Value"}, sourcesBox, supplier);
        tablePane.setVisible(false);
        this.sourceTable = tablePane.getTable();
        this.sourceTableModel = tablePane.getTableModel();

        JCheckBox hasSourcesButton = new JCheckBox("Has sources?");
        hasSourcesButton.addActionListener(e -> {
            boolean isProduct = selectedResource.getSourcePairs() != null;
            if (isProduct) {
                selectedResource.setSource(null);
                tablePane.setVisible(false);
                this.sourceTableModel.setNumRows(0);
            } else {
                selectedResource.setSource(new HashMap<>());
                tablePane.setVisible(true);
            }
        });

        JButton createButton = new JButton("New resource");
        createButton.addActionListener(e -> {
            if(!settingsPanel.isVisible()) settingsPanel.setVisible(true);
            Resource resource = new Resource("New resource");
            selectedResource = resource;
            localResources.add(resource);
            linkedCombobox.addItem(resource);
            Runnable runnable = ()-> selectedResourceBox.setSelectedItem(resource);
            linkedCombobox.fireAction(selectedResourceBox, runnable);
            producerCreator.refreshResourcesData(localResources);
        });

        JButton deleteButton = new JButton("Delete resource");
        deleteButton.addActionListener(e -> {
            localResources.remove(selectedResource);
            linkedCombobox.removeItem(selectedResource);
            producerCreator.refreshResourcesData(localResources);
        });

        JCheckBox isDiscoveredBox = new JCheckBox("Is discovered?");
        isDiscoveredBox.addActionListener(e ->
                selectedResource.setDiscovered(!selectedResource.isDiscovered()));

        selectedResourceBox.addActionListener(e -> {
            selectedResource = (Resource) selectedResourceBox.getSelectedItem();
            resNameField.setText(selectedResource.getResourceName());
            resValueField.setValue(selectedResource.getValue());
            resDifficultyField.setValue(selectedResource.getDifficulty());
            tablePane.setVisible(hasSourcesButton.isSelected());
            isDiscoveredBox.setSelected(selectedResource.isDiscovered());
            Map<Resource, Integer> sourcesMap = selectedResource.getSourcePairs();
            boolean hasSources = sourcesMap != null;
            if (hasSources) sourceTable.updateTableData();
            tablePane.setVisible(hasSources);
            hasSourcesButton.setSelected(hasSources);
        });

        add(createButton, "split 2");
        add(selectedResourceBox, "growx, wrap");
        add(settingsPanel, "hidemode 1");

        settingsPanel.add(deleteButton, "wrap");
        settingsPanel.add(new JLabel("Name: "));
        settingsPanel.add(resNameField, "wrap");
        settingsPanel.add(new JLabel("Value: "));
        settingsPanel.add(resValueField, "wrap");
        settingsPanel.add(new JLabel("Difficulty: "));
        settingsPanel.add(resDifficultyField, "wrap");
        settingsPanel.add(isDiscoveredBox, "wrap");
        settingsPanel.add(hasSourcesButton);
        settingsPanel.add(tablePane, "grow, hidemode 1");
    }

    protected void updateData(ArrayList<Resource> resources) {
        this.localResources = resources;
        linkedCombobox.updateData(resources);
        settingsPanel.setVisible(!resources.isEmpty());
    }

    protected ArrayList<Resource> getLocalResources() {
        return localResources;
    }
}
