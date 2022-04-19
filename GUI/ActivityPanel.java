package GUI;

import Main.DataStorage;
import Producers.Android;
import Producers.Building;
import Producers.Producer;
import Producers.Resource;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public class ActivityPanel extends JPanel {
    ArrayList<Android> androidsFree;
    Producer freeAndroidsCommunity;
    DataStorage dataStorage;
    Font guiFont;
    GroupPseudoSpinners groupPseudoSpinners;
    JList<Android> jList;

    HireListener hireListener;
    ShowListener showListener;
    AccessListener accessListener;

    public ActivityPanel(int listSize, DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.freeAndroidsCommunity = dataStorage.getFree();
        this.androidsFree = freeAndroidsCommunity.getAndroids();
        this.groupPseudoSpinners = new GroupPseudoSpinners(androidsFree);

        jList = new JList<>();
        DefaultListModel<Android> jlistModel = new DefaultListModel<>();
        jList.setModel(jlistModel);
        jList.setCellRenderer(new PanelRenderer());
        SelectDeselectListModel selectionModel = new SelectDeselectListModel();
        selectionModel.addListSelectionListener(e -> accessListener
                .setAssignButtonsEnable(selectionModel.isSelectionEmpty(),
                        showListener.getSelectedAndroidsList()));
        jList.setSelectionModel(selectionModel);

        JLabel selectedItemLabel = new JLabel();

        this.showListener = new ShowListener(listSize, jList, selectedItemLabel);
        this.hireListener = new HireListener();
        this.accessListener = new AccessListener();

        JPanel factoriesScreenPanel = new JPanel();
        factoriesScreenPanel.setLayout(new MigLayout("flowy, fillx"));
        factoriesScreenPanel.add(createFreeAndoidsPanel(), "grow");
        factoriesScreenPanel.add(createFactoriesPanel(), "grow");

        setLayout(new MigLayout("flowx, fill"));
        add(factoriesScreenPanel, "grow");
        add(createAndroidsListPanel(listSize, selectedItemLabel), "grow");
    }

    private JPanel createAndroidsListPanel(int listSize, JLabel selectedItemLabel) {
        JButton previousButton = new JButton("Previous " + listSize);
        JButton nextButton = new JButton("Next " + listSize);
        showListener.installPreviousButtonListener(previousButton);
        showListener.installNextButtonListener(nextButton);
        previousButton.setEnabled(false);
        nextButton.setEnabled(false);

        JPanel androidListPanel = new JPanel();
        androidListPanel.setLayout(new MigLayout("fillx, insets 0, flowy"));
        androidListPanel.setBackground(Color.white);
        androidListPanel.add(previousButton, "split 2, flowx, hidemode 3");
        androidListPanel.add(nextButton, "flowy, hidemode 3");
        androidListPanel.add(selectedItemLabel);
        androidListPanel.add(jList, "growx");
        return androidListPanel;
    }

    public JTabbedPane createFactoriesPanel() {
        JTabbedPane factoriesPane = new JTabbedPane(JTabbedPane.TOP);
        guiFont = new Font("Serif", Font.BOLD, 16);
        factoriesPane.setBackground(Color.white);

        JPanel garbagerPanel = createTabPanel(dataStorage.getSortedProducers(Android.StatType.GATHERING));
        factoriesPane.addTab("Garbagers", garbagerPanel);

        JPanel gardenerPanel = createTabPanel(dataStorage.getSortedProducers(Android.StatType.GARDENING));
        factoriesPane.addTab("Gardeners", gardenerPanel);

        JPanel mechanicPanel = createTabPanel(dataStorage.getSortedProducers(Android.StatType.MECHANIC));
        factoriesPane.addTab("Mechanics", mechanicPanel);

        JPanel alchemicPanel = createTabPanel(dataStorage.getSortedProducers(Android.StatType.ALCHEMY));
        factoriesPane.addTab("Alchemists", alchemicPanel);

        return factoriesPane;
    }

    private JPanel createTabPanel(ArrayList<Producer> producers) {
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new MigLayout("flowy, fillx"));
        producers.forEach(producer -> categoryPanel.add(createCommunityPanel(producer),
                "grow, center"));
        return categoryPanel;
    }

    private JPanel createFreeAndoidsPanel() {
        JLabel freeLabel = new JLabel("Free");
        JLabel freeLabelCount = new JLabel(String.valueOf(androidsFree.size()));
        freeLabel.setFont(guiFont);
        freeLabelCount.setFont(guiFont);
        hireListener.getCommunityUIMap().put(freeAndroidsCommunity, freeLabelCount);

        JButton dismissButton = createAssignButton(freeAndroidsCommunity);
        accessListener.getCommunityUIMap().put(freeAndroidsCommunity, dismissButton);

        JPanel freePanel = new JPanel();
        freePanel.setLayout(new MigLayout("flowy"));
        freePanel.add(freeLabel, "split 2, flowx, center");
        freePanel.add(freeLabelCount);
        freePanel.add(dismissButton, "split 2, flowy, right, sgx [buttonsWidth]");
        freePanel.add(createShowButton(freeAndroidsCommunity, "free"), "right, sgx [buttonsWidth]");
        return freePanel;
    }

    private JButton createAssignButton(Producer community) {
        JButton assignButton = new JButton("Assign here");
        assignButton.addActionListener(e -> hireListener
                .setValueAndUpdateUI(community, showListener));
        assignButton.setEnabled(false);
        return assignButton;
    }

    private JButton createShowButton(Producer producer, String textToShow) {
        JButton showListButton = new JButton("Show androids");
        showListButton.addActionListener(e -> showListener.showJList(showListButton, producer, textToShow));
        return showListButton;
    }

    public JPanel createCommunityPanel(Producer producer) {
        JPanel communityPanel = new JPanel();
        communityPanel.setLayout(new MigLayout("flowy"));
        communityPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        String communityName = producer.getProducerName();

        JLabel taskLabel = new JLabel(communityName);
        taskLabel.setFont(guiFont);
        ArrayList<Android> producerWorkers = producer.getAndroids();

        PseudoSpinner pseudoSpinner = new PseudoSpinner(groupPseudoSpinners, producerWorkers.size());
        pseudoSpinner.addPropertyChangeListener(e -> {
            hireListener.setValueAndUpdateUI(pseudoSpinner, producer, freeAndroidsCommunity, showListener);
            accessListener.setEnableNextButtons(groupPseudoSpinners.getPseudoSpinners(), androidsFree);
        });
        hireListener.getCommunityUIMap().put(producer, pseudoSpinner.getUi());

        JButton hireButton = createAssignButton(producer);
        accessListener.getCommunityUIMap().put(producer, hireButton);

        if (producer.getAndroidUnion() instanceof Building) {
            JButton buildButton = new JButton("Build");
            setBuildActions(producer, buildButton);
            communityPanel.add(buildButton, "split 2, flowx");
            buildButton.setEnabled(!producer.isBuilt());

            JCheckBox autoWork = new JCheckBox("Is auto work allowed?");
            autoWork.addActionListener(e-> producer.setAutoMode(autoWork.isSelected()));
            communityPanel.add(autoWork);
        }

        communityPanel.add(taskLabel, "split 2, flowx, center");
        communityPanel.add(pseudoSpinner.getUi());
        communityPanel.add(hireButton, "split 2, flowy, right, sgx [buttonsWidth]");
        communityPanel.add(createShowButton(producer, communityName),
                " right, sgx [buttonsWidth]");
        return communityPanel;
    }

    private void setBuildActions(Producer producer, JButton buildButton) {
        Map<Resource, Integer> craftMaterials = producer.getCraftMaterials();
        buildButton.addActionListener(e -> {
            AtomicBoolean isReadyToCraft = new AtomicBoolean(true);
            craftMaterials.forEach((material, number) -> {
                if (material.getValue() < number)
                    isReadyToCraft.set(false);
            });
            if (isReadyToCraft.get()) {
                producer.setBuilt(true);
                buildButton.setEnabled(false);
            }
        });

        buildButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                StringBuilder stringBuilder = new StringBuilder();
                craftMaterials.forEach(((resource, neededValue) -> {
                    stringBuilder.append("<p>")
                            .append(resource.getResourceName()).append(": ");
                    stringBuilder.append(Utils.getColouredValue(resource, neededValue))
                            .append("</p>");
                }));
                buildButton.setToolTipText("<html>" + stringBuilder + "</html>");
                super.mouseEntered(e);
            }
        });
    }
}
