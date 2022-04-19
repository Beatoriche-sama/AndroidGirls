package CreatorEngine;

import Main.App;
import Main.FileManage;
import Producers.AndroidUnion;
import Producers.Producer;
import Producers.Resource;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;

public class ObjectsCreator {
    protected final static NumberFormatter numberFormatter = new NumberFormatter();
    private LinkedCombobox linkedProducersCombobox, linkedResourcesCombobox;
    private Producer freeAndroidsProducer;
    private Resource knowledgeResource, energyResource;

    public static void main(String[] args) {
        //new ObjectsCreator();
    }

    public ObjectsCreator() {
        JFrame engineFrame = new JFrame("Object Creator");
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new MigLayout("flowx"));

        NumberFormat intFormat = NumberFormat.getIntegerInstance();
        numberFormatter.setFormat(intFormat);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setMinimum(0);

        linkedResourcesCombobox = new LinkedCombobox();
        linkedProducersCombobox = new LinkedCombobox();
        ProducerCreator producerCreator = new ProducerCreator(linkedResourcesCombobox, linkedProducersCombobox);
        ResourceCreator resourceCreator = new ResourceCreator(linkedResourcesCombobox, producerCreator);
        createBasicObjects();

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(e -> {
            try {
                Map<String, Object> map = new HashMap<>();
                map.put(App.resourcesKeyName, resourceCreator.getLocalResources());
                map.put(App.producersKeyName, producerCreator.getLocalProducers());
                map.put(App.freeCommunityKeyName, freeAndroidsProducer);
                map.put(App.knowledgeKeyName, knowledgeResource);
                map.put(App.energyKeyName, energyResource);
                FileManage.mapSave(App.saveData, map);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        JMenuItem loadMenuItem = new JMenuItem("Load");
        loadMenuItem.addActionListener(e -> {
            try {
                Map<Object, Object> map = FileManage.mapLoad(App.saveData);
                ArrayList<Resource> loadedResources = (ArrayList<Resource>) map.get(App.resourcesKeyName);
                resourceCreator.updateData(loadedResources);
                ArrayList<Producer> loadedProducers = (ArrayList<Producer>) map.get(App.producersKeyName);
                producerCreator.updateData(loadedProducers, loadedResources);

                freeAndroidsProducer = (Producer) map.get(App.freeCommunityKeyName);
                knowledgeResource = (Resource) map.get(App.knowledgeKeyName);
                energyResource = (Resource) map.get(App.energyKeyName);

                updateUIData();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        fileMenu.add(saveMenuItem);
        fileMenu.add(loadMenuItem);
        menuBar.add(fileMenu);

        mainPanel.add(menuBar, "north, growx");
        mainPanel.add(resourceCreator);
        mainPanel.add(producerCreator);

        engineFrame.setContentPane(new JScrollPane(mainPanel));
        engineFrame.setVisible(true);
        engineFrame.setSize(new Dimension(500, 500));
        engineFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void createBasicObjects() {
        freeAndroidsProducer = new Producer();
        freeAndroidsProducer.setProducerName("Free");
        freeAndroidsProducer.setAndroidUnion(new AndroidUnion());

        knowledgeResource = new Resource();
        knowledgeResource.setResourceName("Knowledge");
        energyResource = new Resource();
        energyResource.setResourceName("Energy");

        updateUIData();
    }

    private void updateUIData() {
        linkedProducersCombobox.addItem(freeAndroidsProducer);
        linkedResourcesCombobox.addItem(knowledgeResource);
        linkedResourcesCombobox.addItem(energyResource);
    }

    public static Border createTitledBorder(String title) {
        return BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),
                title);
    }

}
