package UI;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class GameFrame extends JFrame {
    private final ResourcesList resourcesUI;

    private final JTabbedPane tabbedPane;

    public GameFrame() {
        setUIFont(new javax.swing.plaf.FontUIResource(Font.SERIF, Font.PLAIN, 20));

        resourcesUI = new ResourcesList();
        FactoriesList factoriesUI = new FactoriesList();
        ExpeditionsList expeditionsUI = new ExpeditionsList();
        AndroidsList androidsUI = new AndroidsList();
        ResearchTree researchTree = new ResearchTree();


        tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);

        tabbedPane.addTab("Research",
                getScaledIcon("/research.png"),
                researchTree);

        tabbedPane.addTab("Expeditions",
                getScaledIcon("/expedition.png"),
                expeditionsUI);

        tabbedPane.addTab("Androids",
                getScaledIcon("/android.png"),
                androidsUI);

        tabbedPane.addTab("Factories",
                getScaledIcon("/factory.png"),
                factoriesUI);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new MigLayout("flowx, fill"));
        contentPanel.add(resourcesUI, "grow");
        contentPanel.add(tabbedPane, "grow");

        setContentPane(contentPanel);
        setName("Android girls");
        setSize(700, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, f);
        }
    }

    private ImageIcon getScaledIcon(String urlString) {
        URL url = GameFrame.class.getResource(urlString);
        ImageIcon imageIcon = new ImageIcon(url);
        Image image = imageIcon.getImage();
        Image scaledImage = image.getScaledInstance(
                20, 20, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    public void refresh() {
        resourcesUI.onUpdate();
        ((Updatable) tabbedPane.getSelectedComponent()).onUpdate();
    }
}
