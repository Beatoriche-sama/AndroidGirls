package CreatorEngine;

import GUI.SelectDeselectListModel;
import Producers.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ProducerCreator extends JPanel {
    private Producer selectedProducer;
    private ArrayList<Producer> localProducers;

    private final JScrollPane resourcesScrollPane;
    private final JPanel settingsPanel;
    private ResourceValuePanel.CustomJTable customJTable;
    private JFormattedTextField buildingPowerField, knowledgeAmountFiled;
    private JCheckBox isBuiltBox;
    private DefaultListModel<Resource> itemsToProduceListModel;

    private final LinkedCombobox linkedProducersCombobox, linkedResourcesCombobox;
    private LinkedCombobox.ComboBoxMember producerComboBox, parentComboBox, fuelBox;

    protected ProducerCreator(LinkedCombobox linkedResourcesCombobox,
                              LinkedCombobox linkedProducersCombobox) {
        this.linkedResourcesCombobox = linkedResourcesCombobox;
        this.linkedProducersCombobox = linkedProducersCombobox;
        localProducers = new ArrayList<>();

        setBorder(ObjectsCreator.createTitledBorder("Producer creation"));
        setLayout(new MigLayout());
        settingsPanel = new JPanel();
        settingsPanel.setLayout(new MigLayout("flowy"));

        JButton createProducerButton = new JButton("New Producer");
        createProducerButton.addActionListener(e -> {
            if (!settingsPanel.isVisible()) settingsPanel.setVisible(true);
            Producer producer = new Producer();
            producer.setProducerName("New producer");
            selectedProducer = producer;
            localProducers.add(producer);

            linkedProducersCombobox.addItem(producer);
            linkedProducersCombobox.fireAction(producerComboBox,
                    () -> producerComboBox.setSelectedItem(producer));
        });

        JButton deleteProducerButton = new JButton("Delete Producer");
        deleteProducerButton.addActionListener(e -> {
            localProducers.remove(selectedProducer);
            linkedProducersCombobox.removeItem(selectedProducer);
        });

        JTextField nameField = new JTextField(30);
        nameField.addActionListener(e -> {
            selectedProducer.setProducerName(nameField.getText());
            linkedProducersCombobox.updateUIData();
        });

        itemsToProduceListModel = new DefaultListModel<>();
        JList<Resource> itemsToProduceList = new JList<>(itemsToProduceListModel);
        itemsToProduceList.setCellRenderer(new CustomListCellRenderer());
        itemsToProduceList.setSelectionModel(new SelectDeselectListModel());
        itemsToProduceList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addItemToJList(itemsToProduceList, selectedProducer.getResources(), e.getPoint());
                super.mouseClicked(e);
            }
        });

        JPanel unionTypePanel = new JPanel();
        unionTypePanel.setBorder(ObjectsCreator.createTitledBorder("Union type"));
        JPanel buildingEditPanel = createBuildingSettingsPanel();

        JRadioButton manualWorkButton = new JRadioButton("Manual work");
        manualWorkButton.addActionListener(e -> {
            selectedProducer.setAndroidUnion(new AndroidUnion());
            buildingEditPanel.setVisible(false);
        });

        JRadioButton buildingButton = new JRadioButton("Building");
        buildingButton.addActionListener(e -> {
            Building building = new Building();
            selectedProducer.setAndroidUnion(building);
            buildingEditPanel.setVisible(true);
            Map<Resource, Integer> craftMap = new HashMap<>();
            building.setCraftMaterials(craftMap);
        });

        ButtonGroup communityTypeGroup = new ButtonGroup();
        communityTypeGroup.add(manualWorkButton);
        communityTypeGroup.add(buildingButton);
        communityTypeGroup.getElements().asIterator().forEachRemaining(unionTypePanel::add);

        JPanel statPanel = new JPanel();
        statPanel.setBorder(ObjectsCreator.createTitledBorder("Android stat"));
        Map<Android.StatType, JRadioButton> statTypeMap = new HashMap<>();
        statTypeMap.put(Android.StatType.GATHERING, new JRadioButton("Gathering"));
        statTypeMap.put(Android.StatType.GARDENING, new JRadioButton("Gardening"));
        statTypeMap.put(Android.StatType.ALCHEMY, new JRadioButton("Alchemy"));
        statTypeMap.put(Android.StatType.MECHANIC, new JRadioButton("Mechanic"));
        ButtonGroup statButtonGroup = createStatTypeJRadioButtons(statTypeMap, statPanel);

        JPanel produceMethodPanel = new JPanel();
        produceMethodPanel.setBorder(ObjectsCreator.createTitledBorder("Produce method"));
        JRadioButton randomMethodButton = new JRadioButton("Random method");
        randomMethodButton.addActionListener(e -> selectedProducer.setProduceMethod(new RandomItems()));
        JRadioButton definedMethodButton = new JRadioButton("Defined method");
        definedMethodButton.addActionListener(e -> selectedProducer.setProduceMethod(new DefinedItems()));

        ButtonGroup produceMethodGroup = new ButtonGroup();
        produceMethodGroup.add(randomMethodButton);
        produceMethodGroup.add(definedMethodButton);
        produceMethodGroup.getElements().asIterator().forEachRemaining(produceMethodPanel::add);

        JCheckBox isInventedBox = new JCheckBox("Is invented?");
        isInventedBox.addActionListener(e -> selectedProducer.setInvented(isInventedBox.isSelected()));

        parentComboBox = linkedProducersCombobox.createBox();
        parentComboBox.addItemListener(e -> {
            Producer parentProducer = (Producer) parentComboBox.getSelectedItem();
            selectedProducer.setParent(parentProducer);
            if (parentProducer != null)
                parentProducer.getChildren().add(selectedProducer);
        });

        producerComboBox = linkedProducersCombobox.createBox();
        producerComboBox.addItemListener(e -> {
            selectedProducer = (Producer) producerComboBox.getSelectedItem();
            assert selectedProducer != null;
            nameField.setText(selectedProducer.getProducerName());

            ArrayList<Resource> resources = selectedProducer.getResources();
            if (!resources.isEmpty()) {
                ArrayList<Integer> indexesList = new ArrayList<>();
                resources.forEach(resource -> indexesList.add(itemsToProduceListModel.indexOf(resource)));
                itemsToProduceList.setSelectedIndices(indexesList.stream().mapToInt(Integer::intValue).toArray());
            } else itemsToProduceList.clearSelection();

            Android.StatType statType = selectedProducer.getStatType();
            if (statType != null) statTypeMap.get(statType).setSelected(true);
            else statButtonGroup.clearSelection();

            AndroidUnion androidUnion = selectedProducer.getAndroidUnion();
            boolean isBuilding = false;
            if (androidUnion != null) {
                JRadioButton unionTypeButton;
                isBuilding = androidUnion instanceof Building;
                if (isBuilding) {
                    unionTypeButton = buildingButton;
                    Building building = (Building) androidUnion;
                    linkedResourcesCombobox.fireAction(fuelBox,
                            () -> fuelBox.setSelectedItem(building.getFuel()));
                    buildingPowerField.setValue(building.getBuildingPower());
                    knowledgeAmountFiled.setValue(selectedProducer.getKnowledgeValue());
                    customJTable.updateTableData();
                    isBuiltBox.setSelected(selectedProducer.isBuilt());
                } else unionTypeButton = manualWorkButton;
                unionTypeButton.setSelected(true);
            } else communityTypeGroup.clearSelection();
            buildingEditPanel.setVisible(isBuilding);

            ProduceMethod produceMethod = selectedProducer.getProduceMethod();
            if (produceMethod != null) {
                JRadioButton produceMethodButton = produceMethod instanceof RandomItems ?
                        randomMethodButton : definedMethodButton;
                produceMethodButton.setSelected(true);
            } else produceMethodGroup.clearSelection();

            isInventedBox.setSelected(selectedProducer.isInvented());
            parentComboBox.setSelectedItem(selectedProducer.getParent());
        });

        add(createProducerButton, "split 2");
        add(producerComboBox, "wrap");
        add(settingsPanel, "hidemode 1");

        settingsPanel.add(deleteProducerButton, "wrap");
        settingsPanel.add(new JLabel("Name: "), "flowx, split 2");
        settingsPanel.add(nameField);
        settingsPanel.add(new JLabel("Items to produce: "), "flowx, split 2");
        resourcesScrollPane = new JScrollPane(itemsToProduceList);
        settingsPanel.add(resourcesScrollPane);
        settingsPanel.add(unionTypePanel);
        settingsPanel.add(buildingEditPanel, "hidemode 1");
        settingsPanel.add((produceMethodPanel));
        settingsPanel.add(statPanel);
        settingsPanel.add(isInventedBox);
        settingsPanel.add(new JLabel("Parent: "), "flowx, split 2");
        settingsPanel.add(parentComboBox);
    }

    private ButtonGroup createStatTypeJRadioButtons(Map<Android.StatType, JRadioButton> map,
                                                    Container container) {
        ButtonGroup statTypeGroup = new ButtonGroup();
        map.forEach((statType, statButton) -> {
            statButton.addActionListener(e ->
                    selectedProducer.setStatType(statType));
            statTypeGroup.add(statButton);
            container.add(statButton);
        });
        return statTypeGroup;
    }

    private JPanel createBuildingSettingsPanel() {
        JPanel buildingEditPanel = new JPanel();
        buildingEditPanel.setLayout(new MigLayout("flowy"));
        buildingEditPanel.setVisible(false);
        fuelBox = linkedResourcesCombobox.createBox();
        fuelBox.addActionListener(e -> {
            Building building = (Building) selectedProducer.getAndroidUnion();
            if (building == null) return;
            building.setFuel((Resource) fuelBox.getSelectedItem());
        });

        buildingPowerField = new JFormattedTextField(ObjectsCreator.numberFormatter);
        buildingPowerField.setColumns(30);
        buildingPowerField.addActionListener(e -> {
            Building building = (Building) selectedProducer.getAndroidUnion();
            building.setBuildingPower((Integer) buildingPowerField.getValue());
        });

        knowledgeAmountFiled = new JFormattedTextField(ObjectsCreator.numberFormatter);
        knowledgeAmountFiled.setColumns(30);
        knowledgeAmountFiled.addActionListener(e ->
                selectedProducer.setKnowledgeValue((Integer) knowledgeAmountFiled.getValue()));

        LinkedCombobox.ComboBoxMember craftMaterialBox = linkedResourcesCombobox.createBox();
        Supplier<Map<Resource, Integer>> supplier = () -> selectedProducer.getCraftMaterials();
        ResourceValuePanel craftMaterialsPanel =
                new ResourceValuePanel(new String[]{"Craft material", "Value"}, craftMaterialBox, supplier);
        this.customJTable = craftMaterialsPanel.getTable();

        isBuiltBox = new JCheckBox("Is built");
        isBuiltBox.addActionListener(e -> selectedProducer.setBuilt(isBuiltBox.isSelected()));

        buildingEditPanel.add(new JLabel("Fuel: "), "split 2, flowx");
        buildingEditPanel.add(fuelBox);
        buildingEditPanel.add(new JLabel("Building power: "), "split 2, flowx");
        buildingEditPanel.add(buildingPowerField);
        buildingEditPanel.add(new JLabel("Amount of knowledge to invent: "), "split 2, flowx");
        buildingEditPanel.add(knowledgeAmountFiled);
        buildingEditPanel.add(new JScrollPane(craftMaterialsPanel));
        buildingEditPanel.add(isBuiltBox);
        return buildingEditPanel;
    }

    private void addItemToJList(JList jList, ArrayList arrayList, Point point) {
        DefaultListModel listModel = (DefaultListModel) jList.getModel();
        Object object = listModel.getElementAt(jList.locationToIndex(point));
        boolean isSelected = jList.isSelectedIndex(listModel.indexOf(object));
        if (!isSelected) {
            arrayList.remove(object);
        } else {
            arrayList.add(object);
        }
    }

    protected void refreshResourcesData(ArrayList<Resource> resources) {
        itemsToProduceListModel.removeAllElements();
        itemsToProduceListModel.addAll(resources);
        updatePanelUI();

        if (selectedProducer == null) return;
        ArrayList<Resource> items = selectedProducer.getResources();
        if (items != null) items.retainAll(resources);
        Map<Resource, Integer> craftMaterials = selectedProducer.getCraftMaterials();
        if (craftMaterials != null) {
            craftMaterials.keySet().retainAll(resources);
            customJTable.updateTableData();
        }
        AndroidUnion androidUnion = selectedProducer.getAndroidUnion();
        if (!resources.contains(selectedProducer.getFuel()) && androidUnion != null)
            ((Building) androidUnion).setFuel(null);
    }

    protected void updatePanelUI() {
        resourcesScrollPane.updateUI();
        customJTable.updateUI();
    }


    public void updateData(ArrayList<Producer> producers,
                           ArrayList<Resource> resources) {
        this.localProducers = producers;
        linkedProducersCombobox.updateData(producers);

        itemsToProduceListModel.removeAllElements();
        itemsToProduceListModel.addAll(resources);
        settingsPanel.setVisible(!localProducers.isEmpty());
    }

    public ArrayList<Producer> getLocalProducers() {
        return localProducers;
    }
}
