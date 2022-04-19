package GUI;

import Main.DataStorage;
import Producers.Producer;
import Producers.Resource;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MainScreen extends JFrame {
    private JLabel energyCountLabel;
    public HashMap<Resource, JLabel> resourceLabelMap;
    public TechnologyTree technologyTree;
    public ActivityPanel activityPanel;
    private BufferedImage helperIcon;
    private final Font guiFont;

    private DataStorage dataStorage;

    public MainScreen(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        try {
            helperIcon = ImageIO.read(Objects.requireNonNull(getClass()
                    .getResource("/pics/nyasha.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        guiFont = new Font("Serif", Font.BOLD, 17);
        resourceLabelMap = new HashMap<>();

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(200, 200));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Android daughters~♡");
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass()
                .getResource("/pics/frameIcon.jpg")));
        setIconImage(icon.getImage());
    }

    public JPanel createContentPane() {
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new MigLayout("fill, flowx, insets 0"));

        JPanel pipAndroidScreen = new JPanel();
        pipAndroidScreen.setLayout(new MigLayout("fill"));
        pipAndroidScreen.setBorder(BorderFactory.createLineBorder(Color.black));

        activityPanel = new ActivityPanel(5, dataStorage);
        JButton androidsButton = new JButton("Activity manage");
        androidsButton.addActionListener(e -> changeScreen(pipAndroidScreen, activityPanel));

        technologyTree = new TechnologyTree();
        createTreeGraphic();

        JButton technologyTreeButton = new JButton("Technology Tree");
        technologyTreeButton.addActionListener(e -> changeScreen(pipAndroidScreen, technologyTree));

        JPanel helperPanel = createLeftInfoPanel();
        helperPanel.add(androidsButton, "split 2, flowx, tag ok");
        helperPanel.add(technologyTreeButton);

        contentPane.add(helperPanel, "sgx[nya], top, split 2, flowy, w 30%, growy, h 35%");
        contentPane.add(createResourcePane(dataStorage.getResources()), "sgx[nya], growy, h 65%");
        contentPane.add(pipAndroidScreen, "gapx 0, grow, w 70%");
        /*
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Image background = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/background.jpg"));
            g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
        }
        */
        return contentPane;
    }

    private void createTreeGraphic() {
        HashMap<Producer, TechnologyTree.GraphicElement> producerGraphicElement = new HashMap<>();
        dataStorage.getProducers().forEach(producer -> {
            TechnologyTree.GraphicElement graphicElement = technologyTree
                    .createGraphicElement(producer.getProducerName());
            producerGraphicElement.put(producer, graphicElement);
        });

        producerGraphicElement.forEach(((producer, graphicElement) -> {
            Producer parent = producer.getParent();
            boolean isBasic = parent == null;
            TechnologyTree.GraphicElement parentElement = null;
            if (!isBasic) parentElement = producerGraphicElement.get(parent);

            technologyTree.addElementToTree(parentElement, graphicElement);

            JButton inventButton = graphicElement.getInventButton();
            inventButton.setEnabled(!isBasic && Objects.requireNonNull(parent).isInvented());
            inventButton.addActionListener(e -> setAction(producer, graphicElement));
            inventButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    inventButton.setToolTipText("<html>Knowledge: "
                            + Utils.getColouredValue(dataStorage.getKnowledge(),
                            producer.getKnowledgeValue()) + "</html>");
                    super.mouseEntered(e);
                }
            });
        }));
    }

    private void setAction(Producer producer,
                           TechnologyTree.GraphicElement graphicElement) {
        if (dataStorage.getKnowledgeAmount() < producer.getKnowledgeValue()) return;
        dataStorage.spendKnowledge(producer.getKnowledgeValue());
        producer.setInvented(true);
        graphicElement.getInventButton().setEnabled(false);

        ArrayList<Producer> childrenList = producer.getChildren();
        childrenList.forEach(child -> activityPanel.createCommunityPanel(child));

        graphicElement.getChildren().forEach(childGraph ->
                childGraph.getInventButton().setEnabled(true));
    }

    private void changeScreen(JPanel parent, JPanel childPanel) {
        parent.removeAll();
        parent.add(childPanel, "grow");
        parent.repaint();
        parent.revalidate();
    }

    private JPanel createLeftInfoPanel() {
        JPanel imagePanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(Utils.getScaledImage(helperIcon, getWidth(), getHeight()), 0, 0, null);
            }
        };

        energyCountLabel = new JLabel();

        imagePanel.setLayout(new MigLayout("fill, flowy"));
        imagePanel.setOpaque(false);
        imagePanel.add(energyCountLabel);
        energyCountLabel.setFont(guiFont);
        return imagePanel;
    }

    private JScrollPane createResourcePane(ArrayList<Resource> resources) {
        JScrollPane resourcesPane = new JScrollPane();
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new MigLayout("flowy"));

        resources.stream()
                .filter(resource-> !resource.getResourceName().equals("energy"))
                .forEach(resource -> {
            JLabel resNameLabel = new JLabel(resource.getResourceName() + ": ");
            resNameLabel.setFont(guiFont);

            JLabel resCountLabel = new JLabel();
            resCountLabel.setFont(guiFont);
            resCountLabel.setText(String.valueOf((int) resource.getValue()));

            panel.add(resNameLabel, "split 2, flowx");
            panel.add(resCountLabel, "flowy");
            resourceLabelMap.put(resource, resCountLabel);
        });

        resourcesPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        resourcesPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        resourcesPane.setViewportView(panel);
        return resourcesPane;
    }
}
