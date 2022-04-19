package GUI;

import Producers.Android;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class PanelRenderer implements ListCellRenderer<Android>{
    Font font;

    public PanelRenderer() {
        font = new Font("Serif", Font.BOLD, 18);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Android value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        return createAndroidPanel(value, isSelected);
    }

    private JPanel createAndroidPanel(Android android, boolean isSelected){
        JPanel girlPanel = new JPanel();
        Image image = android.getIcon();
        ImageIcon icon = new ImageIcon(Utils.getScaledImage(image,
                image.getWidth(null) / 2, image.getHeight(null) / 2));
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setSize(iconLabel.getWidth() / 2, iconLabel.getHeight() / 2);
        String[] data = {android.getName(), android.getInfo(), android.getVersion()};
        girlPanel.setLayout(new MigLayout("fill"));
        girlPanel.setBackground(Color.WHITE);
        girlPanel.add(iconLabel, "growx");
        for (String s : data) {
            JLabel label = new JLabel(s);
            label.setFont(font);
            girlPanel.add(label, "split 3, flowy, growx");
        }
        girlPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        girlPanel.setBackground(isSelected ? Color.pink : Color.WHITE);
        return girlPanel;
    }

}
